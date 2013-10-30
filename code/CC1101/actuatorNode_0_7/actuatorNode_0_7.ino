#include "EEPROM.h"
#include "ccActuatorNode.h"
#include "eggLeds.h"


#define ACTUATORNODE 7     
#define TWIN_NODE_ID 6

#define NUMLEDS  12       //EGG has 12 leds


#define enableRFChipInterrupt()     attachInterrupt(0, RFChipInterrupt, FALLING);
#define disableRFChipInterrupt()    detachInterrupt(0);


/////////////////////
//--- INSTANCES ---//
/////////////////////

CCACTUATORNODE _actuatorNode = CCACTUATORNODE(ACTUATORNODE,TWIN_NODE_ID); 
EGGLEDS _eggLeds = EGGLEDS();


////////////////////
//--- MEMBERS ---//
///////////////////

boolean _packetAvailable = false;
byte _patternKey = FADE;
byte _firstColor = 13;
byte _secondColor = 1;
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
    _eggLeds.setup(NUMLEDS);
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
               _eggLeds.setFirstColor(_firstColor);
               
               _secondColor = _actuatorNode.getSecondColor();
               _eggLeds.setSecondColor(_secondColor); 
               
               _patternKey = _actuatorNode.getKey();
               _eggLeds.setPattern(_patternKey);
           
               _patternPeriod = _eggLeds.getPatternPeriod(_patternKey);    
               
               _eggLeds.updateLedPattern();
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
    _eggLeds.setFirstColor(_firstColor); 
    _eggLeds.setSecondColor(_secondColor); 
    _eggLeds.setPattern(_patternKey);
    _patternPeriod = _eggLeds.getPatternPeriod(_patternKey);      
    _eggLeds.updateLedPattern(); 
    
}

void changePatternState()
{
    if(millis() - _lastTimeBlink > _patternPeriod )
    {
        _lastTimeBlink = millis();
        _eggLeds.updateLedPattern();
           
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



