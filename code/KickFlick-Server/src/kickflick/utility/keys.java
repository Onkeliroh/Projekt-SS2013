package kickflick.utility;

public enum keys
{
    //Wheel events
//    has_moded_cs("Has moved",(byte)21),
//    speed_reached("Speed reached",(byte)22),
//    direction_changed("Direction changed",(byte)23),
//    distance_covered("Distance covered",(byte)24),
//    has_stopped_wheel("Has stopped",(byte)26),
//    turned_left("Turned left",(byte)27),
//    turned_right("Turned right",(byte)28),

    //Accelerometer-Events
    has_moved("Has moved", (byte) 31),
    kicked("Kicked", (byte) 32),
    tipped("Tipped", (byte) 33),
    rolledx("Rolled X", (byte) 34),
    //    falled_down("Fallen down",(byte)36),
//    has_stopped_accelerometer("Has stopped",(byte)37),
    rolledy("Rolled Y", (byte) 38),
    rolledz("Rolled Z", (byte) 39);

    private final String name_;
    private final byte key_;

    keys(String name, byte key) {
        this.name_ = name;
        this.key_ = key;
    }

    public String get_name() {
        return this.name_;
    }

    public byte get_key() {
        return this.key_;
    }


    public boolean contains_key(byte b) {
        for (keys k : keys.values())
            if (k.key_ == b)
                return true;
        return false;
    }

    public keys get_key(String str) {
        for (keys k : keys.values())
            if (k.get_name().equals(str))
                return k;
        return null;
    }

    public keys get_key(byte b) {
        for (keys k : keys.values())
            if (k.get_key() == b)
                return k;
        return null;
    }

}