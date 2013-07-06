package kickflick.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class setting_parser {
	Map< String , Byte > meta_keys; 
	Map< String , Byte > element_keys;
	Map< String , action_class > action_keys;
	
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
			
			String line = null;
			
			do 
			{
				line = settings_reader.readLine();
				//line contains informations -> begin parsing
				if (!line.isEmpty())
				{
					String[] parts = line.split("\\s");
					
					switch (parts[0].charAt(0))
					{
					case '#'://nothing because it's a comment
					case 'c':
						//first = name, second = key
						meta_keys.put( parts[1], parts[2].getBytes("US-ASCII")[0] );
					case 'e':
						//first = name, second = key
						element_keys.put( parts[1], parts[2].getBytes("US-ASCII")[0] );
					case 'a':
						//first = name_element, second = name, third = key
						if (element_keys.containsKey(parts[3]))
							action_keys.put( parts[3], new action_class(parts[1], parts[2].getBytes("US-ASCII")[0]) );
					}
				}
			}
			while (line != null);

			
			
		} catch (IOException e) {
			System.err.println("Oops something went from, while reading the settings.");
		}	
	}
	
	//Getter
	Map<String,Byte> get_meta_keys()
	{
		return meta_keys;
	}
	
	Map<String, Byte> get_element_keys()
	{
		return element_keys;
	}
	
	Map<String, action_class> get_action_keys()
	{
		return action_keys;
	}
	
	class action_class
	{
		String name_;
		Byte key_;
		
		action_class(String name, Byte key)
		{
			this.name_ = name;
			this.key_ = key;
		}
	}
}
