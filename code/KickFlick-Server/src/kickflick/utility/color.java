package kickflick.utility;

import java.util.Enumeration;

// Color Enumeration
// compare with Leds.cpp for more color values or to change them
public enum color {
    GREEN("GREEN", (byte)0),
    LEMON_GREEN("Lemon Green", (byte)1),
    RED_DIM("Red (dim)", (byte)2),
    RED("Red", (byte) 3),
    RED_BRIGHT("Red (bright)", (byte) 4),
    BLUE_DIM("Blue (dim)", (byte) 5),
    BLUE("Blue", (byte) 6),
    BLUE_BRIGHT("Blue (bright)", (byte) 7),
    YELLOW_DIM("Yellow (dim)", (byte) 8),
    YELLOW("Yellow", (byte) 9),
    YELLOW_BRIGHT("Yellow (bright)", (byte) 10),
    PINK("PINK", (byte)11),          //TODO change to real value
    ORANGE("ORANGE", (byte)12),
    WHITE("WHITE", (byte)13),
    FUCHSIA("FUCHSIA", (byte)14),
    TURQUOISE("TURQUOISE", (byte)15),
    BLACK("Black",(byte)16);

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
