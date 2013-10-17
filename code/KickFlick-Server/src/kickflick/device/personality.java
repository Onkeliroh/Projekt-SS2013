package kickflick.device;

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
//    private byte[] Color1_ = new byte[4];
//    private byte[] Color2_ = new byte[4];
//    private byte[] pattern_ = new byte[4];
    private Map<keys, reaction[]> Reactions_ = new HashMap<keys, reaction[]>();
    private Map<String, reaction> neighbours_ = new HashMap<String, reaction>();

    //Constructors

    // default
    public personality() {
        this.Name_ = presetpersonalities.Paul.get_personality().Name_;
        this.State_ = presetpersonalities.Paul.get_personality().State_;
        this.Reactions_ = presetpersonalities.Paul.get_personality().Reactions_;
        this.neighbours_ = presetpersonalities.Paul.get_personality().neighbours_;
    }

    public personality(String name, short state, HashMap<keys, reaction[]> tmp_reactions, HashMap<String, reaction> neighbours) {
        this.Name_ = name;
        this.State_ = state;
        this.reactions = tmp_reactions;
        this.neighbours_ = neighbours;
    }

    public personality(personality pers) {
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

    //sets the color of the current state
    public void set_Color1(byte Color) {
        //this.Color_.set_Color(Color.get_Color());
        this.Color1_[this.State_] = Color;
    }

    public void set_Color2(byte Color) {
        //this.Color_.set_Color(Color.get_Color());
        this.Color2_[this.State_] = Color;
    }

    //sets the color of a state
    public void set_Color1(byte Color, short state) {
        this.Color1_[state] = Color;
    }

    //sets the color of a state
    public void set_Color2(byte Color, short state) {
        this.Color2_[state] = Color;
    }

    public void set_pattern(byte pattern) {
        this.pattern_[this.State_] = pattern;
    }

    public void set_pattern(byte pattern, short state) {
        this.pattern_[state] = pattern;
    }

    public void set_pattern(byte[] pattern) {
        this.pattern_ = pattern;
    }

    public void set_pattern(String PersName, reaction react) {
        this.neighbours_.put(PersName, react);
    }

    //Getter

    public String get_Name() {
        return this.Name_;
    }

    public short get_State() {
        return this.State_;
    }

    public byte get_Color1() {
        return this.Color1_[this.State_];
    }

    public byte get_Color2() {
        return this.Color2_[this.State_];
    }

    public byte get_Color1(short state) {
        return this.Color1_[state];
    }

    public byte get_Color2(short state) {
        return this.Color2_[state];
    }

    public byte get_pattern(short state) {
        return this.pattern_[state];
    }

    public byte get_pattern() {
        return this.pattern_[this.State_];
    }

    public String get_state_name() {
        return this.get_state_name(this.State_);
    }

    public String get_state_name(short state) {
        switch (state)
        {
            case -1:
                return "Out of Range";
            case 0:
                return "Standbye";
            case 1:
                return "First contact";
            case 2:
                return "Playing ";
            case 3:
                return "Playing (hard)";
            default:
                return "unknown";
        }
    }

    public Map<String, reaction> get_Neighbours() {
        return this.neighbours_;
    }

    public byte[] get_neighbor(String str) {
        for (Map.Entry e : neighbours_.entrySet())
            if (e.getKey().equals(str))
                return (byte[]) e.getValue();
        //if the string was'nt found in the map as a key, return default values
        return new byte[]{pattern.BLINK.get_key(), color.BLUE.get_key(), color.GREEN_BRIGHT.get_key()};
    }

    public void inc_state() {
        short state = this.State_;
        state += 1;
        this.set_State(state);
    }

    public void dec_state() {
        this.set_State(--this.State_);
    }

    private create_reaction_map()
    {
        int i = 0;
        for ( keys k : keys.values())
        {
            reaction[] tmp_reaction = new reaction[4];
            tmp_reaction[0] = new reaction(presetpersonalities.Paul.get_personality().)
            this.reactions.put(k,new reaction[i]())
        }
    }

}
