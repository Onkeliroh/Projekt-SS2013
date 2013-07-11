package kickflick.device;

public class personality{
	protected String Name_;
	protected short Id_;
	protected short State_;
	private color Color_;
	private short FrequencyLED_;
	private short FrequencySpeaker_;
	private short FrequencyVibrator_;
	private short LEDDelay_;
	
	//Constructor
	
	public personality()
	{
		
	}
	
	public personality(String name, short id, short state, color color_tmp, short fled, short fsp, short fvib, short ledd)
	{
		this.Name_ = name;
		this.Id_ = id;
		this.State_ = state;
		this.Color_ = color_tmp;
		this.FrequencyLED_ = fled;
		this.FrequencySpeaker_ = fsp;
		this.FrequencyVibrator_ = fvib;
		this.LEDDelay_ = ledd;
	}
	
	public personality(String name, short id)
	{
		this.Name_ = name;
		this.Id_ = id;
		this.State_ = 0;
		this.Color_ = new color();
		this.FrequencyLED_ = 1;
		this.FrequencySpeaker_ = 1;
		this.FrequencyVibrator_ = 1;
		this.LEDDelay_ = 0;
	}
	
	//Setter
	public void set_Name(String name)
	{
		this.Name_ = name;
	}
	
	public void set_Id (short Id)
	{
		this.Id_ = Id;
	}
	
	public void set_State (short state)
	{
		if (state < 5 && state >= 0)
			this.State_ = state;
		else
		{
			//ToDo: Error
		}
	}
	
	public void set_Color (color Color)
	{
		//this.Color_.set_Color(Color.get_Color());
		this.Color_ = new color(Color);
	}
	
	public void set_FrequencyLED (short fled)
	{
		this.FrequencyLED_ = fled;
	}
	
	public void set_FrequencySpeaker (short fsp)
	{
		this.FrequencySpeaker_ = fsp;
	}
	
	public void set_FrequencyVibrator (short fvib)
	{
		this.FrequencyVibrator_ = fvib;
	}
	
	//Getter
	
	public String get_Name ()
	{
		return this.Name_;
	}
	
	public short get_Id ()
	{
		return this.Id_;
	}
	
	public short get_State()
	{
		return this.State_;
	}
	
	public color get_Color ()
	{
		return this.Color_;
	}
	
	public short get_FrequencyLED ()
	{
		return this.FrequencyLED_;
	}
	
	public short get_FrequencySpeaker ()
	{
		return this.FrequencySpeaker_;
	}
	
	public short get_FrequencyVibrator ()
	{
		return this.FrequencyVibrator_;
	}
	
	public short get_LEDDelay ()
	{
		return this.LEDDelay_;
	}
}
