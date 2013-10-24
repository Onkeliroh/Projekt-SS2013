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
            new DefaultHashMap<keys, reaction[]>(new reaction[]{
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
            },
            new reaction(color.BLUE,color.BLUE,pattern.BLINK, 1000) //standby

    )),

    Tanja(new personality(
            "Tanja",
            (short) 1,
            new DefaultHashMap<keys, reaction[]>(new reaction[]{
                new reaction(color.YELLOW_DIM,color.WHITE,pattern.BLINK),
                new reaction(color.YELLOW_BRIGHT,color.YELLOW_BRIGHT,pattern.BLINK),
                new reaction(color.RED,color.RED_BRIGHT,pattern.FADE)
            }),
        new HashMap<String, reaction>()
    {
        {
            put("Paul", new reaction(color.RED, color.RED_BRIGHT, pattern.FADE));
            put("Mama", new reaction(color.RED, color.RED_BRIGHT, pattern.BLINK));
        }
    },
        new reaction(color.LILA,color.LILA,pattern.BLINK, 1000) //standby
    )),


    Mama(new personality(
            "Mama",
            (short) 2,
            new DefaultHashMap<keys, reaction[]>(new reaction[]{
                new reaction(color.ORANGE,color.ORANGE,pattern.BLINK),
                new reaction(color.RED,color.RED_DIM,pattern.BLINK),
                new reaction(color.RED_BRIGHT,color.RED,pattern.RAINBOW)
            }),
        new HashMap<String, reaction>()
    {
        {
            put("Paul", new reaction(color.GREEN, color.ORANGE, pattern.BLINK));
            put("Mama", new reaction(color.GREEN, color.ORANGE, pattern.BLINK));
        }
    },
        new reaction(color.BLUE_DIM,color.WHITE,pattern.BLINK, 1000) //standby
    ));

    private final personality personality_;

    private presetpersonalities(personality perso) {
        personality_ = perso;
    }

    public personality get_personality() {
        return personality_;
    }
}
