package kickflick.device;

public class device {
	protected short Id_ ;
	private personality Personality_;
	private byte State_;
	
	private Byte sender_adr_;
	private Byte receiver_adr_;
	
	//Constructors
	
	public device ( short Id, personality Personality, Byte state, Byte sender, Byte receiver)
	{
		this.Id_ = Id;
		this.Personality_ = Personality;
		this.State_ = state;
		this.sender_adr_ = sender;
		this.receiver_adr_ = receiver;
		
		this.Personality_.set_device(this);
	}
	
	public device (short Id, personality Personality, byte State)
	{
		this.Id_ = Id;
		this.Personality_ = Personality;
		this.State_ = State;
		
		this.Personality_.set_device(this);
	}
	
	public device ( short id, byte State)
	{
		this.Id_ = id;
		this.State_ = State;
		this.Personality_ = new personality();
		
		this.Personality_.set_device(this);
	}
	
	public device (short Id)
	{
		this.Id_ = Id;
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
	
	//Getter
	public short get_Id ()
	{
		return this.Id_;
	}
	
	public personality get_Personality ()
	{
		return this.Personality_;
	}
	
	public byte get_State ()
	{
		return this.State_;
	}
}
