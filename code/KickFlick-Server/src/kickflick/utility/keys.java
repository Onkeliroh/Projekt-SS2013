package kickflick.utility;

import java.util.Enumeration;

//TODO update
public enum keys {
    request_cs("Request C->S",(byte)001),	
    set_sc("Set S->C",(byte)002), //Client-ID	
    return_cs("Return C->S",(byte)003),	


    report_sc("Report S->C",(byte)11),	
    reset_sc("Reset S->C",(byte)12),	
    hypernate_sc("Hypernate S->C",(byte)13),

    //Wheel events
    has_moded_cs("Has moved C->S",(byte)21),	
    speed_reached("Speed reached C->S",(byte)22),	
    direction_changed("Direction changed C->S",(byte)23),	
    distance_covered("Distance covered C->S",(byte)24),	
    status("Status C->S",(byte)25),	
    has_stopped_wheel("Has stopped C->S",(byte)26),	
    turned_left("Turned left C->S",(byte)27),	
    turned_right("Turned right C->S",(byte)28),

    //Accelerometer-Events
    has_moved("Has moved C->S",(byte)31),	
    kicked("Kicked C->S",(byte)32),	
    tipped("Tipped C->S",(byte)33),	
    rolled("Rolled C->S",(byte)34),	
    picked_up("Picked up C->S",(byte)35),	
    falled_down("Fallen down C->S",(byte)36),	
    has_stopped_accelerometer("Has stopped C->S",(byte)37),

    //LED-Chain
    on("On S->C",(byte)41),	
    fade("Fade S->C",(byte)42),	
    blink("Blink S->C",(byte)43),	
    off("Off S->C",(byte)043),	
    default_pattern_on("Oefault pattern on S->C",(byte)45),	
    default_pattern_of("Request S->C",(byte)46),	

    //Test Cases
    test_cs("Test C->S",(byte)255),	
    test_sc("Test S->C",(byte)255);
    
    private final String name_;
    private final byte key_;
    
    keys (String name, byte key){
    	this.name_ = name;
    	this.key_ = key;
    }
    
    public String get_name() { return this.name_; }
    public byte get_key() { return this.key_; }

}
