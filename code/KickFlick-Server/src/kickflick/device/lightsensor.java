package kickflick.device;

public class lightsensor {
	private short Id_ ;
	private personality Personality_;
	private byte State_;
	private int Intensity_;	


	//Constructors
	public lightsensor(short Id, personality Personality, byte State, int Intensity)
	{
		this.Id_ = Id;
		this.Personality_ = Personality;
		this.State_ = State;
		this.Intensity_ = Intensity;
	}
	
	public lightsensor (short Id)
	{
		this.Id_ = Id;
		this.Personality_ = new personality();
		this.State_ = 0;
		this.Intensity_ = 0;
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

	public void set_Intensity(int Intensity) {
		this.Intensity_ = Intensity;
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

	public int get_Intensity() {
		return this.Intensity_;
	}

}
