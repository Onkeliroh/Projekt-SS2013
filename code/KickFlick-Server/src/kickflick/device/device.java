package kickflick.device;

import java.sql.Timestamp;
import java.util.*;
import java.util.Map;

import kickflick.utility.keys;

//TODO write timer

public class device {
	private personality Personality_;
	
	private byte sensor_node;
	private byte actuator_node;

    private Map<kickflick.utility.keys,Boolean> trigger = new HashMap<keys,Boolean>();

    private Timestamp timestamp;
	//Constructors

    public device (personality Personality, byte sender, byte receiver, Map<keys,Boolean> map)
    {
        this.trigger = map;
        this.Personality_ = Personality;
        this.sensor_node = sender;
        this.actuator_node = receiver;

        this.timestamp = new Timestamp(new Date().getTime()); //TODO decide wether new timestamp on gui change or not
    }

	public device (personality Personality, byte sender, byte receiver)
	{
		this.Personality_ = Personality;
		this.sensor_node = sender;
		this.actuator_node = receiver;

        this.timestamp = new Timestamp(new Date().getTime());

        create_trigger();
	}
	
	public device (personality Personality)
	{
		this.Personality_ = Personality;
        this.timestamp = new Timestamp(new Date().getTime());

        create_trigger();
	}

    public device (device dev)
    {
        this.set_Personality(dev.get_Personality());
        this.sensor_node = dev.get_sensor_node();
        this.actuator_node = dev.get_actuator_node();
        this.trigger = dev.get_trigger_map();
    }
	
	public device ()
	{
		this.Personality_ = new personality();
        this.timestamp = new Timestamp(new Date().getTime());

        create_trigger();
	}

	//Setter
	public void set_Personality (personality Personality)
	{
		this.Personality_ = Personality;
	}
	
	public void set_sensor_node(byte sensor)
	{
		this.sensor_node = sensor;
	}
	
	public void set_actuator_node(byte actuator)
	{
		this.actuator_node = actuator;
	}

    public void set_trigger_map ( Map<keys,Boolean> tmp)
    {
        this.trigger = tmp;
    }
	
	//Getter
	
	public personality get_Personality ()
	{
		return this.Personality_;
	}
	
	public byte get_sensor_node()
	{
		return this.sensor_node;
	}
	
	public byte get_actuator_node()
	{
		return this.actuator_node;
	}

    public String get_timestamp()
    {
        //TODO reduce time stamp to minutes and seconds
        return this.timestamp.toString();
    }

    public Map<keys,Boolean> get_trigger_map()
    {
        return this.trigger;
    }

    private void create_trigger()
    {
        for ( int i = 0; i < keys.values().length; ++i)
        {
            this.trigger.put(keys.values()[i],true);
        }
    }

    public void set_new_timestamp()
    {
        this.timestamp = new Timestamp(new Date().getTime());
    }
}
