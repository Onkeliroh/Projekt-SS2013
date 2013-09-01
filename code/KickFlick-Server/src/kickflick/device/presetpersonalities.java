package kickflick.device;

public enum presetpersonalities
{
    //TODO write correct color and pattern values
	Paul("Paul",new personality(
			"Paul",
			(short)0,
			new byte[] { 1, 1, 1, 1 },    //Color 1
            new byte[] { 1, 1, 1, 1 },    //Color 2
            new byte[] { 1, 1, 1, 1 }     //Pattern
			)),
	
	Tanja("Tanja",new personality(
			"Tanja",
			(short)0,
            new byte[] { 1, 1, 1, 1 },
            new byte[] { 1, 1, 1, 1 },
            new byte[] { 1, 1, 1, 1 }
			)),
	
	Mama("Mama",new personality(
			"Mama",
			(short)0,
            new byte[] { 1, 1, 1, 1 },
            new byte[] { 1, 1, 1, 1 },
            new byte[] { 1, 1, 1, 1 }
			));

	private final String name_;
	private final personality personality_;
		
	private presetpersonalities(String name, personality perso)
    {
		name_ = name;
		personality_ = perso;
	}

    public personality get_personality()
    {
        return personality_;
    }
}
