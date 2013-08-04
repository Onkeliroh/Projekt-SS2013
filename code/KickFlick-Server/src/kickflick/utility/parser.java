package kickflick.utility;

import java.io.IOException;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class parser implements SerialPortEventListener {
	private server Server_;
	
	
	//TODO write parser
	public parser() {	
	}
	
	public parser(server Serv)
	{
		this.Server_=Serv;
	}
	
	public void parse(String arg0)
	{
		
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		try {
			serial_lib.com_listener horcher = new serial_lib.com_listener(
					this.Server_.serial_com, this.Server_.serial_com.get_inputstream()
					);
			Thread read = new Thread(horcher);
//			read.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
