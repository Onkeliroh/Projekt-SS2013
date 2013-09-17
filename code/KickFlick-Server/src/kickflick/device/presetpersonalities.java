package kickflick.device;

import java.util.Map;
import java.util.HashMap;

import kickflick.utility.color;
import kickflick.utility.pattern;

public enum presetpersonalities
{
	Paul(new personality(
			"Paul",
			(short)0,
			new byte[] { color.BLUE.get_key(), color.RED.get_key(), color.GREEN.get_key(), color.FUCHSIA.get_key() },    //Color 1
            new byte[] { color.BLUE.get_key(), color.RED.get_key(), color.GREEN.get_key(), color.FUCHSIA.get_key() },    //Color 2
            new byte[] { pattern.BLINK.get_key(), pattern.BLINK.get_key(), pattern.BLINK.get_key(), pattern.BLINK.get_key() },     //Pattern
            new HashMap<String, byte[]>(){
                {
                    put("Tanja",new byte[] {pattern.BLINK.get_key(),color.RED.get_key(),color.BLUE.get_key()});
                    put("Mama",new byte[] {pattern.BLINK.get_key(),color.WHITE.get_key(),color.BLACK.get_key()});
                    put("DEFAULT", new byte[] {pattern.BLINK.get_key(),color.WHITE.get_key(),color.BLUE.get_key()});
                }
            }
    )),
		
	Tanja(new personality(
			"Tanja",
			(short)0,
            new byte[] { color.PINK.get_key(), color.YELLOW_DIM.get_key(), color.YELLOW_BRIGHT.get_key(), color.RED_BRIGHT.get_key() },
            new byte[] { color.PINK.get_key(), color.WHITE.get_key(), color.YELLOW_BRIGHT.get_key(), color.RED_DIM.get_key() },
            new byte[] { pattern.BLINK.get_key(), pattern.BLINK.get_key(), pattern.RAINBOW.get_key(), pattern.FADE.get_key() },
            new HashMap<String, byte[]>(){
                {
                    put("Paul",new byte[] {pattern.FADE.get_key(),color.RED.get_key(),color.RED_BRIGHT.get_key()});
                    put("Mama",new byte[] {pattern.BLINK.get_key(),color.RED.get_key(),color.RED_BRIGHT.get_key()});
                    put("DEFAULT", new byte[] {pattern.BLINK.get_key(),color.WHITE.get_key(),color.BLUE.get_key()});
                }
            }
	)),
	
	Mama(new personality(
			"Mama",
			(short)0,
            new byte[] { color.BLUE_DIM.get_key(), color.ORANGE.get_key(), color.RED.get_key(), color.RED_BRIGHT.get_key() },
            new byte[] { color.WHITE.get_key(), color.ORANGE.get_key(), color.RED_DIM.get_key(), color.RED.get_key() },
            new byte[] { pattern.BLINK.get_key(), pattern.BLINK.get_key(), pattern.BLINK.get_key(), pattern.BLINK.get_key() },
            new HashMap<String, byte[]>(){
                {
                    put("Paul",new byte[] {pattern.BLINK.get_key(),color.GREEN.get_key(),color.LEMON_GREEN.get_key()});
                    put("Tanja",new byte[] {pattern.FADE.get_key(),color.GREEN.get_key(),color.LEMON_GREEN.get_key()});
                    put("DEFAULT", new byte[] {pattern.BLINK.get_key(),color.WHITE.get_key(),color.BLUE.get_key()});
                }
            }
	));

	private final personality personality_;
		
	private presetpersonalities(personality perso)
    {
		personality_ = perso;
	}

	public personality get_personality()
    {
        return personality_;
    }
}
