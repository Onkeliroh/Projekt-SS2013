package kickflick.device;

import java.util.Map;
import java.util.HashMap;

import kickflick.utility.color;
import kickflick.utility.pattern;

public enum presetpersonalities
{
	Paul("Paul",new personality(
			"Paul",
			(short)0,
			new byte[] { color.BLUE.get_key(), color.RED.get_key(), color.GREEN.get_key(), color.FUCHSIA.get_key() },    //Color 1
            new byte[] { color.BLUE.get_key(), color.RED.get_key(), color.GREEN.get_key(), color.FUCHSIA.get_key() },    //Color 2
            new byte[] { pattern.BLINK.get_key(), pattern.BLINK.get_key(), pattern.BLINK.get_key(), pattern.BLINK.get_key() },     //Pattern
            new HashMap<String, Byte[]>(){{put("bla",new byte[] {(byte)0,(byte)1,(byte)1});}}
    ));
//			new HashMap<String, Byte[]> (){ 
//				{
//					put("Tanja", new byte[]{pattern.BLINK.get_key(), color.RED.get_key(), color.BLUE.get_key()});
//					put("Mama", new byte[]{pattern.BLINK.get_key(), color.WHITE.get_key(), color.NONE.get_key()});
//				}
//			}
//	));			
		
	Tanja("Tanja",new personality(
			"Tanja",
			(short)0,
            new byte[] { 1, 1, 1, 1 },
            new byte[] { 1, 1, 1, 1 },
            new byte[] { 1, 1, 1, 1 }
			));
	
	Mama("Mama",new personality(
			"Mama",
			(short)0,
            new byte[] { 1, 1, 1, 1 },
            new byte[] { 1, 1, 1, 1 },
            new byte[] { 1, 1, 1, 1 }
			));

	private final String name_;
	private final personality personality_;
		
	private presetpersonalities(String name, personality perso)
    {
		name_ = name;
		personality_ = perso;
	}

	public personality get_personality()
    {
        return personality_;
    }
*/
}
