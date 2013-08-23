#include "analogComp.h"
#include "EEPROM.h"
#include "ccServer.h"


#define SERVER_ID 1

<<<<<<< HEAD
#define enableRFChipInterrupt() attachInterrupt(0, RFChipInterrupt, FALLING);
#define disableRFChipInterrupt() detachInterrupt(0);
=======
#define enableRFChipInterrupt()     attachInterrupt(0, RFChipInterrupt, FALLING);
#define disableRFChipInterrupt()    detachInterrupt(0);
>>>>>>> origin/master

/////////////////////
//--- INSTANCES ---//
/////////////////////

<<<<<<< HEAD
CCSERVER _server = CCSERVER(SERVER_ID);
boolean _packetAvailable = false;
=======
CCSERVER  _server = CCSERVER(SERVER_ID);
boolean   _packetAvailable = false;
>>>>>>> origin/master

//////////////////////
//--- INTERRUPTS ---//
//////////////////////

// Handle interrupt from CC1101 (INT0)
void RFChipInterrupt()

{
<<<<<<< HEAD
    _packetAvailable = true;
=======
    _packetAvailable = true;          
>>>>>>> origin/master

}


// The setup method gets called on start of the system.
void setup()
{
    _server.setup();
<<<<<<< HEAD
    enableRFChipInterrupt();
=======
    enableRFChipInterrupt();   
>>>>>>> origin/master
}


/////////////////////
//--- MAIN LOOP ---//
/////////////////////
byte incomingByte;


void loop()
{
<<<<<<< HEAD
    if(_packetAvailable)
=======
    if(_packetAvailable) 
>>>>>>> origin/master
    {
        disableRFChipInterrupt();
        
        if(_server.ccReceive())
        {
            _server.ccPrintPacket();
<<<<<<< HEAD
            _server.ccHandle();
=======
            _server.ccHandle(); 
>>>>>>> origin/master
             
        }
        
        if(!_server.isSender())
        {
            _server.ccPrintPacket();
        }
        
<<<<<<< HEAD
        _packetAvailable = false;
        enableRFChipInterrupt();
    
    }
    else
=======
        _packetAvailable = false;   
        enableRFChipInterrupt();
    
    }
    else 
>>>>>>> origin/master
    {
        _server.ledBlink();
    }
  
    while (Serial.available())
    {
        // read the incoming byte:
        incomingByte = Serial.read();
<<<<<<< HEAD
        // say what you got:
        Serial.print("I received: ");
=======
        // say what you got:   
        Serial.print("I received: ");    
>>>>>>> origin/master
        Serial.println(incomingByte);
   
    }
}
<<<<<<< HEAD


=======
>>>>>>> origin/master
