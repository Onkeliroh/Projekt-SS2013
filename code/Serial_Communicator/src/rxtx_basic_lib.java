/* 
 * Author: Daniel Pollack
 */
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

import org.eclipse.swt.widgets.Display;



public class rxtx_basic_lib
{
	//input and output streams for sending and receiving data
	private InputStream in = null;
	private  OutputStream out = null;
	
	//wether this instance is connected to a port or not (default = false)
	private  boolean connected = false;
	
	//the serialport this instance is connected to
	private  SerialPort serialPort;
	
	//instance of the main window
	private Window window;
	
	//the timeout value for connecting with the port
    final static int TIMEOUT = 2000;

    //some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;
	
	
	//Constructor
	public rxtx_basic_lib(Window win)
	{
		this.window = win;
	}
	
	//searches for all devices connected with a serial port on the pc
	@SuppressWarnings({ "rawtypes" })
	public  Enumeration get_ports() {		
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
		if (connected)
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
				serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(bps,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);			
				
				System.out.println("Begin read/write");
			}
		}
	}
	
	public InputStream get_inputstream() throws IOException
	{
		return in;
	}
	
	public boolean is_connected()
	{
		if (connected)
			return true;
		else
			return false;
	}

	public static class com_listener implements Runnable
	{
		rxtx_basic_lib bums;
		Window windoof;
		InputStream in;
		public com_listener(rxtx_basic_lib ding, Window win, InputStream input)
		{
			this.bums = ding;
			this.windoof = win;
			this.in = input;
		}
		public void run() {
			int data;
	        byte[] buffer = new byte[1024];
	        String str;
	      
	        try
	        {
	            int len = 0;
	            while ( ( data = in.read()) > -1 )
	            {
	                if ( data == '\n' ) {
	                    break;
	                }
	                buffer[len++] = (byte) data;
	            }
	            str = new String(buffer,0,len);
	            System.out.print(str);
	            windoof.getList().add(str);
	        }
	        catch ( IOException e )
	        {
	            e.printStackTrace();
	            System.exit(-1);
	        }   
		}
	}
	
//	public boolean initListener()
//    {
//        try
//        {
//            serialPort.addEventListener(this);
//            serialPort.notifyOnDataAvailable(true);
//            
//            return true;
//        }
//        catch (TooManyListenersException e)
//        {
//        	System.err.println("ERROR: Too Many ListenerExceptions in initListener.");
//        	return false;
//    	}	
//    }
	
	//open the input and output streams
    //pre: an open port
    //post: initialized intput and output streams for use to communicate data
    public boolean initIOStream()
    {
        try {
            //
            in = serialPort.getInputStream();
            out = serialPort.getOutputStream();
            writeData(0, 0);
            
            return true;
        }
        catch (IOException e) {
            System.err.format("I/O Streams failed to open. ( %s )\n", e.toString());
            return false;
        }
    }

	
	//for a nice cleanup
	public  boolean exit() throws IOException
	{		
		serialPort.removeEventListener();
        serialPort.close();
        connected = false;
		
		return true;
	}

	
	
	//method that can be called to send data
    //pre: open serial port
    //post: data sent to the other device
    public void writeData(int leftThrottle, int rightThrottle)
    {
        try
        {
            out.write(leftThrottle);
            out.flush();
            //this is a delimiter for the data
            out.write(DASH_ASCII);
            out.flush();
            
            out.write(rightThrottle);
            out.flush();
            //will be read as a byte so it is a space key
            out.write(SPACE_ASCII);
            out.flush();
        }
        catch (Exception e)
        {
            System.err.format("Failed to write data. ( %s )\n", e.toString());
        }
    }
}
