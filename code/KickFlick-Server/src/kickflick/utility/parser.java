package kickflick.utility;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import kickflick.device.*;

class parser implements SerialPortEventListener {
	private final server Server_;

	public parser(server Serv)
	{
		System.out.println("Create Parser");
		this.Server_=Serv;
	}

	public void parse(byte[] arg)
	{
        Timestamp stamp = new Timestamp(new Date().getTime());
		System.out.print( new SimpleDateFormat("HH:mm:ss").format(stamp)+" | Parser received message: ");
        System.out.println(Arrays.toString(arg));

        byte[] msg = new byte[4];

		if (arg.length >= 4 && checksum_check(arg)) // must contain at least sender receiver and key and must have correct checksum
		{
            int index = -1; //by default parser assumes, that no device is present
            if (arg[0] % 2 == 0)
                index = find_device_sensor_node(arg[0]);
            else if ( arg[0] % 2 == 1)
                index = find_device_actuator_node(arg[0]);

            if ( index == -1 )
			{    //if empty OR no device found -> create new device and fill
			    System.out.println("Parser: create new Device");
                device tmp = new device ( new personality());

                if (arg[0] % 2 == 0)
                {
                    tmp.set_sensor_node(arg[0]);
                    tmp.set_actuator_node(++arg[0]);     //actuator is next to sensor node
                }
                else if ( arg[0] % 2 == 1)
                {
                    tmp.set_actuator_node(arg[0]);
                    tmp.set_sensor_node(--arg[0]);      //sensor is next to actuator node
                }
                else
                {
                    System.err.println("Parser Error: incorrect Packet Sender ID");
                    return;
                }

                index = this.Server_.get_devices().size();
                this.Server_.get_devices().add(index, tmp);
			}

            switch (arg[1])
            {
                case system_keys.BATTERY_LOW:
                {
                    if ( !this.Server_.get_device(index).is_battery_low() )
                        this.Server_.get_device(index).toggle_battery_low();
                    break;
                }
                case system_keys.STILL_ALIVE:
                {
                    this.Server_.get_device(index).set_new_timestamp_last_heard_of();
//                    this.Server_.send_msg(new byte[]{arg[0],system_keys.MESSAGE_RECEIVED,0,0});
                    break;
                }
                case system_keys.FOUND_NEIGHBOR:
                {
                    int neighbor_id = find_device_sensor_node(arg[2]);  //TODO create new device if not found
                    if ( neighbor_id != -1 )
                        if (!this.Server_.get_device(index).has_neighbor())
                        {
                            this.Server_.send_neighbor(this.Server_.get_device(index), this.Server_.get_device(neighbor_id));
                            this.Server_.get_device(index).set_neighbor(this.Server_.get_device(neighbor_id));
                            this.Server_.get_device(neighbor_id).set_neighbor(this.Server_.get_device(index));

                            System.out.println("Found  Neighbor for: " + this.Server_.get_device(index).get_Personality().get_Name());
                        }
                        else
                            System.out.println("Device "+this.Server_.get_device(index).get_Personality().get_Name() + " allready got a neighbor");
                    else
                        System.err.println("Found NO Neighbor for: " + this.Server_.get_device(index).get_Personality().get_Name());

                    break;
                }
                default:
                {
                    //only changes state if there is no known neihbor
                    if ( !this.Server_.get_device(index).has_neighbor())
                    {
                        if ( stamp.getTime() - this.Server_.get_device(index).get_timestamp().getTime() >= this.Server_.get_device(index).get_Personality().get_state_duration())      //if the time difference between the last and this contact is big enougth
                        {
                            //send device information
                            for ( Map.Entry entry : this.Server_.get_device(index).get_trigger_map().entrySet())
                            {
                                keys k = (keys) entry.getKey();
                                if (k.get_key() == arg[1] && (Boolean) entry.getValue())
                                {
                                    System.out.println("Found key: " +k.get_name() +" and reacing to it");
                                    //increment state
                                    this.Server_.get_device(index).get_Personality().inc_state();

                                    //set current reaction acording
                                    this.Server_.get_device(index).get_Personality().set_current_reaction(k);

                                    //send device(reaction) depending on the current reaction (previous step is important)
                                    this.Server_.send_device(index);

                                    this.Server_.get_device(index).set_new_timestamp();

                                    break;
                                }
                            }
                        }
                    }
                }
            }
            this.Server_.get_device(index).set_new_timestamp_last_heard_of();
		}
		else {
			System.err.println("Parser received corrupt message!");
		}
	}

	private int find_device_sensor_node(byte address)
	{
		for ( int i = 0; i < this.Server_.get_devices().size() ; ++i)
			if ( this.Server_.get_device(i).get_sensor_node() == address)
				return i;
		//found nothing
		return -1;
	}

    private int find_device_actuator_node(byte address)
    {
        for ( int i = 0 ; i < this.Server_.get_devices().size() ; ++i )
            if ( this.Server_.get_device(i).get_actuator_node() == address)
                return i;
        return -1; //found nothing
    }

    private boolean checksum_check(byte[] arg)
    {
        byte sum = 0;
        for ( int i = 0; i < arg.length-1; ++i)
        {
            sum += arg[i];
//            System.out.println(i + "\t" + sum + "\t" + arg[arg.length-1]);
        }
        if ( sum == arg[arg.length-1])
            return true;
        return false;
    }

	public void serialEvent(SerialPortEvent arg0) {
//        System.out.println("INCOMMING!!!");
        serial_lib.com_listener horcher = new serial_lib.com_listener(
                this.Server_.get_SerialCom(), this.Server_.get_SerialCom().get_inputstream()
                );
        Thread thread = new Thread(horcher);
        thread.start();
        try
        {
            thread.join();
        }
        catch(InterruptedException e)
        {
            e.fillInStackTrace();
        }

        this.parse(horcher.get_buffer());
	}
}
