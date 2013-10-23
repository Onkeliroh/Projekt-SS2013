package kickflick.device;

import kickflick.utility.color;
import kickflick.utility.pattern;

public class reaction {
    private color color1_;
    private color color2_;
    private pattern pattern_;
    private int duration_ = 1000; // in milliseconds


    public reaction (color c1, color c2, pattern p, int tmp_durration)
    {
        this.color1_ = c1;
        this.color2_ = c2;
        this.pattern_ = p;
        this.duration_ = tmp_durration;
    }

    public reaction (color c1, color c2, pattern p)
    {
        this.color1_ = c1;
        this.color2_ = c2;
        this.pattern_ = p;
    }

    public color get_color1()
    {
        return this.color1_;
    }

    public color get_color2()
    {
        return this.color2_;
    }

    public pattern get_pattern()
    {
        return this.pattern_;
    }

    public int get_durration()
    {
        return this.duration_;
    }

    public void set_color1(color c)
    {
        this.color1_ = c;
    }

    public void set_color2_(color c)
    {
        this.color2_ = c;
    }

    public void set_pattern(pattern p)
    {
        this.pattern_ = p;
    }

    public void set_duration(int t)
    {
        this.duration_ = t;
    }
}
