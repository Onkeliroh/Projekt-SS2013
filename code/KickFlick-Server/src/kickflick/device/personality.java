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
    private reaction standby = new reaction(color.BLUE,color.BLACK,pattern.FADE);
    private reaction current_reaction = null;
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
    }

    public personality(String name, short state, DefaultHashMap<keys, reaction[]> tmp_reactions, HashMap<String, reaction> neighbours, reaction stand) {
        create_reaction_map();

        this.Name_ = name;
        this.State_ = state;
        this.Reactions_ = tmp_reactions;
        this.neighbours_ = neighbours;
        this.standby = stand;
    }

    public personality(String name, short state, DefaultHashMap<keys, reaction[]> tmp_reactions, HashMap<String, reaction> neighbours) {
        create_reaction_map();

        this.Name_ = name;
        this.State_ = state;
        this.Reactions_ = tmp_reactions;
        this.neighbours_ = neighbours;
    }

    public personality(personality pers) {
        create_reaction_map();

        this.Name_ = pers.Name_;
        this.State_ = pers.State_;
        this.Reactions_ = pers.Reactions_;
    }


    //Setter

    public void set_Name(String name) {
        this.Name_ = name;
    }

    public void set_State(short state) {
        if (state < state_count && state >= -1)
            this.State_ = state;
    }

    public void set_standby (reaction tmp)
    {
        this.standby = tmp;
    }

    //Getter

    public String get_Name() {
        return this.Name_;
    }

    public short get_State() {
        return this.State_;
    }

//    public byte get_Color1() {
//        return this.Color1_[this.State_];
//    }
//
//    public byte get_Color2() {
//        return this.Color2_[this.State_];
//    }
//
//    public byte get_Color1(short state) {
//        return this.Color1_[state];
//    }
//
//    public byte get_Color2(short state) {
//        return this.Color2_[state];
//    }
//
//    public byte get_pattern(short state) {
//        return this.pattern_[state];
//    }
//
//    public byte get_pattern() {
//        return this.pattern_[this.State_];
//    }

    public String get_state_name() {
        return this.state_names[this.State_-1];
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
                        return ((reaction[])e.getValue())[state];
                }
                return this.Reactions_.get(key)[state];
            }
            else if (state == 0)
            {
                return this.standby;
            }
        }
        else
        {
            System.err.println("Personality get_reaction of " + this.Name_ + " wrong state");
            return null;
        }
    }



    public reaction get_neighbor(String str) {
        for (Map.Entry e : neighbours_.entrySet())
            if (e.getKey().equals(str))
                return (reaction) e.getValue();
        //if the string was'nt found in the map as a key, return default values
        return new reaction(color.GREEN_BRIGHT, color.BLUE, pattern.BLINK);
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
