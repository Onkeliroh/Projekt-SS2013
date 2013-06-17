package kickflick.utility;

public interface commands {
	//Mutex
	
	
	//Commands
	byte  Reset = 011;
	byte StateReport = 012;
	byte Blink = 013;
	byte Beep = 014;
	byte Vibrate = 015;
	byte Hypernate = 016;
	
	//Send Data
	byte SendSC = 021;
	byte SendCS = 022;
	//byte StateReport = 023;
	byte Connected = 024;
	byte Disconnected = 025;
	
	//Request Data
	byte RequestDataSC = 031;
	byte RequestDataCS = 032;
	
}
