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
byte patternKey = BLINK;
byte firstColor = 7;
byte secondColor = 4;
boolean changePatternState = false;

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
          
    _pearLeds.setLedPattern(patternKey, firstColor, secondColor); 
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
           _actuatorNode.ledBlink();   
           _actuatorNode.ccPrintPacket();
          
           if(_actuatorNode.keyforLeds())  
           {
               patternKey = _actuatorNode.getKey();
               firstColor = _actuatorNode.getFirstColor();
               secondColor = _actuatorNode.getSecondColor();
               _pearLeds.setLedPattern(patternKey, firstColor, secondColor);
           }
           else         
               _actuatorNode.ccHandle();  
            
         }     
                
        _packetAvailable = false; 
        
        enableRFChipInterrupt();  
    } 
    else 
    {
      
     BlinkCounter();    
         
    }

}

void BlinkCounter()
{
   if(millis() - lastTimeBlink > 3000 )
   {
       changePatternState = !changePatternState;
       lastTimeBlink = millis();
       
       upDownBlink();
   }       
       
}


void upDownBlink()
{
  if(changePatternState)
  {    
    _pearLeds.setLedPattern(LEDSON, firstColor, secondColor); 
   }
   else
    {
    _pearLeds.setLedPattern(LEDSOFF, 0, 0);   
   }
  
}




