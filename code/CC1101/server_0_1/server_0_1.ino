#include "analogComp.h"
#include "EEPROM.h"
#include "ccServer.h"

#define SERVER_ID 1

#define enableRFChipInterrupt()     attachInterrupt(0, RFChipInterrupt, FALLING);
#define disableRFChipInterrupt()    detachInterrupt(0);

/////////////////////
//--- INSTANCES ---//
/////////////////////

CCSERVER  _server = CCSERVER(SERVER_ID);

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
/////////////////////&


void loop()
{
    if(_packetAvailable) 
      {
          disableRFChipInterrupt();
        
          if(_server.ccGetNewPacket())
          {
              _server.ccHandle();
              //delay(10);
            
          }
        
           _packetAvailable = false;   
           enableRFChipInterrupt();
    
       }
    else 
       {
           if(_server.newJavaCommand())
           {
              _server.getJavaCommand();
              _server.ccSendCommand();
              _server.ccPrintPacket();  
              _server.cleanBuffer();
           } 
           
       }   
         
}
