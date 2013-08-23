#ifndef SerialCommand_h
#define SerialCommand_h

#include "Arduino.h"

#define SERIALCOMMANDBUFFER 10

#define SERIALCOMMANDDEBUG 1
//#undef SERIALCOMMANDDEBUG      // Comment this out to run the library in debug mode (verbose messages)

#define KEY 2

class SerialCommand
{
	public:
		SerialCommand();      // Constructor
		void clearBuffer();   // Sets the command buffer to all 0x00
		void readSerial();   
                void showCommandKey();
                void showBuffer();
			
	private:
		byte inByte;          // A byte read from the serial stream 
		byte buffer[SERIALCOMMANDBUFFER];   // Buffer of stored bytes while waiting for terminator character
		byte bufPos;                        // Current position in the buffer
		//byte endBuffer;                     // Character that signals end of command (default 255)		       
			
};

#endif //SerialCommand_h
