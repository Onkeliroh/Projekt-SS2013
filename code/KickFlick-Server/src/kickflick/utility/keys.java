package kickflick.utility;

public enum keys {
    request_cs("Request C->S",(byte)001),	
    set_sc("Set S->C",(byte)002), //Client-ID	
    return_cs("Return C->S",(byte)003),	


    report_sc("Report S->C",(byte)011),	
    reset_sc("Reset S->C",(byte)012),	
    hypernate_sc("Hypernate S->C",(byte)013),

    //Wheel events
    has_moded_cs("Has moved C->S",(byte)021),	
    speed_reached("Speed reached C->S",(byte)022),	
    direction_changed("Direction changed C->S",(byte)023),	
    distance_covered("Distance covered C->S",(byte)024),	
    status("Status C->S",(byte)025),	
    has_stopped_wheel("Has stopped C->S",(byte)026),	
    turned_left("Turned left C->S",(byte)027),	
    turned_right("Turned right C->S",(byte)28), //TODO check why leading 0 causes failure 

    //Accelerometer-Events
    has_moved("Has moved C->S",(byte)031),	
    kicked("Kicked C->S",(byte)032),	
    tipped("Tipped C->S",(byte)033),	
    rolled("Rolled C->S",(byte)034),	
    picked_up("Picked up C->S",(byte)035),	
    falled_down("Fallen down C->S",(byte)036),	
    has_stopped_accelerometer("Has stopped C->S",(byte)037),

    //LED-Chain
    on("On S->C",(byte)041),	
    fade("Fade S->C",(byte)042),	
    blink("Blink S->C",(byte)043),	
    off("Off S->C",(byte)043),	
    default_pattern_on("Oefault pattern on S->C",(byte)045),	
    default_pattern_of("Request S->C",(byte)046),	

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
