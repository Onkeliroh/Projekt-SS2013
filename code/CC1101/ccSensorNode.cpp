#include "ccSensorNode.h"


//CONSTRUCTOR//

CCSENSORNODE::CCSENSORNODE(byte id, byte twinNode)

{

    _id = id;
    _twinNode = twinNode;
    _syncWord = (19,9);    
              
}


//DESTRUCTOR//
CCSENSORNODE::~CCSENSORNODE()

{

	
}

//METHODS

void CCSENSORNODE::setup()

{

    Serial.begin(BAUDRATE); 
    
    ledBlinkSetup();
    
    rfChipInit();

    initBattMonitor();    //Had to repair the BattMonitor circuit 

}
    

void CCSENSORNODE::initBattMonitor()
{

    analogComparator.setOn(AIN0,AIN1);  //we instruct the lib to use voltages on the pins PD6 and PD7  

}


boolean CCSENSORNODE::ccGetNewPacket(void)

{   

    CCPACKET ccPacket; 
    boolean validPacket = false; 

    byte cc11 = _cc1101.receiveData(&ccPacket);
    
    if(cc11 > 0) // some data was received
    {     
        if (ccPacket.crc_ok && ccPacket.length > 1) // the whole ccPacket was properly received
        {
             _ccPacketHandler.setPacket(ccPacket);
          
	    if(ccPacket.RECEIVER_ID == _id )
            { 	        
                validPacket = true; 
            }
                                 
        } 
    }

    return validPacket;
      
}


void CCSENSORNODE::ccHandle()

{
    byte key = _ccPacketHandler.getAdminKey();
      
    switch (key)
    {
        case ACKNOWLEDGE:
            if (_ccPacketHandler.hashMatches()) 
                _ccPacketHandler._ccClear = true;
            else
                Serial.println("False acknowledge! Resending prev. package");
            break;    
        case TEST: // test packet received
            ccAcknowledge();
            break;
        default: // unknown packet received
            Serial.print("Unknown packet received, key: ");
            Serial.println(key);
            break; 
    }
}


boolean CCSENSORNODE::isPacketsSender()

{
   
   byte sender = _ccPacketHandler.getPacketSender();
    
   if(sender == _id || sender == _twinNode)
       return true;
   else
       return false;
}

void CCSENSORNODE::reportAccelEvent()

{

    _ccPacketHandler.buildPacket(BROADCAST, _id, SHAKE_EVENT);
    ccSendPacket();    

}


void CCSENSORNODE::reportRSSI()

{
    byte rawRSSI =  _ccPacketHandler.getPacketRSSI();
    byte nearNodeId =  _ccPacketHandler.getPacketSender();

    sendRSSI(rawRSSI,nearNodeId); 

}



void CCSENSORNODE::reportLowBatt()

{
    _ccPacketHandler.buildPacket(SERVER_01, _id, LOW_BATTERY);
    ccSendPacket();    
}


void CCSENSORNODE::sendRSSI(byte rawRSSI,byte nearNodeId)
{
    _ccPacketHandler.buildRSSIPacket(_id, rawRSSI,nearNodeId);
    ccSendPacket();  
}


void CCSENSORNODE::sendInRangePacket()

{
    _ccPacketHandler.buildPacket(SERVER_01, _id, INRANGE);
    ccSendPacket();    
}


