/* 
 * Author: Daniel Pollack
 */
import gnu.io.CommPortIdentifier;

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
}
