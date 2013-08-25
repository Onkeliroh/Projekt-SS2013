package kickflick.device;

public enum presetpersonalities {
	//TODO add patterns
	Paul("Paul",new personality(
			"Paul",
			(short)0,
			new color[] { 
					new color(0,0,255,255),
					new color(0,0,255,255),
					new color(100,0,255,255),
					new color(0,200,230,255)
					}
			)),
	
	Tanja("Tanja",new personality(
			"Tanja",
			(short)0,
			new color[] { 
					new color(255,204,221,255),
					new color(255,238,100,255),
					new color(100,0,255,255),
					new color(255,0,0,255)
					}
			)),
	
	Mama("Mama",new personality(
			"Mama",
			(short)0,
			new color[] { 
					new color(255,255,255,255),
					new color(255,187,0,255),
					new color(255,0,0,255),
					new color(255,0,0,255)
					}
			));

	private final String name_;
	private final personality personality_;
		
	private presetpersonalities(String name, personality perso) {
		this.name_ = name;
		this.personality_ = perso;
	}
}
