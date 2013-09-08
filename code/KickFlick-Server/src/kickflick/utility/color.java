package kickflick.utility;

import java.util.Enumeration;

// Color Enumeration
public enum color {
    RED("RED", (byte)0),
    GREEN("GREEN", (byte)1),
    BLUE("BLUE", (byte)2),
    YELLOW("YELLOW", (byte)3),
    PINK("PINK", (byte)4),
    ORANGE("ORANGE", (byte)5),
    WHITE("WHITE", (byte)6),
    FUCHSIA("FUCHSIA", (byte)7),
    TURQUOISE("TURQUOISE", (byte)8);

    private final String colorName;
    private final byte color_key;

    color (String name, byte b)
    {
        this.colorName = name;
        this.color_key = b;
    }

    public String get_name()
        {return colorName;}

    public byte get_key()
        {return color_key;}
}
