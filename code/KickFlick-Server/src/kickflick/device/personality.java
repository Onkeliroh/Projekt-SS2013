package kickflick.device;

import kickflick.device.color;
import java.util.Timer;
import java.util.TimerTask;

public class personality{
	private String Name_;
	private short State_ = 0;
    public final int state_count = 4;
	private byte[] Color1_ = new byte[4];
    private byte[] Color2_ = new byte[4];
	private byte[] pattern_ = new byte[4]; //TODO should be the subkey

    private byte[][] actio_reactio = new byte[4][2];

	//Constructors

	// default
	public personality()
	{
        this.Name_ = presetpersonalities.Paul.get_personality().Name_;
        this.State_ = presetpersonalities.Paul.get_personality().State_;
        this.Color1_ = presetpersonalities.Paul.get_personality().Color1_;
        this.Color2_ = presetpersonalities.Paul.get_personality().Color2_;
        this.pattern_ = presetpersonalities.Paul.get_personality().pattern_;
	}
	
	public personality(String name, short state, byte[] color1_tmp, byte[] color2_tmp, byte[] patterns)
	{
		this.Name_ = name;
		this.State_ = state;
		this.Color1_ = color1_tmp;
        this.Color2_ = color2_tmp;
		this.pattern_ = patterns;
	}
	
	public personality(String name, short state, byte[] color1_tmp, byte[] color2_tmp)
	{
		this.Name_ = name;
		this.State_ = state;
		this.Color1_ = color1_tmp;
        this.Color2_ = color2_tmp;
	}
	
	public personality(String name, short state)
	{
		this.Name_ = name;
		this.State_ = state;
	}
	
	public personality(String name)
	{
		this.Name_ = name;
	}

    public personality ( personality pers )
    {
        this.Name_ = pers.Name_;
        this.State_ = pers.State_;
        this.Color1_ = pers.Color1_;
        this.Color2_ = pers.Color2_;
        this.pattern_ = pers.pattern_;
    }
	
	//Setter
	
	public void set_Name(String name)
	{
		this.Name_ = name;
	}
	
	public void set_State (short state)
	{
		if (state < 5 && state >= 0)
			this.State_ = state;
		else
            System.err.println("Personality: State is out of range.");
	}
	
	//sets the color of the current state
	public void set_Color1 (byte Color)
	{
		//this.Color_.set_Color(Color.get_Color());
		this.Color1_[this.State_] = Color;
	}

    public void set_Color2 (byte Color)
    {
        //this.Color_.set_Color(Color.get_Color());
        this.Color2_[this.State_] = Color;
    }
	
	//sets the color of a state
	public void set_Color1 ( byte Color, short state)
	{
		try	{
			this.Color1_[state] = Color;
		}
		finally	{
			System.err.println("Error while setting Color. Maybe wrong state.");
		}
	}

    //sets the color of a state
    public void set_Color2 ( byte Color, short state)
    {
        try	{
            this.Color2_[state] = Color;
        }
        finally	{
            System.err.println("Error while setting Color. Maybe wrong state.");
        }
    }
	
	public void set_pattern(byte pattern)
	{
		this.pattern_[this.State_] = pattern;
	}
	
	public void set_pattern( byte pattern, short state )
	{
		this.pattern_[state] = pattern;
	}
	
	public void set_pattern (byte[] pattern)
	{
		this.pattern_ = pattern;
	}
	
	//Getter
	
	public String get_Name ()
	{
		return this.Name_;
	}
	
	public short get_State()
	{
		return this.State_;
	}
	
	public byte get_Color1 ()
	{
		return this.Color1_[this.State_];
	}

    public byte get_Color2 ()
    {
        return this.Color2_[this.State_];
    }
	
	public byte get_Color1 ( short state )
	{
		return this.Color1_[state];
	}

    public byte get_Color2 ( short state )
    {
        return this.Color2_[state];
    }

	public byte get_pattern ( short state )
	{
		return this.pattern_[state];
	}
	
	public byte get_pattern ()
	{
		return this.pattern_[this.State_];
	}

    public String get_state_name()
    {
        return this.get_state_name(this.State_);
    }

    public String get_state_name(short state)
    {
        switch (state)
        {
            case 0: return "Standbye";
            case 1: return "First contact";
            case 2: return "Playing ";
            case 3: return "Playing (hard)";
            default: return "unknown";
        }
    }


    public void inc_state()
    {
        this.set_State(this.State_++);
    }

    public void dec_state()
    {
        this.set_State(this.State_--);
    }

}
