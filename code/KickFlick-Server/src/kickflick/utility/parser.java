package kickflick.utility;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry;
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
	
	void parse(byte[] arg)
	{
		System.out.print("Parser received message: ");
        System.out.println(Arrays.toString(arg));

        byte[] msg = new byte[4];

		if (arg.length >= 4) // must contain at least sender receiver and key
		{
            int index;
			if ( !this.Server_.get_devices().isEmpty()) // if NOT emtpy
			{
                if (arg[0] % 2 == 0)
                    index = find_device_sensor_node(arg[0]);
                else if ( arg[0] % 2 == 1)
                    index = find_device_actuator_node(arg[0]);
                else
                {
                    System.err.println("Parser Error: incorrect Packet Sender ID");
                    return;
                }
			}
			else    //if empty -> create new device and fill                                                                                               g
			{
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

            Timestamp stamp = new Timestamp(new Date().getTime());
            if ( this.Server_.get_device(index).get_timestamp().getTime() - stamp.getTime() >= -10000)      //if the time difference between the last and this contact is big enougth
            {
                //send device information
                boolean found = false;
                for ( Map.Entry entry : this.Server_.get_device(index).get_trigger_map().entrySet())
                {
                    keys k = (keys) entry.getKey();
                    if (k.get_key() == arg[1] && (Boolean) entry.getValue())
                    {
                        found = true;
                        System.out.println("Parser: Found key");
                    }
                }
                if ( found )
                {

                    this.Server_.get_device(index).get_Personality().inc_state(); //TODO set inc delay

                    this.Server_.get_device(index).set_new_timestamp();

                    this.Server_.send_device(index);
                }
                else
                    System.out.println("Parser found no match.");
            }
		}
		else {
			System.err.println("Parser received empty message!");
		}
	}
	
	int find_device_sensor_node(byte address)
	{
		for ( int i = 0; i < this.Server_.get_devices().size() ; ++i)
			if ( this.Server_.get_device(i).get_sensor_node() == address)
				return i;
		//found nothing
		return -1;
	}

    public int find_device_actuator_node(byte address)
    {
        for ( int i = 0 ; i < this.Server_.get_devices().size() ; ++i )
            if ( this.Server_.get_device(i).get_actuator_node() == address)
                return i;
        return -1; //found nothing
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
