#include "analogComp.h"
#include "EEPROM.h"
#include "ccServer.h"
#include "SerialCommand.h"

#define SERVER_ID 1

#define NORMAL_FLOW 1
#undef NORMAL_FLOW   //comment this line out when not doing tests

#define enableRFChipInterrupt()     attachInterrupt(0, RFChipInterrupt, FALLING);
#define disableRFChipInterrupt()    detachInterrupt(0);

/////////////////////
//--- INSTANCES ---//
/////////////////////

CCSERVER  _server = CCSERVER(SERVER_ID);
SerialCommand SCmd;   

boolean   _packetAvailable = false;

//////////////////////
//--- INTERRUPTS ---//
//////////////////////

// Handle interrupt from CC1101 (INT0)
void RFChipInterrupt()

{
    _packetAvailable = true;          

}


// The setup method gets called on start of the system.
void setup()
{
    _server.setup();
    enableRFChipInterrupt();   
}


/////////////////////
//--- MAIN LOOP ---//
/////////////////////
byte incomingByte;


void loop()
{
    #ifdef NORMAL_FLOW		
		
        if(_packetAvailable) 
        {
            disableRFChipInterrupt();
        
            if(_server.ccReceive())
            {
                _server.ccHandle(); 
            
            }
        
            _packetAvailable = false;   
            enableRFChipInterrupt();
    
         }
        else 
        {
            _server.ledBlink();
        }
    
    #endif
  
    SCmd.readSerial();
    SCmd.showBuffer();
    SCmd.clearBuffer();
     
    delay(5000);
}
