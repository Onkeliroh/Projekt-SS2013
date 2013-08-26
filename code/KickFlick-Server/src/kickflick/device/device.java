package kickflick.device;

public class device {
	private personality Personality_;
	private byte State_;
	
	private byte sensor_node;
	private byte actuator_node;
	
	//Constructors
	
	public device (personality Personality, byte state, byte sender, byte receiver)
	{
		this.Personality_ = Personality;
		this.State_ = state;
		this.sensor_node = sender;
		this.actuator_node = receiver;
		
		this.Personality_.set_device(this);
	}
	
	public device (personality Personality, byte State)
	{
		this.Personality_ = Personality;
		this.State_ = State;
		
		this.Personality_.set_device(this);
	}
	
	public device (byte State)
	{
		this.State_ = State;
		this.Personality_ = new personality();
		
		this.Personality_.set_device(this);
	}
	
	public device ()
	{
		this.State_ = 00000000;
		this.Personality_ = new personality();
		
		this.Personality_.set_device(this);
	}

	//Setter
	public void set_Personality (personality Personality)
	{
		this.Personality_ = Personality;
		
		this.Personality_.set_device(this);
	}
	
	public void set_State (byte State)
	{
		this.State_ = State;
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
	
	public byte get_State ()
	{
		return this.State_;
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
