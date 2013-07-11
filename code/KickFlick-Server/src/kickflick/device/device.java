package kickflick.device;

public class device {
	protected short Id_ ;
	protected personality Personality_;
	protected byte State_;
	
	//Constructors
	public device (short Id, personality Personality, byte State)
	{
		this.Id_ = Id;
		this.Personality_ = Personality;
		this.State_ = State;
	}
	
	public device (short Id)
	{
		this.Id_ = Id;
		this.State_ = 00000000;
	}

	//Setter
	public void set_Personality (personality Personality)
	{
		this.Personality_ = Personality;
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
