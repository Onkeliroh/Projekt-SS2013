package kickflick.utility;

/* 
 * Author: Daniel Pollack
 */
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;



public class serial_lib
{
	//input and output streams for sending and receiving data
	private InputStream in = null;
	private  OutputStream out = null;
	
	//Whether this instance is connected to a port or not (default = false)
	private  boolean connected = false;
	
	//the serialport this instance is connected to
	private SerialPort serialPort;
	
	//the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    //some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    private final static int NEW_LINE_ASCII = 124;
	
	
	//Constructor
	public serial_lib()
	{}
	
	//searches for all devices connected with a serial port on the pc
	@SuppressWarnings({ "rawtypes" })
    Enumeration get_ports() {
		Enumeration enumComm = CommPortIdentifier.getPortIdentifiers();
	    
	    if (enumComm != null)
	    {
	    	return enumComm;
	    }
	    else
	    {
	    	return null;
	    }
	}
	
	//creates list of all the serial port names, which are connected
	public  List<String> get_port_names()
	{
		@SuppressWarnings("rawtypes")
		Enumeration device_list = get_ports();
		if (device_list != null)
		{
			List<String> device_names = new ArrayList<String>();
			while (device_list.hasMoreElements())
			{
				CommPortIdentifier ID = (CommPortIdentifier) device_list.nextElement();
				device_names.add(ID.getName());
			}
			return device_names;
		}
		else
		{
			return null;
		}
	}
	
	public SerialPort get_connected_Port()
	{
		if (this.connected)
			return this.serialPort;
		else
			return null;
	}
	
	    
	public  void connect ( String portName, int bps ) throws Exception
	{
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if ( portIdentifier.isCurrentlyOwned() )
		{
			System.err.println("Error: Port is currently in use");
		}
		else
		{
			CommPort commPort = portIdentifier.open(portIdentifier.getClass().getName(),bps);

			if ( commPort instanceof SerialPort )
			{
				connected = true;
				this.serialPort = (SerialPort) commPort;
				this.serialPort.setSerialPortParams(bps,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

                this.initIOStream();
			}
		}
	}
	
	public InputStream get_inputstream() {
		return in;
	}
	
	public OutputStream get_outputstream() {
		return out;
	}
	
	public boolean is_connected()
	{
        return connected;
    }

	public static class com_listener implements Runnable
	{
		private final serial_lib bums_;
		private final InputStream in_;
		@SuppressWarnings("CanBeFinal")
        private byte[] Buffer_ = new byte[1024];
		
		public com_listener(serial_lib ding, InputStream input)
		{
			System.out.println("create Com-Listener");
			this.bums_ = ding;
			this.in_ = input;
		}
		public void run() {
            int tmp_int = 0;
            byte[] tmp = new byte[1024];
            try {
                int availableBytes = this.in_.available();
//                if (availableBytes > 0) {
                while (tmp_int < availableBytes)
                {
                    // Read the serial port
//                    this.in_.read(this.Buffer_, 0, availableBytes);
                    this.Buffer_[tmp_int] = (byte) this.in_.read();
                    System.out.print(this.Buffer_[tmp_int]+"\t");
                    ++tmp_int;
                }
            } catch (IOException e) {
            }

		}
		public byte[] get_buffer()
		{
			byte[] tmp = new byte[4];
            System.arraycopy(this.Buffer_,0,tmp,0,4);
            System.out.println(Arrays.toString(this.Buffer_));
            return tmp;

		}
	}
	
	/** */
    public static class com_writer implements Runnable
    {
        private final OutputStream out;
        private final byte[] str;
        
        public com_writer ( OutputStream out, String string )
        {
            this.out = out;
            this.str = string.getBytes();
        }
        
        public com_writer ( OutputStream out, byte[] bytes )
        {
        	this.out = out;
        	this.str = bytes;
        }
        
        public void run ()
        {
            try
            {
                System.out.println("Send: "+ Arrays.toString(this.str));
                this.out.write(this.str);
                this.out.flush();
            }
            catch (Exception e)
            {
                System.err.println("Failed to write data. com_writer (" + e.toString() + ")");
            }
        }
    }

	
	//open the input and output streams
    //pre: an open port
    //post: initialized intput and output streams for use to communicate data
    public boolean initIOStream()
    {
        try {
            //
            this.in = this.serialPort.getInputStream();
            this.out = this.serialPort.getOutputStream();
            
            return true;
        }
        catch (IOException e) {
            System.err.format("I/O Streams failed to open. ( %s )\n", e.toString());
            return false;
        }
    }

	//for a nice cleanup
	public  void exit() {
        serialPort.close();
        connected = false;
	}
}
