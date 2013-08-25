#include "analogComp.h"
#include "EEPROM.h"
#include "ccServer.h"

#define SERVER_ID 1

#define enableRFChipInterrupt()     attachInterrupt(0, RFChipInterrupt, FALLING);
#define disableRFChipInterrupt()    detachInterrupt(0);

#define REAL_DATA 1
#undef REAL_DATA  //Comment this out to use real packets

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
/////////////////////
byte incomingByte;


void loop()
{
  #ifdef REAL_DATA
    	
      if(_packetAvailable) 
      {
          disableRFChipInterrupt();
        
          if(_server.ccReceive())
          {
              _server.saveDataInBuffer();             
              _server.sendBufferToJavaServer();
            
              delay(2000);
            
           }
        
           _packetAvailable = false;   
           enableRFChipInterrupt();
    
       }
       else 
       {
           _server.ledBlink();
       }
    
  #else //Use random data to fill the buffer
  
        _server.setRandomBuffer();  
        _server.sendBufferToJavaServer(); 
        
        delay(2000);  
        
        _server.ledBlink();
        
  #endif
         
}
