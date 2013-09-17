package kickflick.utility;

import java.sql.Timestamp;
import java.util.*;

import kickflick.gui.Server_Main;
import kickflick.device.device;

public class server extends Timer{
    private kickflick.utility.serial_lib serial_com;
	private setting_parser set_pars = new setting_parser();
	private final parser input_parser;
    private Timer timer;
    private server Server;
	
	@SuppressWarnings("CanBeFinal")
    private List<device> devices = new ArrayList<device>();

    private server()
    {
        this.Server = this;
        this.init_communication();
        this.input_parser = new parser(this);

        this.init_timer();

        this.openWindow();
    }
	
	public static void main(String[] args)
	{ 
		server Server = new server();

        System.exit(0);
	}

	private void openWindow()
	{
		try {
            Server_Main window = new Server_Main();
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

    public void disconnect_pannstamp()
    {
        this.serial_com.get_connected_Port().removeEventListener();
        this.serial_com.exit();
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

    public void set_device(int index, device dev)
    {
        this.devices.set(index,dev);
    }
	
	public device get_device(int index)
	{
		return this.devices.get(index);
	}

    public void send_msg(byte[] msg)
    {
        if ( this.serial_com.is_connected())
        {
            serial_lib.com_writer writer = new serial_lib.com_writer(this.get_SerialCom().get_outputstream(),msg);
            Thread writer_thread = new Thread(writer);
            writer_thread.run();
            try
            {
                writer_thread.join();
            }
            catch(InterruptedException e)
            {
                e.fillInStackTrace();
            }
            //TODO mybe join function nessesary
        }
        //TODO remove comment
    }

    public void send_device(int index)
    {
        send_device(this.get_device(index));
    }


    //creates a byte array which will then be send to the server-panstamp
    public void send_device(device d)
    {
        byte[] msg = new byte[4];
        msg[0] = d.get_actuator_node();
        msg[1] = d.get_Personality().get_pattern();
//        msg[1] = 44;
        msg[2] = d.get_Personality().get_Color1();
        msg[3] = d.get_Personality().get_Color2();

        this.send_msg(msg);
    }

    private void init_timer()
    {
        this.timer = new Timer();
        this.timer.schedule(new TimerTask()
        {
            @Override
            public void run() {
                Timestamp stamp = new Timestamp(new Date().getTime());
                for (int i = 0 ; i < Server.devices.size() ; ++i)
                {
                    if(stamp.getTime() - Server.devices.get(i).get_timestamp().getTime() >= 60000 && Server.devices.get(i).get_Personality().get_State() != 0) //TODO make global
                    {
                        System.out.println("Server Timer: set device '"+ Server.devices.get(i).get_Personality().get_Name() +"' to default state.");
                        Server.devices.get(i).get_Personality().set_State((short)0);
                        Server.devices.get(i).set_new_timestamp();
                        Server.send_device(i);
                    }
                }
            }
        },3000,100);
    }

    public void stop_timer()
    {
        this.timer.cancel();
        this.timer.purge();
    }
}
