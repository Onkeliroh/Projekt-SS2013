package kickflick.utility;

import java.util.ArrayList;
import java.util.List;

import kickflick.gui.Server_Main;
import kickflick.device.device;

public class server{
	private kickflick.gui.Server_Main window;
	public kickflick.utility.serial_lib serial_com;
	public setting_parser set_pars = new setting_parser();
	public parser input_parser;
	
	private List<device> devices = new ArrayList<device>();

    public server()
    {
        this.init_communication();
        this.input_parser = new parser(this);

        this.openWindow();
    }
	
	public static void main(String[] args)
	{ 
		server Server = new server();
		
		
//		Server.read_settings(); //TODO maybe not needed at all
//		Server.init_communication();

        //Server.connect_panstamp("/dev/ttyACM0",9600);
//        Server.devices.add(new device());
//        Server.devices.add(new device());


//        Server.input_parser = new parser(Server);

//		Server.openWindow();
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
			this.serial_com.connect(str, Baut);

            //add Listener
            this.serial_com.get_connected_Port().addEventListener(this.input_parser);
            this.serial_com.get_connected_Port().notifyOnDataAvailable(true);

		} catch (Exception e) {
			System.err.println(e.toString());
		}
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
	
//	private void read_settings()
//	{
//		try {
//			this.set_pars.parse_settings("res/keys");
//		} finally {}
//		System.out.println(set_pars.get_element_keys());
//
//	}
	
	//Getter
	public kickflick.utility.serial_lib get_SerialCom() {
			return this.serial_com;		
	}
	
	public List<device> get_devices()
	{
		return this.devices;
	}

    public void set_device(int index, kickflick.device.device dev)
    {
        for ( device d : this.devices)
        {
            System.out.print(d.toString() + "\t");
        }

        try{
            this.devices.set(index,dev);
        }
        catch ( Exception  e)
        {
            System.out.println("Server set_device: " + e.toString());
        }
    }
	
	public device get_device(int index)
	{
		return this.devices.get(index);
	}
}
