package kickflick.utility;

import java.util.ArrayList;

public class SerialEventHubSource {
	private ArrayList listeners_ = new ArrayList();
	public synchronized void addEventListener(SerialEventHubClassListener listener) {
		listeners_.add(listener);
	}
	
	public synchronized void removeEventListener(SerialEventHubClassListener listener) {
		listeners_.remove(listener);
	}
}
