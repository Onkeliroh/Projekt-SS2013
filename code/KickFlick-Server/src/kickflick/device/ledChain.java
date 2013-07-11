package kickflick.device;

public class ledChain {
	private short Id_;
	private personality Personality_;
	private byte State_;
	private color Color_;
	private short Frequency_;
	
	// Constructors
	public ledChain(short Id,
					personality Personality,
					byte State,
					color Color,
					short Frequency)
	{
		this.Id_ = Id;
		//TODO defaultPersonality value?
		personality defaultPersonality = null;
		this.Personality_ = defaultPersonality;
		this.State_ = 0;
		this.Color_.set_Color(null);
		this.Frequency_ = 0;  					// means: static light emission, no changes 
	}

	// Setter
	public void set_Id(short Id) {
		this.Id_ = Id;
	}

	public void set_Personality(personality Personality) {
		this.Personality_ = Personality;
	}

	public void set_State(byte State) {
		this.State_ = State;
	}

	public void set_Color(color Color) {
		this.Color_ = Color;
	}

	public void set_Frequency(short frequency) {
		this.Frequency_ = frequency;
	}

	// Getter
	public short get_Id() {
		return this.Id_;
	}

	public personality get_Personality() {
		return this.Personality_;
	}

	public byte get_State() {
		return this.State_;
	}

	public color get_Color() {
		return this.Color_;
	}

	public short get_Frequency() {
		return this.Frequency_;
	}
}

