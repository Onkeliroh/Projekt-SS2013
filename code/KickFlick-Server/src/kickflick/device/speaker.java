package kickflick.device;

public class speaker {
	private short Id_ ;
	private personality Personality_;
	private byte State_;
	private int Pitch_;
	private short Duration_;
	
	//Constructors
	public speaker (short Id, 
					personality Personality, 
					byte State,
					int Pitch,
					short Duration)
	{
		this.Id_ = Id;
		this.Personality_ = Personality;
		this.State_ = State;
		this.Pitch_ = Pitch;
		this.Duration_ = Duration;
	}
	
	public speaker (short Id)
	{
		this.Id_ = Id;
		personality defaultPersonality = new personality();
		this.Personality_ = defaultPersonality;
		this.State_ = 00000000;
		this.Pitch_ = 0;
		this.Duration_ = 0;
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

	public void set_Pitch (int Pitch) {
		this.Pitch_ = Pitch;
	}
	
	public void set_Duration (short Duration) {
		this.Duration_ = Duration;
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

	public int get_Pitch() {
		return this.Pitch_;
	}
	
	public short get_Duration() {
		return this.Duration_;
	}
}
