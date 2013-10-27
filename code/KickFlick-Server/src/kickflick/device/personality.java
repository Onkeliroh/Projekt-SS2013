package kickflick.device;

import kickflick.utility.DefaultHashMap;
import kickflick.utility.color;
import kickflick.utility.keys;
import kickflick.utility.pattern;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class personality implements Serializable
{
    private String Name_;
    private short State_ = 0;
    public final int state_count = 4;
    private reaction standby = null;
    private keys current_reaction_key = null;
    private Map<keys, reaction[]> Reactions_;
    private Map<String, reaction> neighbours_ = new HashMap<String, reaction>();

    private String[] state_names = {"Out of Range","Standby", "First contact", "Playing", "Playing (hard)"};

    //Constructors

    // default
    public personality() {
        create_reaction_map();

        this.Name_ = presetpersonalities.Paul.get_personality().Name_;
        this.State_ = presetpersonalities.Paul.get_personality().State_;
        this.Reactions_ = presetpersonalities.Paul.get_personality().Reactions_;
        this.neighbours_ = presetpersonalities.Paul.get_personality().neighbours_;
        this.standby = presetpersonalities.Paul.get_personality().standby;
//        this.current_reaction = this.standby;
    }

    public personality(String name, short state, DefaultHashMap<keys, reaction[]> tmp_reactions, HashMap<String, reaction> neighbours, reaction stand) {
        create_reaction_map();

        this.Name_ = name;
        this.State_ = state;
        this.Reactions_ = tmp_reactions;
        this.neighbours_ = neighbours;
        this.standby = stand;
//        this.current_reaction = this.standby;
    }

    public personality(String name, short state, DefaultHashMap<keys, reaction[]> tmp_reactions, HashMap<String, reaction> neighbours) {
        create_reaction_map();

        this.Name_ = name;
        this.State_ = state;
        this.Reactions_ = tmp_reactions;
        this.neighbours_ = neighbours;
        this.standby = presetpersonalities.Paul.get_personality().standby;
//        this.current_reaction = this.standby;
    }

    public personality(personality pers) {
        this.Name_ = pers.Name_;
        this.State_ = pers.State_;
        this.Reactions_ = pers.Reactions_;
        this.standby = pers.standby;
        this.current_reaction_key = pers.current_reaction_key;
    }


    //Setter

    public void set_Name(String name) {
        this.Name_ = name;
    }

    public void set_State(short state) {
        if (state < state_count && state >= -1)
        {
            this.State_ = state;
//            System.out.println("New state :" + this.get_Name() + "\t" + this.State_);
        }
    }

    public void set_standby (reaction tmp)
    {
        this.standby = tmp;
    }

    //requires to set the state first
    public void set_current_reaction(keys k)
    {
        System.out.println("Setting current reaction");
        this.current_reaction_key = k;
    }

//    public void set_current_reaction(reaction r)
//    {
//        this.current_reaction = r;
//    }

    //Getter

    public String get_Name() {
        return this.Name_;
    }

    public short get_State() {
        return this.State_;
    }

    public String get_state_name() {
        return this.state_names[this.State_+1];
    }

    public String get_state_name(short state) {
        if (check_state(state))
            return this.state_names[state-1];
        return "unknown";
    }

    public String[] get_state_names()
    {
        return this.state_names;
    }

    public Map<String, reaction> get_Neighbours() {
        return this.neighbours_;
    }

    public reaction get_reaction(keys key)
    {
        return get_reaction(key, this.State_);
    }

    public reaction get_reaction(keys key, int state)
    {
        if ( check_state(state) )
        {
            if (state > 0)
            {
                for (Map.Entry e : this.Reactions_.entrySet())
                {
                    if ( e.getKey().equals(key) )
                        return ((reaction[])e.getValue())[state -1];
                }
                return this.Reactions_.get(key)[state -1];
            }
            else if (state == 0)
            {
                return this.standby;
            }
        }
        System.err.println("Personality get_reaction of " + this.Name_ + " wrong state");
        return null;
    }
    
    public Map<keys,reaction[]> get_reactions()
    {
    	return this.Reactions_;
    }

    public reaction get_current_reaction()
    {
        if ( this.current_reaction_key != null)
        {
            return this.Reactions_.get(this.current_reaction_key)[this.State_ -1];
        }
        return this.standby;
    }

    public byte[] get_current_reaction_array()
    {
//        System.out.println("creating current reaction array");
        byte[] tmp = new byte[3];
        if ( this.current_reaction_key != null && this.State_ > 0)
        {
            tmp[0] = this.Reactions_.get(this.current_reaction_key)[this.State_ -1].get_pattern().get_key();
            tmp[1] = this.Reactions_.get(this.current_reaction_key)[this.State_ -1].get_color1().get_key();
            tmp[2] = this.Reactions_.get(this.current_reaction_key)[this.State_ -1].get_color2().get_key();
        }
        else
        {
            tmp[0] = this.standby.get_pattern().get_key();
            tmp[1] = this.standby.get_color1().get_key();
            tmp[2] = this.standby.get_color2().get_key();
        }
        return tmp;
    }

    public reaction get_standby()
    {
        return this.standby;
    }

    public reaction get_neighbor(String str) {
        for (Map.Entry e : neighbours_.entrySet())
            if (e.getKey().equals(str))
                return (reaction) e.getValue();
        //if the string was'nt found in the map as a key, return default values
        return new reaction(color.GREEN_BRIGHT, color.BLUE, pattern.BLINK); //TODO change return value to something configurable
    }
    
    public reaction get_standby_reaction()
    {
    	return this.standby;
    }

    public int get_state_duration()
    {
        return this.get_current_reaction().get_durration();
    }

    public void inc_state() {
        short state = this.State_;
        state += 1;
        this.set_State(state);
    }

    public void dec_state() {
        this.set_State(--this.State_);
    }

    public  boolean check_state(int state)
    {
        if (state < 0 || state > this.state_count)
            return false;
        return true;
    }

    private void create_reaction_map()
    {
        //ToDo set default value somehow
        this.Reactions_ = new DefaultHashMap<keys, reaction[]>(new reaction[4]);
    }

}
