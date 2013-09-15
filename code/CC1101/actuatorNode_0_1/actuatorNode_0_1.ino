#include "EEPROM.h"
#include "ccActuatorNode.h"


#define ACTUATORNODE 38 //needed a number with a normal ascii character for testing
#define TWIN_NODE_ID 2

#define enableRFChipInterrupt()     attachInterrupt(0, RFChipInterrupt, FALLING);
#define disableRFChipInterrupt()    detachInterrupt(0);

/////////////////////
//--- INSTANCES ---//
/////////////////////

CCACTUATORNODE _actuatorNode = CCACTUATORNODE(ACTUATORNODE,TWIN_NODE_ID); 

////////////////////
//--- MEMBERS ---//
///////////////////

boolean _packetAvailable = false;


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
    enableRFChipInterrupt();   
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

            _actuatorNode.ccHandle();  
            
        }
                
        _packetAvailable = false; 
        
        enableRFChipInterrupt();  
    }    
}



