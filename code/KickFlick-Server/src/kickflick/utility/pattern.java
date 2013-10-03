package kickflick.utility;

public enum pattern
{
    BLINK("Blink", (byte) 0),
    FADE("Fade", (byte) 1),
    RAINBOW("Rainbow", (byte) 2),
    ON("On", (byte) 3),
    OFF("Off", (byte) 4);

    private final String name;
    private final byte key;

    pattern(String name, byte b) {
        this.name = name;
        this.key = b;
    }

    public String get_name() {
        return this.name;
    }

    public byte get_key() {
        return this.key;
    }
}
