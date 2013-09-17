#include "ccServer.h"



//CONSTRUCTOR//

CCSERVER::CCSERVER(byte id)

{

    _id = id;
    _syncWord = (19,9); 
    _twinNode = 0; 
    cleanBuffer();
        
}


//DESTRUCTOR//
CCSERVER::~CCSERVER(void)

{

	
}

//METHODS
void CCSERVER::cleanBuffer()
{
    for(int i=0;i<BUFFERLENGTH;i++)    
        buffer[i]= 0;      
}

void CCSERVER::setup()

{
    Serial.begin(BAUDRATE); 
    
    ledBlinkSetup(); 

    rfChipInit();    

}


boolean CCSERVER::ccGetNewPacket(void)

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
    #ifdef SERVERDEBUG
        _ccPacketHandler.printPacket();		
    #endif

    byte key = _ccPacketHandler.getAdminKey();
  
    switch (key)
    {
        case SHAKE_EVENT: 
            #ifdef BYPASS_JAVASERVER
                //ccAcknowledge();   
                _ccPacketHandler.buildPacket(LED_CLIENT_01, _id, CHANGE_COLOR); 
                _ccPacketHandler.printPacket();
                ccSendPacket();  
            #endif
            break;
        case NEAR_NODE_EVENT:            
            #ifdef SERVERDEBUG
                distanceAlert();
            #endif      
            break;    
        case ACKNOWLEDGE_REQUEST:
            if (_ccPacketHandler.hashMatches()) 
                _ccPacketHandler._ccClear = true;
            else
                #ifdef SERVERDEBUG
                    Serial.println("False acknowledge! Resending prev. package");
                #endif
            break;   
        case LOW_BATTERY:
            #ifdef SERVERDEBUG
                lowBatteryAlert();
            #endif
        case TEST: 
            ccAcknowledge();
            break;
        default: // unknown packet received
            #ifdef SERVERDEBUG
                Serial.print("Unknown packet received, key: ");
                Serial.println(key);
            #endif
            break; 
    }
}


void CCSERVER::saveDataInBuffer(void)
{
    byte key = _ccPacketHandler.getAdminKey();
  
    if(key == NEAR_NODE_EVENT)
    {
        setNearNodeBuffer();
    } 
    else
    {
        setBuffer();
    } 
   
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


void CCSERVER::setBuffer()

{
    CCPACKET ccPacket = _ccPacketHandler.getPacket();
  
    buffer[0] = ccPacket.SENDER_ID;
    buffer[1] = ccPacket.ADMINKEY;
    buffer[2] = 0;   //to do testing 
    buffer[3] = 10;  //to do testing. This corresponds to "end line" in ASCII
    
}

void CCSERVER::setNearNodeBuffer()
{
    CCPACKET ccPacket = _ccPacketHandler.getPacket();
    byte nearNodeRssiDBm = ccRSSI(ccPacket.NEAR_NODE_RSSI);

  
    buffer[0] = ccPacket.SENDER_ID;
    buffer[1] = ccPacket.ADMINKEY;
    buffer[2] = ccPacket.NEAR_NODE_ID;
    buffer[3] = 10;
  
}


void CCSERVER::sendBufferToJavaServer()
{
    for(int i=0; i<BUFFERLENGTH; i++)
    {
        Serial.write(buffer[i]);                     
    }
          
}

boolean CCSERVER::newJavaCommand()
{
    boolean newCommand = false;
   
    if(Serial.available() > 0)    
    {  
        newCommand = true;       
     }
   
     return newCommand;
} 


void CCSERVER::getJavaCommand()
{
    for(int i=0; i<BUFFERLENGTH; i++)
    {
        if(Serial.available() > 0)
        {
             buffer[i] = Serial.read();      
             delay(1);      
        }
    }
} 

void CCSERVER::TestBuffer1()
{
   buffer[0] = 38;
   buffer[1] = 44;
   buffer[2] = 4;
   buffer[3] = 7; 
}

void CCSERVER::TestBuffer2()
{
   buffer[0] = 38;
   buffer[1] = 44;
   buffer[2] = 10;
   buffer[3] = 15; 
}

void CCSERVER::ccSendCommand()
{
    setNewCommand(); 
    ccSendPacket();

}

void CCSERVER::setNewCommand()
{
    byte receiverId = buffer[0];
    byte metaKey = buffer[1];
    byte firstColor = buffer[2];
    byte secondColor = buffer[3];
    
    _ccPacketHandler.buildPatternCommand(receiverId, metaKey, firstColor, secondColor);    
}              
                


