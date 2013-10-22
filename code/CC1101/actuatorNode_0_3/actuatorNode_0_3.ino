#include "EEPROM.h"
#include "ccActuatorNode.h"
#include "pearLeds.h"


#define ACTUATORNODE 3   
#define TWIN_NODE_ID 2

#define NUMLEDS  50       //PEAR has 50 leds

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
byte _patternKey = FADE;
byte _firstColor = 13;
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
    _pearLeds.setup(NUMLEDS);
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
                    
           if(_actuatorNode.keyforLeds())  
           {
               _firstColor = _actuatorNode.getFirstColor();
               _pearLeds.setFirstColor(_firstColor);
               
               _secondColor = _actuatorNode.getSecondColor();
               _pearLeds.setSecondColor(_secondColor); 
               
               _patternKey = _actuatorNode.getKey();
               _pearLeds.setPattern(_patternKey);  
             
               _patternPeriod = _pearLeds.getPatternPeriod(_patternKey);  
               
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
         if(patternHasPeriod(_patternKey))
         {
             changePatternState();   
         } 
    }

}

void setDefaultlLedPattern()
{
    _pearLeds.setFirstColor(_firstColor); 
    _pearLeds.setSecondColor(_secondColor); 
    _pearLeds.setPattern(_patternKey);
    _patternPeriod = _pearLeds.getPatternPeriod(_patternKey);      
    _pearLeds.updateLedPattern(); 
    
}


void changePatternState()
{
    if(millis() - _lastTimeBlink > _patternPeriod )
    {
        _lastTimeBlink = millis();
        _pearLeds.updateLedPattern();
           
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

