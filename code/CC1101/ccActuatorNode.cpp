#include "ccActuatorNode.h"

boolean _clear = true;


//CONSTRUCTOR//

CCACTUATORNODE::CCACTUATORNODE(byte id, byte twinNode)

{

    _id = id;
    _twinNode = twinNode;
    _syncWord = (19,9);   
   
              
}


//DESTRUCTOR//
CCACTUATORNODE::~CCACTUATORNODE()

{

	
}

//METHODS
void CCACTUATORNODE::setup()

{
    
    Serial.begin(BAUDRATE); 

    ledBlinkSetup();
  
    rfChipInit();
    

}



boolean CCACTUATORNODE::ccGetNewPacket()

{   
    
    CCPACKET ccPacket; 
    boolean validPacket = false; 

    byte cc11 = _cc1101.receiveData(&ccPacket);
   
    if(cc11 > 0) // some data was received      
    {
        if (ccPacket.crc_ok && ccPacket.length > 1) // the whole ccPacket was properly received
        {  
            if(ccPacket.RECEIVER_ID == _id) 
            {                 
                validPacket = true; 
                _ccPacketHandler.setPacket(ccPacket);
            }   
                 
        }
    }
    
    return validPacket;
   
}

boolean CCACTUATORNODE::keyforLeds()
{  
    boolean keyForLeds = false;
    byte key = _ccPacketHandler.getAdminKey();

    if(key < MAXKEYPATTERN)
    {
        keyForLeds = true;
    }

    return keyForLeds;
}


byte CCACTUATORNODE::getKey()
{
    return _ccPacketHandler.getAdminKey();
}

byte CCACTUATORNODE::getFirstColor()

{
    return _ccPacketHandler.getFirstColor();
}

byte CCACTUATORNODE::getSecondColor()

{
    return _ccPacketHandler.getSecondColor();
}


void CCACTUATORNODE::ccHandle()

{
    byte key = _ccPacketHandler.getAdminKey();
      
    switch (key)
    {
       case ACKNOWLEDGE_REQUEST: 
            break;
        case ACKNOWLEDGE_RESPONSE:
            _clear = true;
            break;
        case TEST: 
            //flipRainbow();
            //ccAcknowledge();
            break;
        default: // unknown packet received
            Serial.print("ERROR - unknown packet received, key: ");
            Serial.println(key);
            break; 
    }
}

