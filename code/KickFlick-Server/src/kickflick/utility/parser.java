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
