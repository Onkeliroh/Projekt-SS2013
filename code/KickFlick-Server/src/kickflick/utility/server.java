package kickflick.utility;

import kickflick.device.device;
import kickflick.device.reaction;
import kickflick.gui.Server_Main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

public class server extends Timer implements Serializable
{

    //fixed variables
    //!!!only change if you want timings to be different
    private final int OUT_OF_RANGE = 120000;    //default : 120000ms
    private final int CHECK_STATE_TIME = 30000; //0.5 minutes
    //END fixed variables


    private kickflick.utility.serial_lib serial_com;
    private final parser input_parser;
    private Timer timer;
    private server Server;

    @SuppressWarnings("CanBeFinal")
    private List<device> devices = new ArrayList<device>();

    private server() {
        this.Server = this;
        this.init_communication();
        this.input_parser = new parser(this);

        this.init_timer();

        this.openWindow();
    }

    public static void main(String[] args) {
        server Server = new server();
        //if the server constructor is finished, the program will bestoped
        System.exit(0);
    }

    private void openWindow() {
        try
        {
            Server_Main window = new Server_Main();
            window.set_Server(this);
            window.open();
        } catch (Exception e)
        {
            System.err.println(e.toString());
        }
    }

    private void init_communication() {
        this.serial_com = new kickflick.utility.serial_lib();
    }

    public void connect_panstamp(String str, int Baut) {
        try
        {
            this.serial_com.connect(str, Baut);

            //add Listener
            this.serial_com.get_connected_Port().addEventListener(this.input_parser);
            this.serial_com.get_connected_Port().notifyOnDataAvailable(true);

        } catch (Exception e)
        {
            System.err.println(e.toString());
        }
    }

    public void disconnect_pannstamp() {
        this.serial_com.get_connected_Port().removeEventListener();
        this.serial_com.exit();
    }

    //is meant to be part of a configuation process
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

    public List<device> get_devices() {
        return this.devices;
    }

    public void set_device(int index, device dev) {
        this.devices.set(index, dev);
    }

    public device get_device(int index) {
        return this.devices.get(index);
    }

    //basic sending function, takes a byte array and sends it to the serialport, if connected
    public void send_msg(byte[] msg) {
        if (this.serial_com.is_connected())
        {
            serial_lib.com_writer writer = new serial_lib.com_writer(this.get_SerialCom().get_outputstream(), msg);
            Thread writer_thread = new Thread(writer);
            writer_thread.run();
            try
            {
                writer_thread.join();
            } catch (InterruptedException e)
            {
                e.fillInStackTrace();
            }
        }
    }

    public void send_device(int index) {
        send_device(this.get_device(index));
    }


    //creates a byte array which will then be send to the server-panstamp
    public void send_device(device d) {
        byte[] msg = new byte[4];
        byte[] tmp = d.get_Personality().get_current_reaction_array();
        msg[0] = d.get_actuator_node();
        msg[1] = tmp[0]; //pattern
        msg[2] = tmp[1]; //color1
        msg[3] = tmp[2]; //color2

        this.send_msg(msg);
    }

    public void send_neighbor(device d, device neighbor) {
        //takes the neighbor values and puts them in to a byte array, which is to be send
        byte[] msg = new byte[4];
        reaction neighbor_pers = d.get_Personality().get_neighbor(neighbor.get_Personality().get_Name());
        msg[0] = d.get_actuator_node();
        msg[1] = neighbor_pers.get_pattern().get_key();
        msg[2] = neighbor_pers.get_color1().get_key();
        msg[3] = neighbor_pers.get_color2().get_key();

        this.send_msg(msg);

        msg[0] = neighbor.get_actuator_node();

        this.send_msg(msg);

    }


    //checks for a fixed frequency whether the device is idle and therefor will be reset to the default state if no neighbor is known
    private void init_timer() {
        this.timer = new Timer();
        this.timer.schedule(new TimerTask()
        {
            @Override
            public void run() {
                Timestamp stamp = new Timestamp(new Date().getTime());
                for (int i = 0; i < Server.devices.size(); ++i)
                {
                    if (!Server.get_device(i).has_neighbor())
                    {
                        if (stamp.getTime() - Server.devices.get(i).get_timestamp().getTime() >= CHECK_STATE_TIME)
                            if (Server.devices.get(i).get_Personality().get_State() > 0)
                            {
                                System.out.println("Server Timer: set device '" + Server.devices.get(i).get_Personality().get_Name() + "\t Id: " + i + "' to default state.");
                                Server.devices.get(i).get_Personality().set_State((short) 0);
                                Server.devices.get(i).set_new_timestamp();
//                                Server.send_device(i);
                            }
                    }
                    if (stamp.getTime() - Server.devices.get(i).get_timestamp_last_heard_of().getTime() >= OUT_OF_RANGE)
                    {
                        Server.get_device(i).get_Personality().set_State((short) -1);
                    }
                }
            }
        }, 3000, 100);
    }

    public void stop_timer() {
        this.timer.cancel();
        this.timer.purge();
    }

    //returns a array of all known unique personality names
    public ArrayList<String> get_PersonalitiesCount() {
        ArrayList<String> PersNames = new ArrayList<String>();
        for (device d : this.Server.get_devices())
        {
            PersNames.add(d.get_Personality().get_Name());
        }
        Set<String> setNames = new LinkedHashSet<String>(PersNames);
        PersNames = new ArrayList<String>(setNames);
        return PersNames;
    }

    //loades a previously saved device list
    public void loadDevicesFromFile(String fName) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream f_in;

        f_in = new FileInputStream(fName);
        // Read object using ObjectInputStream
        ObjectInputStream obj_in = new ObjectInputStream(f_in);

        // Read an object
        Object obj = obj_in.readObject();

        devices = (((ArrayList<device>) obj));//.getAnimations());
        System.out.println("The devices were loaded from: ");

        obj_in.close();
    }

    //saves the current device list
    public void saveDevicesToFile(String fname) throws IOException {
        // Write to disk with FileOutputStream
        FileOutputStream f_out;

        f_out = new FileOutputStream(fname);

        ObjectOutputStream obj_out;
        obj_out = new ObjectOutputStream(f_out);


        // Write object out to disk
        obj_out.writeObject(devices);
        // MessageDialog.openInformation(shlAnimationBuilder, "Info",
        // "The animations have been saved");

        obj_out.close();
        System.out.println("Devices were saved to file.");

    }


}
