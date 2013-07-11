package kickflick.device;


public class color{
	private short R;
	private short G;
	private short B;
	private short Intensity_;
	
	// Color Enumeration
	public enum Color {
		RED("RED", new color(255,0,0,50)),
		GREEN("GREEN", new color(0,255,0,50)),
		BLUE("BLUE", new color(0,0,255,50)),
		YELLOW("YELLOW", new color(255,255,0,50)),
		ROSE("ROSE", new color(255,228,225,50)),
		ORANGE("ORANGE", new color(255,165,0,50)),
		WHITE("WHITE", new color(255,255,255,50));
		
		private final String colorName;
		private final color colorColor;
		
		Color(String colorName, color colorColor) {
			this.colorName = colorName;
			this.colorColor = colorColor;
		}
		
		private String get_colorName() 
			{return colorName;}

		private color get_colorColor() 
			{return colorColor;}
	}

	
	//Constructors
	public color(short r, short g, short b, short Intensity) {
		this.R = r;
		this.G = g;
		this.B = b;
		this.Intensity_ = Intensity;
	}
	
	public color(short[] color)	{
		set_Color(color);
	}
	
	public color(){
		set_Color(new short[]{0,0,0,0});
	}
	
	public color(color Color)
	{
		this.set_Color(Color.get_Color());
	}
	

	public color(int i, int j, int k, int l) {
		this.R = (short)i;
		this.G = (short)j;
		this.B = (short)k;
		this.Intensity_ = (short)l;
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
	
	public void set_Intensity (short Intensity) {
		this.Intensity_ = Intensity;
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
	
	public short get_Intensity() {
		return this.Intensity_;
	}
	
	public short[] get_Color()
	{
		short[] dings = {this.R,this.G,this.B,this.Intensity_}; 
		return dings;
	}
	
}
