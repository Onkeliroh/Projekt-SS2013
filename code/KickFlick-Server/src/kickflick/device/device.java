package kickflick.device;

import kickflick.utility.keys;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class device implements Serializable
{

    private final long NEIGHBOR_MOVED_AWAY = 30000; //when after 0.5 minutes no responce from neighbor, the device is declared single and without neighbor

    private personality Personality_;

    private byte sensor_node; //sensor panstamp address
    private byte actuator_node; //actuator panstamp address

    private Map<kickflick.utility.keys, Boolean> trigger = new HashMap<keys, Boolean>(); //stores all keys and wether it should be reacted to

    private Timestamp timestamp;        //timestamp of last action send by device (e.g. kicked)
    private Timestamp timestamp_last_heard_of; //timestamp of last received message

    private boolean battery_low = false;

    private device neighbor = null;
    private Timestamp met_neighbor = null;

    //Constructors
    public device(personality Personality, byte sender, byte receiver, Map<keys, Boolean> map) {
        this.trigger = map;
        this.Personality_ = Personality;
        this.sensor_node = sender;
        this.actuator_node = receiver;

        this.timestamp = new Timestamp(new Date().getTime());
        this.timestamp_last_heard_of = this.timestamp;
    }

    public device(personality Personality, byte sender, byte receiver) {
        this.Personality_ = Personality;
        this.sensor_node = sender;
        this.actuator_node = receiver;

        this.timestamp = new Timestamp(new Date().getTime());
        this.timestamp_last_heard_of = this.timestamp;

        create_trigger();
    }

    public device(personality Personality) {
        this.Personality_ = Personality;
        this.timestamp = new Timestamp(new Date().getTime());
        this.timestamp_last_heard_of = this.timestamp;

        create_trigger();
    }

    public device(device dev) {
        this.set_Personality(dev.get_Personality());
        this.sensor_node = dev.get_sensor_node();
        this.actuator_node = dev.get_actuator_node();
        this.trigger = dev.get_trigger_map();
        this.timestamp = dev.timestamp;
        this.timestamp_last_heard_of = dev.timestamp_last_heard_of;
        this.battery_low = dev.battery_low;
    }

    public device() {
        this.Personality_ = new personality();
        this.timestamp = new Timestamp(new Date().getTime());
        this.timestamp_last_heard_of = this.timestamp;

        create_trigger();
    }

    //Setter
    public void set_Personality(personality Personality) {
        this.Personality_ = Personality;
    }

    public void set_sensor_node(byte sensor) {
        this.sensor_node = sensor;
    }

    public void set_actuator_node(byte actuator) {
        this.actuator_node = actuator;
    }

    public void set_trigger_map(Map<keys, Boolean> tmp) {
        this.trigger = tmp;
    }

    public void set_neighbor(device d) {
        this.neighbor = d;
        this.met_neighbor = new Timestamp(new Date().getTime());
    }

    public void reset_neighbor() {
        this.neighbor = null;
        this.met_neighbor = null;
    }

    //Getter
    public personality get_Personality() {
        return this.Personality_;
    }

    public byte get_sensor_node() {
        return this.sensor_node;
    }

    public byte get_actuator_node() {
        return this.actuator_node;
    }

    public java.sql.Timestamp get_timestamp() {
        return this.timestamp;
    }

    public Timestamp get_timestamp_last_heard_of() {
        return this.timestamp_last_heard_of;
    }

    public Map<keys, Boolean> get_trigger_map() {
        return this.trigger;
    }

    //creates a new map woth all key values set to true, means device reacts to every known key
    private void create_trigger() {
        for (int i = 0; i < keys.values().length; ++i)
        {
            this.trigger.put(keys.values()[i], true);
        }
    }

    public final boolean is_battery_low() {
        return this.battery_low;
    }

    public final String get_battery_state() {
        if (this.battery_low)
            return "LOW";
        return "OK";
    }

    //setter for last action send by sensor node
    public void set_new_timestamp() {
        this.timestamp = new Timestamp(new Date().getTime());
    }

    //setter for last received message by sensor node
    public void set_new_timestamp_last_heard_of() {
        this.timestamp_last_heard_of = new Timestamp(new Date().getTime());
    }

    public device get_neightbor() {
        return this.neighbor;
    }

    //checks whether a neighbor exsists and if the time run out, if so the neighbor will be reseted
    //a appropriate answer will be given
    public boolean has_neighbor() {
        if (this.met_neighbor != null)
            if (new Timestamp(new Date().getTime()).getTime() - this.met_neighbor.getTime() <= NEIGHBOR_MOVED_AWAY)
                return true;
            else
            {
                this.reset_neighbor();
                return false;
            }
        return false;
    }

    public void toggle_battery_low() {
        this.battery_low = !this.battery_low;
    }
}
