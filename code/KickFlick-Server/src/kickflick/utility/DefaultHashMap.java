package kickflick.utility;

import java.util.HashMap;
import java.util.Map;

public class DefaultHashMap<K,V> extends HashMap<K,V> {
    protected V defaultValue;
    public DefaultHashMap(V defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public V get(Object k) {
        V v = super.get(k);
        return ((v == null) && !this.containsKey(k)) ? this.defaultValue : v;
    }
    
    public V get_default() {
    	return this.defaultValue;
    }

    public void set_default(V defaultValue) {
        this.defaultValue = defaultValue;
    }

}
