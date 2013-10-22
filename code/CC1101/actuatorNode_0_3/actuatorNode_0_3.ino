#include "EEPROM.h"
#include "ccActuatorNode.h"
#include "pearLeds.h"


#define ACTUATORNODE 3     //needed a number with a normal ascii character for testing
#define TWIN_NODE_ID 2

#define enableRFChipInterrupt()     attachInterrupt(0, RFChipInterrupt, FALLING);
#define disableRFChipInterrupt()    detachInterrupt(0);


/////////////////////
//--- INSTANCES ---//
/////////////////////

CCACTUATORNODE _actuatorNode = CCACTUATORNODE(ACTUATORNODE,TWIN_NODE_ID); 
PEARLEDS _pearLeds = PEARLEDS();


////////////////////
//--- MEMBERS ---//
///////////////////

boolean _packetAvailable = false;
byte _patternKey = BLINK;
byte _firstColor = 7;
byte _secondColor = 4;


unsigned long lastTimeBlink = 0;
//////////////////////
//--- INTERRUPTS ---//
//////////////////////

// Handle interrupt from CC1101 (INT0)
void RFChipInterrupt()
{
    _packetAvailable = true;            // set the flag that a package is available        
}


///////////////////////////
//--- INITIALIZATIONS ---//
///////////////////////////

void setup()
{
    _actuatorNode.setup();
    _pearLeds.setup();
    enableRFChipInterrupt();           
    _pearLeds.updateLedPattern();
}


/////////////////////
//--- MAIN LOOP ---//
/////////////////////

void loop()
{  
    if(_packetAvailable) 
    {              
        disableRFChipInterrupt();        
         
        if(_actuatorNode.ccGetNewPacket())
        {
//           _actuatorNode.ledBlink();   
                     
           if(_actuatorNode.keyforLeds())  
           {
               _firstColor = _actuatorNode.getFirstColor();
               _pearLeds.setFirstColor(_firstColor);
               
               _secondColor = _actuatorNode.getSecondColor();
               _pearLeds.setSecondColor(_secondColor); 
               
               _patternKey = _actuatorNode.getKey();
               _pearLeds.setPattern(_patternKey);    
               
               _pearLeds.updateLedPattern();
           }
           else         
               _actuatorNode.ccHandle();  
            
         }     
                
        _packetAvailable = false; 
        
        enableRFChipInterrupt();  
    } 
    else 
    {
      
         changePatternState();    
         
    }

}

void changePatternState()
{
   if(millis() - _lastTimeBlink > 500 )
   {
       _lastTimeBlink = millis();
       _pearLeds.updateLedPattern();            
   }       
       
}


