package kickflick.device;

public class color{
	private short R;
	private short G;
	private short B;
	
	
	//Constructors
	public color(short r, short g, short b) {
		this.R = r;
		this.G = g;
		this.B = b;
	}
	
	public color(short[] color)	{
		set_Color(color);
	}
	
	public color(){
		set_Color(new short[]{0,0,0});
	}
	
	public color(color Color)
	{
		this.set_Color(Color.get_Color());
	}
	
	//Methods - Setter	
	public void set_R(short r_tmp)
	{
		this.R = r_tmp;
	}
	
	public void set_G(short g_tmp)
	{
		this.G = g_tmp;
	}
	
	public void set_B(short b_tmp)
	{
		this.B = b_tmp;
	}
	
	public void set_Color (short[] color_tmp)
	{
		this.R = color_tmp[0];
		this.G = color_tmp[1];
		this.B = color_tmp[2];
	}
	
	
	//Methods - Getter
	public short get_R()
	{
		return this.R;
	}
	
	public short get_G()
	{
		return this.G;
	}
	
	public short get_B()
	{
		return this.B;
	}
	
	public short[] get_Color()
	{
		short[] dings = {this.R,this.G,this.B}; 
		return dings;
	}
	
}
