package kickflick.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class setting_parser {
	private Map< String , Byte > meta_keys = new HashMap< String , Byte >(); 
	private Map< String , Byte > element_keys = new HashMap< String , Byte >();
	private Map< String , action_class > action_keys = new HashMap< String , action_class >();
	
	final String default_path = "res/keys";
	
	void parse_settings()
	{
		parse_settings(default_path);
	}
	
	@SuppressWarnings("null")
	void parse_settings(final String file_path)
	{		
		try {
			FileReader reader = new FileReader(file_path);
			BufferedReader settings_reader = new BufferedReader(reader);
			
			String line = null;
			
			while (( line = settings_reader.readLine() ) != null) 
			{
//				System.out.println(line);t
				String[] parts = line.split("\\s");
				
				if ( parts.length >= 3)
				{
					switch (parts[0].charAt(0))
					{
						case '#'://nothing because it's a comment
						{
							break;
						}
						case 'c':
						{
							//first = name, second = key
							this.meta_keys.put( parts[1], parts[2].getBytes()[0] );
							break;
						}
						case 'e':
						{
							//first = name, second = key
							this.element_keys.put( parts[1], parts[2].getBytes()[0] );
							break;
						}
						case 'a':
						{
							//first = name_element, second = name, third = key
							if (element_keys.containsKey(parts[3]))
								this.action_keys.put( parts[3], new action_class(parts[1], parts[2].getBytes()[0]) );
							break;
						}
					}
				}
			}	
			
			settings_reader.close();
			reader.close();
			
		} catch (IOException e) {
			System.err.println("Oops something went from, while reading the settings.");
		}	
		
		System.out.println(this.element_keys);
		System.out.println(this.action_keys);
	}
	
	//Getter
	Map<String,Byte> get_meta_keys()
	{
		return this.meta_keys;
	}
	
	Map<String, Byte> get_element_keys()
	{
		return this.element_keys;
	}
	
	Map<String, action_class> get_action_keys()
	{
		return this.action_keys;
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
		
		public String toString()
		{
			return new String(this.name_ + " | " + this.key_);
		}
	}
}
