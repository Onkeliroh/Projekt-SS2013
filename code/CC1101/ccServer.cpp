#include "ccServer.h"



//CONSTRUCTOR//

CCSERVER::CCSERVER(byte id)

{

    _id = id;
    _syncWord = (19,9); 
    _twinNode = 0;        
          
}


//DESTRUCTOR//
CCSERVER::~CCSERVER(void)

{

	
}

//METHODS
void CCSERVER::setup()

{
    Serial.begin(115200); // 9600 // 38400
    
    ledBlinkSetup(); 

    rfChipInit();    

}




boolean CCSERVER::ccReceive(void)

{   

    CCPACKET ccPacket; 
    boolean validPacket = false; 

    byte cc11 = _cc1101.receiveData(&ccPacket);
    
    if(cc11 > 0) // some data was received
    {     
        if (ccPacket.crc_ok && ccPacket.length > 1) // the whole ccPacket was properly received
        {
	    if(ccPacket.RECEIVER_ID == BROADCAST || ccPacket.RECEIVER_ID == _id )
            {
  	        _ccPacketHandler.setPacket(ccPacket);
                validPacket = true; 
            }
                                 
        } 
    }

    return validPacket;
      
}


void CCSERVER::ccHandle(void)

{

    byte key = _ccPacketHandler.getAdminKey();
  
    switch (key)
    {
        case SHAKE_EVENT: 
            //ccAcknowledge();   
            _ccPacketHandler.buildPacket(LED_CLIENT_01, _id, CHANGE_COLOR); 
            _ccPacketHandler.printPacket();
            ccSend();  
            break;
        case NEAR_NODE_EVENT: 
            distanceAlert();
            //ccAcknowledge();
            break;    
        case ACKNOWLEDGE_REQUEST:
            if (_ccPacketHandler.hashMatches()) 
                _ccPacketHandler._ccClear = true;
            else
                Serial.println("False acknowledge! Resending prev. package");
            break;   
        case LOW_BATTERY:
            Serial.println("Node has low batt!");   
        case TEST: 
            ccAcknowledge();
            break;
        default: // unknown packet received
            Serial.print("Unknown packet received, key: ");
            Serial.println(key);
            break; 
    }
}


boolean CCSERVER::isSender()

{
   byte sender = _ccPacketHandler.getPacketSender();   
 
   if(sender == _id)
       return true;
   else
       return false;
}




int CCSERVER::ccRSSI(byte rawRSSI)

{

    int rssi_dBm;

    if (rawRSSI >= 128)
        rssi_dBm = (((int)rawRSSI - 256) / 2) - RSSI_OFFSET;
    else
        rssi_dBm = ((int)rawRSSI / 2) - RSSI_OFFSET;
    
    return rssi_dBm;  
}


void CCSERVER::distanceAlert()
{

    CCPACKET ccPacket = _ccPacketHandler.getPacket();
    byte rssi_dBm = ccRSSI(ccPacket.NEAR_NODE_RSSI);
    byte emisorID = ccPacket.NEAR_NODE_ID;
  
    Serial.print("Detected RSSI " );
    Serial.print(rssi_dBm);
    Serial.print(" from device Nr: \t");
    Serial.println(emisorID);
  
}


void CCSERVER::lowBatteryAlert()
{
    CCPACKET ccPacket = _ccPacketHandler.getPacket();
    byte nodeLowBattery = ccPacket.SENDER_ID;
 
    Serial.print("Node Nr. " );
    Serial.print(nodeLowBattery);
    Serial.println("  is running out of Battery!!!");  
}






