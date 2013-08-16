#include "analogComp.h"
#include "EEPROM.h"
#include "ccServer.h"


#define SERVER_ID 1

#define enableRFChipInterrupt() attachInterrupt(0, RFChipInterrupt, FALLING);
#define disableRFChipInterrupt() detachInterrupt(0);

/////////////////////
//--- INSTANCES ---//
/////////////////////

CCSERVER _server = CCSERVER(SERVER_ID);
boolean _packetAvailable = false;

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
    if(_packetAvailable)
    {
        disableRFChipInterrupt();
        
        if(_server.ccReceive())
        {
            _server.ccPrintPacket();
            _server.ccHandle();
             
        }
        
        if(!_server.isSender())
        {
            _server.ccPrintPacket();
        }
        
        _packetAvailable = false;
        enableRFChipInterrupt();
    
    }
    else
    {
        _server.ledBlink();
    }
  
    while (Serial.available())
    {
        // read the incoming byte:
        incomingByte = Serial.read();
        // say what you got:
        Serial.print("I received: ");
        Serial.println(incomingByte);
   
    }
}


