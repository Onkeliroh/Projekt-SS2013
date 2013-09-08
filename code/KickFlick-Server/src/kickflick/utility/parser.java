package kickflick.utility;

import java.io.IOException;
import java.util.Arrays;

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

		if (arg.length == 4) // must contain at least sender receiver and key
		{
            int index;
			if ( !this.Server_.get_devices().isEmpty()) // if NOT emtpy
			{
                if (arg[0] % 2 == 0)
                    index = find_device_sensor_node(arg[0]);
                else if ( arg[0] % 2 == 1)
                    index = find_device_sensor_node(arg[0]);
                else
                {
                    System.err.println("Parser Error: incorrect Packet Sender ID");
                    return;
                }
			}
			else    //if empty -> create new device and fill
			{
                device tmp = new device ( new personality());

                if (arg[0] % 2 == 0)
                {
                    tmp.set_sensor_node(arg[0]);
                    tmp.set_actuator_node(arg[0]++);     //actuator is next to sensor node
                }
                else if ( arg[0] % 2 == 1)
                {
                    tmp.set_actuator_node(arg[0]);
                    tmp.set_sensor_node(arg[0]--);      //sensor is next to actuator node
                }
                else
                {
                    System.err.println("Parser Error: incorrect Packet Sender ID");
                    return;
                }

                index = this.Server_.get_devices().size();
                this.Server_.get_devices().add(index, tmp);
			}

            //send device information
            if ( this.Server_.get_device(index).get_trigger_map().containsKey(arg[1]))
            {
                this.Server_.get_device(index).set_new_timestamp();

                this.Server_.get_device(index).get_Personality().inc_state();
                msg[0] = this.Server_.get_device(index).get_actuator_node();
                msg[1] = reaction_keys.SET_PATTERN.get_key();
                msg[2] = this.Server_.get_device(index).get_Personality().get_pattern();

                send_msg(msg);

                msg[1] = reaction_keys.SET_COLORS.get_key();
                msg[2] = this.Server_.get_device(index).get_Personality().get_Color1();
                msg[3] = this.Server_.get_device(index).get_Personality().get_Color2();

                send_msg(msg);
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

	@Override
	public void serialEvent(SerialPortEvent arg0) {
        System.out.println("INCOMMING!!!");
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

    private void send_msg(byte[] msg)
    {
        serial_lib.com_writer writer = new serial_lib.com_writer(this.Server_.get_SerialCom().get_outputstream(),msg);
        Thread writer_thread = new Thread(writer);
        writer_thread.run();
        //TODO mybe join function nessesary
    }
}
