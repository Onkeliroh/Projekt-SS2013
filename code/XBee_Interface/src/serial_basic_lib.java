import gnu.io.CommPortIdentifier;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class serial_basic_lib {
	@SuppressWarnings({ "unused", "null", "rawtypes" })
	public static Enumeration get_ports() {		
		@SuppressWarnings("rawtypes")
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
	
	@SuppressWarnings("null")
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
