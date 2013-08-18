package kickflick.utility;

import java.util.ArrayList;
import java.util.List;

import kickflick.gui.Server_Main;
import kickflick.device.device;

public class server{
	private kickflick.gui.Server_Main window;
	public kickflick.utility.serial_lib serial_com;
	public setting_parser set_pars = new setting_parser();
	public parser input_parser = new parser(this);
	
	private List<device> devices = new ArrayList<device>();
	
	//maybe unnecessary
	//adress can't be 0 because 0 stands for every client
	private byte adress = 1;
	
	 
	
	public static void main(String[] args)
	{ 
		server Server = new server();
		
		
//		Server.read_settings(); //TODO maybe not needed at all
		Server.init_communication();
		Server.openWindow();
	}

	private void openWindow()
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
			
			//adds listener to server
			serial_com.get_connected_Port().addEventListener(input_parser);
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
		serial_com.initIOStream();
	}
	
	public void request_devices()
	{
		//TODO write
	}

	public byte[] compose_bytearray(byte receiver, byte sender, byte key, byte[] data)
	{
		byte[] dings = new byte[64];
		dings[0] = receiver;
		dings[1] = sender;
		dings[2] = key;
		System.arraycopy(data, 0, dings, 3, data.length);
		return dings;
	}
	
	private void read_settings()
	{	
		try {
			set_pars.parse_settings("res/keys");
		} finally {} 
		System.out.println(set_pars.get_element_keys());
		
	}
	
	//Getter
	public kickflick.utility.serial_lib get_SerialCom() {
			return serial_com;		
	}
	
	public byte get_server_adress()	{
		return adress;
	}
	
	public List<device> get_devices()
	{
		return this.devices;
	}
	
	public device get_device(int index)
	{
		return this.devices.get(index);
	}
}
