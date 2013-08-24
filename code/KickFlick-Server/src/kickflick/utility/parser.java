package kickflick.utility;

import java.io.IOException;

import kickflick.utility.serial_lib.com_listener;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class parser implements SerialPortEventListener {
	private server Server_;
	
	public parser(server Serv)
	{
		this.Server_=Serv;
	}
	
	public void parse(byte[] arg)
	{
		System.out.println("Parser received message: " + arg.toString());
		if (arg.length > 2) // must contain at least sender receiver and key
		{
			if ( !this.Server_.get_devices().isEmpty())
			{
				
			}
			else
			{
	//			this.Server_.get_devices().add(new device(new personality(),(byte) 0, )
			}
			
			switch (arg[2])
			{
				
			}
		}
		else {
			System.err.println("Parser received empty message!");
		}
	}
	
	public int find_device(byte address)
	{
		for ( int i = 0; i < this.Server_.get_devices().size() ; ++i)
		{
			if ( this.Server_.get_device(i).get_sender_address() == address)
			{
				return i;
			}
			
			if ( this.Server_.get_device(i).get_receiver_address() == address)
			{
				return i;
			}
		}
		//found nothing
		return -1;
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		try {
			serial_lib.com_listener horcher = new serial_lib.com_listener(
					this.Server_.serial_com, this.Server_.serial_com.get_inputstream()
					);
			horcher.run();
			this.parse(horcher.get_buffer());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
