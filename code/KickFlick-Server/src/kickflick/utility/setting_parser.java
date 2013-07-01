package kickflick.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class setting_parser {
	Map<String,Byte>meta_keys; 
	Map<String,Byte>element_keys;
	Map<String,Byte>action_keys;
	
	final String default_path = "res/keys"; //ToDo set path 
	
	void parse_settings()
	{
		parse_settings(default_path);
	}
	
	void parse_settings(final String file_path)
	{
		try {
			FileReader reader = new FileReader(file_path);
			BufferedReader settings_reader = new BufferedReader(reader);
			
			String line = settings_reader.readLine();
			
			
			//line contains informations -> begin parsing
			if (!line.isEmpty())
			{
				String[] parts = line.split("\\s");
				
				switch (parts[0].charAt(0))
				{
				case '#'://nothing because it's a comment
				case 'c':
					meta_keys.put(parts[1], parts[2].getBytes("US-ASCII")[0]);
				case 'e':
					element_keys.put(parts[1], parts[2].getBytes("US-ASCII")[0]);
				case 'a':
					action_keys.put(parts[1], parts[2].getBytes("US-ASCII")[0]);
				}
			}
			
			
		} catch (IOException e) {
			System.err.println("Oops something went from, while reading the settings.");
		}	
	}
	
	//Getter
	Map<String,Byte> get_meta_keys()
	{
		return meta_keys;
	}
	
	Map<String,Byte> get_element_keys()
	{
		return element_keys;
	}
	
	Map<String,Byte> get_action_keys()
	{
		return action_keys;
	}
}
