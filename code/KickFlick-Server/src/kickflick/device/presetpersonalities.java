package kickflick.device;

import kickflick.utility.DefaultHashMap;
import kickflick.utility.color;
import kickflick.utility.keys;
import kickflick.utility.pattern;

import java.util.HashMap;

public enum presetpersonalities
{
    //TODO check for durrations
    Paul(new personality(
            "Paul",
            (short) 0,
//            new byte[]{color.BLUE.get_key(), color.RED.get_key(), color.GREEN.get_key(), color.ORANGE.get_key()},    //Color 1
//            new byte[]{color.BLUE.get_key(), color.RED.get_key(), color.GREEN.get_key(), color.ORANGE.get_key()},    //Color 2
//            new byte[]{pattern.BLINK.get_key(), pattern.BLINK.get_key(), pattern.BLINK.get_key(), pattern.RAINBOW.get_key()},     //Pattern
            new DefaultHashMap<keys, reaction[]>(new reaction[]{
                        new reaction(color.BLUE,color.BLUE,pattern.BLINK),
                        new reaction(color.RED,color.RED,pattern.BLINK),
                        new reaction(color.GREEN,color.GREEN,pattern.BLINK),
                        new reaction(color.ORANGE,color.ORANGE,pattern.RAINBOW)
                }),
            new HashMap<String, reaction>()
            {
                {
                    put("Tanja", new reaction(color.RED, color.BLUE, pattern.BLINK));
                    put("Mama", new reaction(color.WHITE, color.BLACK, pattern.BLINK));
                }
            }
    ));

//    Tanja(new personality(
//            "Tanja",
//            (short) 0,
//            new byte[]{color.LILA.get_key(), color.YELLOW_DIM.get_key(), color.YELLOW_BRIGHT.get_key(), color.RED_BRIGHT.get_key()},
//            new byte[]{color.LILA.get_key(), color.WHITE.get_key(), color.YELLOW_BRIGHT.get_key(), color.RED_DIM.get_key()},
//            new byte[]{pattern.BLINK.get_key(), pattern.BLINK.get_key(), pattern.RAINBOW.get_key(), pattern.FADE.get_key()},
//            new HashMap<String, byte[]>()
//            {
//                {
//                    put("Paul", new byte[]{pattern.FADE.get_key(), color.RED.get_key(), color.RED_BRIGHT.get_key()});
//                    put("Mama", new byte[]{pattern.BLINK.get_key(), color.RED.get_key(), color.RED_BRIGHT.get_key()});
//                }
//            }
//    )),
//
//    Mama(new personality(
//            "Mama",
//            (short) 0,
//            new byte[]{color.BLUE_DIM.get_key(), color.ORANGE.get_key(), color.RED.get_key(), color.RED_BRIGHT.get_key()},
//            new byte[]{color.WHITE.get_key(), color.ORANGE.get_key(), color.RED_DIM.get_key(), color.RED.get_key()},
//            new byte[]{pattern.BLINK.get_key(), pattern.BLINK.get_key(), pattern.BLINK.get_key(), pattern.BLINK.get_key()},
//            new HashMap<String, byte[]>()
//            {
//                {
//                    put("Paul", new byte[]{pattern.BLINK.get_key(), color.GREEN.get_key(), color.ORANGE.get_key()});
//                    put("Tanja", new byte[]{pattern.FADE.get_key(), color.GREEN.get_key(), color.ORANGE.get_key()});
//                }
//            }
//    ));

    private final personality personality_;

    private presetpersonalities(personality perso) {
        personality_ = perso;
    }

    public personality get_personality() {
        return personality_;
    }
}
