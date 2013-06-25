package kickflick.utility;

import kickflick.gui.Server_Main;

public class server {
	private kickflick.gui.Server_Main window;
	public kickflick.utility.serial_lib serial_com;
	
	private byte adress = 0;
	
	public static void main(String[] args)
	{ 
		server Server = new server();
		
		Server.init_communication();
		Server.openWindow();
	}

	public void openWindow()
	{
		try {
			window = new Server_Main();
			window.set_Server(this);
			window.open();
		} catch (Exception e) {
			System.err.println(e.toString());
		}		
	}
	
	private void init_communication()
	{
		this.serial_com = new kickflick.utility.serial_lib();
	}
	
	public void connect_panstamp(String str, int Baut)
	{
		try {
			serial_com.connect(str, Baut);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		
		//initListener
		
		if ( serial_com.is_connected() )
		{
			//request Devices
		}
	}
	
	public void init_Listener()
	{
		
	}
	
	public void request_devices()
	{
		
	}
	
	public void compose_bytearray(byte receiver, byte sender, byte key, byte)
	
	
	//Getter
	public kickflick.utility.serial_lib get_SerialCom() {
			return serial_com;		
	}
	
	public byte get_server_adress()	{
		return adress;
	}
}
