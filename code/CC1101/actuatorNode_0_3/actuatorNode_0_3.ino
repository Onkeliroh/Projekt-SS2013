#include "EEPROM.h"
#include "ccActuatorNode.h"
#include "kidneyLeds.h"


#define ACTUATORNODE 5     //needed a number with a normal ascii character for testing
#define TWIN_NODE_ID 4

#define enableRFChipInterrupt()     attachInterrupt(0, RFChipInterrupt, FALLING);
#define disableRFChipInterrupt()    detachInterrupt(0);


/////////////////////
//--- INSTANCES ---//
/////////////////////

CCACTUATORNODE _actuatorNode = CCACTUATORNODE(ACTUATORNODE,TWIN_NODE_ID); 
KIDNEYLEDS _kidneyLeds = KIDNEYLEDS();


////////////////////
//--- MEMBERS ---//
///////////////////

boolean _packetAvailable = false;
byte patternKey = BLINK;
byte firstColor = 0;
byte secondColor = 0;


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
    _kidneyLeds.setup();
    enableRFChipInterrupt();           
    _kidneyLeds.updateLedPattern();
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
               firstColor = _actuatorNode.getFirstColor();
               _kidneyLeds.setFirstColor(firstColor);
               
               secondColor = _actuatorNode.getSecondColor();
               _kidneyLeds.setSecondColor(secondColor); 
               
               patternKey = _actuatorNode.getKey();
               _kidneyLeds.setPattern(patternKey);    
               
               _kidneyLeds.updateLedPattern();
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
   if(millis() - lastTimeBlink > 500 )
   {
       lastTimeBlink = millis();
       _kidneyLeds.updateLedPattern();            
   }       
       
}


