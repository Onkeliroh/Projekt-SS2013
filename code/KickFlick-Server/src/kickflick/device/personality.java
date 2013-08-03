package kickflick.device;

import java.awt.Color;

public class personality{
	protected String Name_;
	protected short Id_;
	protected short State_;
	private color[] Colors_ = new color[4];
	
	
	//Constructors
	
	// default
	public personality()
	{
		this.Name_ = "undefined";
		this.Id_ = 0;
		this.State_ = 0;
		this.Colors_ = new color[] {new color(0,255,0,150),new color(0,255,0,150),new color(0,255,0,150),new color(0,255,0,150)};
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
		return this.Colors_[this.State_];
	}
	
	public color get_Color ( short state )
	{
		return this.Colors_[state];
	}
}
