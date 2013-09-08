package kickflick.utility;

public enum reaction_keys
{
    SET_PATTERN("Set Pattern",(byte)42),
    SET_COLORS("Set Colors",(byte)43);


    private final String name;
    private final byte key;

    reaction_keys (String name, byte key)
    {
        this.name = name;
        this.key = key;
    }

    public String get_name() { return this.name;}
    public byte get_key() { return this.key; }
}
