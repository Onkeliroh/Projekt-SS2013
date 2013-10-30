#include "ccSensorNode.h"


//CONSTRUCTOR//

CCSENSORNODE::CCSENSORNODE(byte id, byte twinNode)

{

    _id = id;
    _twinNode = twinNode;
    _syncWord = (19,9);    
    _neighborRSSI = 0;
              
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

byte CCSENSORNODE::neighborSender()

{   
    return _ccPacketHandler.getPacketSender();   
}



int CCSENSORNODE::calculateTrueRSSI(byte rawRSSI)

{
    int rssi_dBm;

    if (rawRSSI >= 128)
    {
        rssi_dBm = (((int)rawRSSI - 256) / 2) - RSSI_OFFSET;
    }
    else
    {
        rssi_dBm = ((int)rawRSSI / 2) - RSSI_OFFSET;
    }
    
    return rssi_dBm;  
}

void CCSENSORNODE::storeNeighborRSSI()
{
    byte rawRSSI =  _ccPacketHandler.getPacketRSSI();

    _neighborRSSI = calculateTrueRSSI(rawRSSI);

}

boolean CCSENSORNODE::neighborRssiIsHigh(int neighborThreshold)
{

    boolean rssiIsHigh = false;

    if(_neighborRSSI >= neighborThreshold)
    {
        rssiIsHigh = true;
    }
    
    return rssiIsHigh;

}

boolean CCSENSORNODE::isNeighborClose(int neighborId, int neighborRssiThreshold)
{
    boolean neighborIsClose = false;
  
    if( (neighborSender() == neighborId) && (neighborRssiIsHigh(neighborRssiThreshold)))
    {
        neighborIsClose = true;         
    }
     
    return neighborIsClose;  
}


void CCSENSORNODE::reportShakeEvent()

{

    _ccPacketHandler.buildPacket(BROADCAST, _id, SHAKE_EVENT);
    ccSendPacket();    

}


void CCSENSORNODE::reportKickEvent()

{

    _ccPacketHandler.buildPacket(BROADCAST, _id, KICK_EVENT);
    ccSendPacket();    

}


void CCSENSORNODE::reportRSSI()

{
    byte rawRSSI =  _ccPacketHandler.getPacketRSSI();
    byte nearNodeId =  _ccPacketHandler.getPacketSender();

    sendRSSI(rawRSSI,nearNodeId); 

}


void CCSENSORNODE::reportDetectedNearNode()
{
    byte nearNodeId =  _ccPacketHandler.getPacketSender();

    _ccPacketHandler.buildDetectedNodePacket(_id, nearNodeId);
    ccSendPacket();   

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


