package kickflick.device;

import java.security.Timestamp;

//TODO write timertask, timestamp and message composer + sender

public class device {
	private personality Personality_;
	
	private byte sensor_node;
	private byte actuator_node;

    private Timestamp last_seen;
	
	//Constructors
	
	public device (personality Personality, byte sender, byte receiver)
	{
		this.Personality_ = Personality;
		this.sensor_node = sender;
		this.actuator_node = receiver;
	}
	
	public device (personality Personality)
	{
		this.Personality_ = Personality;
	}
	
	public device ()
	{
		this.Personality_ = new personality();
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

    //react function
    public void react(byte[] arg)
    {

    }
}
