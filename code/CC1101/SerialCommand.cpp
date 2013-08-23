#include "Arduino.h"
#include "SerialCommand.h"

// Constructor makes sure some things are set. 
SerialCommand::SerialCommand()
{
	
	//endBuffer=0x7A;   // return character, default terminator for commands
	clearBuffer(); 
}

//
// Initialize the command buffer being processed to all null characters
//
void SerialCommand::clearBuffer()
{
	for (int i=0; i<SERIALCOMMANDBUFFER; i++) 
	{
		buffer[i]= 0x00;
	}

	bufPos=0; 
}


void SerialCommand::readSerial() 
{
   
    while (Serial.available() > 0 && bufPos< SERIALCOMMANDBUFFER) 
    {
        inByte = Serial.read();   // Read single available character, there may be more waiting
	 	                
        buffer[bufPos++] = inByte;   // Put byte into buffer		
		
    }
    
    bufPos=0;               
}

void SerialCommand::showCommandKey()
{
    Serial.println(buffer[KEY],HEX);
}

void SerialCommand::showBuffer()
{
    for(int i=0; i<SERIALCOMMANDBUFFER; i++) 
    {           
        Serial.print(buffer[i],HEX);
    }
    
    Serial.println("");
}

