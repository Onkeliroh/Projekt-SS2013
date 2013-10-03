package kickflick.utility;

// Color Enumeration
// compare with Leds.cpp for more color values or to change them
public enum color
{
    WHITE("White", (byte) 0),
    RED_BRIGHT("Red (bright)", (byte) 1),
    RED("Red", (byte) 2),
    RED_DIM("Red (dim)", (byte) 3),
    GREEN_BRIGHT("Green (bright)", (byte) 4),
    GREEN("Green", (byte) 5),
    GREEN_DIM("Green (dim)", (byte) 6),
    BLUE_BRIGHT("Blue (bright)", (byte) 7),
    BLUE("Blue", (byte) 8),
    BLUE_DIM("Blue (dim)", (byte) 9),
    YELLOW_BRIGHT("Yellow (bright)", (byte) 10),
    YELLOW("Yellow", (byte) 11),
    YELLOW_DIM("Yellow (dim)", (byte) 12),
    LILA("Lila", (byte) 13),
    ORANGE("Orange", (byte) 14),
    TURQUOISE("Turquoise", (byte) 15),
    BLACK("Black", (byte) 16);

    private final String colorName;
    private final byte color_key;

    color(String name, byte b) {
        this.colorName = name;
        this.color_key = b;
    }

    public String get_name() {
        return colorName;
    }

    public byte get_key() {
        return color_key;
    }
}
