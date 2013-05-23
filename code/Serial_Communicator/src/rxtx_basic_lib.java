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
import java.util.Enumeration;
import java.util.List;


public class rxtx_basic_lib {
	
	//searches for all devices connected with a serial port on the pc
	@SuppressWarnings({ "rawtypes" })
	public static Enumeration get_ports() {		
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
	public static List<String> get_port_names()
	{
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
	
	//creates array of all the serial port names, which are connected
	public static String[] get_port_names_array()

	{
		@SuppressWarnings("rawtypes")
		Enumeration device_list = get_ports();
		if (device_list != null)
		{
			//10 should be enougth ports
			String[] device_array = new String[10];
			
			int count = 0;
			
			while (device_list.hasMoreElements())
			{
				CommPortIdentifier ID = (CommPortIdentifier) device_list.nextElement();
				device_array[count]= ID.getName();
				count++;
			}
			System.out.println(device_array[0]);
			return device_array;
		}
		else
		{
			String[] Fail = new String[1];
			return Fail;
		}
	}

	    
	public static boolean connect ( String portName, int bps ) throws Exception
	{
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if ( portIdentifier.isCurrentlyOwned() )
		{
			System.err.println("Error: Port is currently in use");
			return false;
		}
		else
		{
			CommPort commPort = portIdentifier.open(portIdentifier.getClass().getName(),bps);

			if ( commPort instanceof SerialPort )
			{
				SerialPort serialPort = (SerialPort) commPort;
//				serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
				serialPort.setSerialPortParams(bps,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();

				System.out.println("Begin read/write");
				(new Thread(new SerialReader(in))).start();
				(new Thread(new SerialWriter(out))).start();

				return true;
			}
			else
			{
				return false;				
			}
		}     
	}

	/** */
	public static class SerialReader implements Runnable 
	{
		InputStream in;

		public SerialReader ( InputStream in )
		{
			this.in = in;
		}

		public void run ()
		{
			byte[] buffer = new byte[1024];
			int len = -1;
			try
			{
				while ( ( len = this.in.read(buffer)) > -1 )
				{
					System.out.print(new String(buffer,0,len));
				}
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}            
		}
	}

	/** */
	public static class SerialWriter implements Runnable 
	{
		OutputStream out;

		public SerialWriter ( OutputStream out )
		{
			this.out = out;
		}

		public void run ()
		{
			try
			{                
				int c = 0;
				while ( ( c = System.in.read()) > -1 )
				{
					this.out.write(c);
				}                
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}            
		}
	}
}
