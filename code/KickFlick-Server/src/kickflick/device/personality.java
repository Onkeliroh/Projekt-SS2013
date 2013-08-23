package kickflick.device;

import kickflick.device.color;
import java.util.Timer;
import java.util.TimerTask;

public class personality{
	private String Name_;
	private short Id_;
	private short State_;
	private color[] Colors_ = new color[4];
	private byte[] pattern_;
	
	private device device_;
	
	private Timer timer_ = new Timer(true); 
	
	private Timer timer_ = new Timer(true); 
	
	
	//Constructors
	
	//TODO build timer 
	//TODO come up with a idea about switching state depending on timer
	
	// default
	public personality()
	{
		this.device_ = null;
		this.Name_ = "undefined";
		this.Id_ = 0;
		this.State_ = 0;
		this.Colors_ = new color[] {new color(0,255,0,150),new color(0,255,0,150),new color(0,255,0,150),new color(0,255,0,150)};
		
		this.timer_.schedule(this.checkState(), 30000);
<<<<<<< HEAD
	}
	
	public personality(device dev, String name, short id, short state, color[] color_tmp, byte[] patterns)
	{
		this.device_ = dev;
		this.Name_ = name;
		this.Id_ = id;
		this.State_ = state;
		this.Colors_ = color_tmp;
		this.pattern_ = patterns;
=======
>>>>>>> origin/master
	}
	
	public personality(String name, short id, short state, color[] color_tmp, byte[] patterns)
	{
		this.Name_ = name;
		this.Id_ = id;
		this.State_ = state;
		this.Colors_ = color_tmp;
		this.pattern_ = patterns;
	}
	
	public personality(String name, short id, short state, color[] color_tmp)
	{
		this.Name_ = name;
		this.Id_ = id;
		this.State_ = state;
		this.Colors_ = color_tmp;
	}
	
	public personality(String name, short id, short state)
	{
		this.Name_ = name;
		this.Id_ = id;
		this.State_ = state;
	}
	
	public personality(String name, short id)
	{
		this.Name_ = name;
		this.Id_ = id;
		this.State_ = 0;
		this.Colors_ = new color[4];
	}
	
	//Setter
	
	public void set_device(device dev)
	{
		this.device_ = dev;
	}
	
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
			//TODO: Error
		}
	}
	
	//sets the color of the current state
	public void set_Color (color Color)
	{
		//this.Color_.set_Color(Color.get_Color());
		this.Colors_[this.State_] = new color(Color);
	}
	
	//sets the color of a state
	public void set_Color ( color Color, short state)
	{
		try	{
			this.Colors_[state] = Color;
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
	
	public device get_device()
	{
		return this.device_;
	}
	
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
		return this.Colors_[this.State_];
	}
	
	public color get_Color ( short state )
	{
		return this.Colors_[state];
	}
	
	public byte get_pattern ( short state )
	{
		return this.pattern_[state];
	}
	
	public byte get_pattern ()
	{
		return this.pattern_[this.State_];
	}

	public TimerTask checkState() {
		// TODO if nothing happens
<<<<<<< HEAD
		// TODO write to serial
		return null;		
	}
	
	public void restart_timer()
	{
		//stopping
		if ( this.timer_ != null)
		{
			this.timer_.cancel();
			this.timer_.purge();
		}
		
		//starting
		this.timer_.schedule(this.checkState(), 30000);
	}
=======
		return null;		
	}
>>>>>>> origin/master
}
