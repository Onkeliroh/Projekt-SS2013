#include "EEPROM.h"
#include "ccActuatorNode.h"
#include "kidneyLeds.h"


#define ACTUATORNODE 5     //needed a number with a normal ascii character for testing
#define TWIN_NODE_ID 4

#define NUMLEDS  38       //KIDNEY has 38 leds

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
byte _patternKey = ONESTRIPE;
byte _firstColor = 15;
byte _secondColor = 7;
unsigned long  _patternPeriod = 0;

unsigned long _lastTimeBlink = 0;
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
    _kidneyLeds.setup(NUMLEDS);
    enableRFChipInterrupt();           
    setDefaultlLedPattern();
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
               _kidneyLeds.setFirstColor(_firstColor);
               
               _secondColor = _actuatorNode.getSecondColor();
               _kidneyLeds.setSecondColor(_secondColor); 
               
               _patternKey = _actuatorNode.getKey();
               _kidneyLeds.setPattern(_patternKey);  
             
               _patternPeriod = _kidneyLeds.getPatternPeriod(_patternKey);  
               
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
         if(patternHasPeriod(_patternKey))
         {
             changePatternState();   
         } 
         
    }

}

void setDefaultlLedPattern()
{
    _kidneyLeds.setFirstColor(_firstColor); 
    _kidneyLeds.setSecondColor(_secondColor); 
    _kidneyLeds.setPattern(_patternKey);
    _patternPeriod = _kidneyLeds.getPatternPeriod(_patternKey);      
    _kidneyLeds.updateLedPattern(); 
    
}


void changePatternState()
{
    if(millis() - _lastTimeBlink > _patternPeriod )
    {
        _lastTimeBlink = millis();
        _kidneyLeds.updateLedPattern();
           
    }        
   
}

boolean patternHasPeriod(byte pattern)
{
    boolean hasPeriod = true;
   
    if((pattern == LEDSON)||(pattern == LEDSOFF))  
    {
        hasPeriod = false;  
      
    }
  
    return hasPeriod;
}


