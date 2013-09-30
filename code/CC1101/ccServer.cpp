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
    {   
        buffer[i]= 0x00;       
    }
}

void CCSERVER::setup()

{
    Serial.begin(BAUDRATE); 
     
    Serial.flush();
    
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
 
    byte key = _ccPacketHandler.getAdminKey();

      switch (key)
    {
        case SHAKE_EVENT: 
            setBuffer();         
            sendBufferToJavaServer();
            cleanBuffer();
            break;
        case NEAR_NODE_EVENT:            
            //showRSSI();  //For debugging purposes
            setNearNodeBuffer();      
            sendBufferToJavaServer();
            cleanBuffer();      
            break;    
        case ACKNOWLEDGE:
            setBuffer();         
            sendBufferToJavaServer();
            break;   
        case LOW_BATTERY:
            setBuffer();         
            sendBufferToJavaServer();
            cleanBuffer();
        case INRANGE:
            setBuffer();         
            sendBufferToJavaServer(); 
            cleanBuffer();      
        default: // unknown packet received
            //SEND MESSAGE TO ORDER A PACKAGE. NEED TO IMPLEMENT THIS
            break; 
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


void CCSERVER::showRSSI()
{

    CCPACKET ccPacket = _ccPacketHandler.getPacket();
    byte rssi_dBm = ccRSSI(ccPacket.NEAR_NODE_RSSI);
    byte emisorID = ccPacket.NEAR_NODE_ID;
  
    //Serial.print("Detected RSSI " );
    //Serial.print(rssi_dBm);
    //Serial.print(" from device Nr: \t");
    //Serial.println(emisorID);    
}


void CCSERVER::lowBatteryAlert()
{
    CCPACKET ccPacket = _ccPacketHandler.getPacket();
    byte nodeLowBattery = ccPacket.SENDER_ID;
 
    //Serial.print("Node Nr. " );
    //Serial.print(nodeLowBattery);
    //Serial.println("  is running out of Battery!!!");      

}


void CCSERVER::setBuffer()

{
    CCPACKET ccPacket = _ccPacketHandler.getPacket();
    
    buffer[SENDERID] = ccPacket.SENDER_ID;  
    buffer[KEY] = ccPacket.ADMINKEY;
    buffer[DUMMY] = NULL; 
    buffer[CHECKSUM] = getBufferChecksum(); 
       
}

void CCSERVER::setNearNodeBuffer()
{
    CCPACKET ccPacket = _ccPacketHandler.getPacket();
    byte nearNodeRssiDBm = ccRSSI(ccPacket.NEAR_NODE_RSSI);
      
    buffer[SENDERID] = ccPacket.SENDER_ID;
    buffer[KEY] = ccPacket.ADMINKEY;
    buffer[NEARNODEID] = ccPacket.NEAR_NODE_ID;
    buffer[CHECKSUM] = getBufferChecksum(); 
     
}


void CCSERVER::sendBufferToJavaServer()
{
    for(int i=0; i<BUFFERLENGTH; i++)
    {
        Serial.write(buffer[i]); 
                                   
    }       
    delay(1);      //Let's see if this is necessary 29-09       
}

boolean CCSERVER::newJavaCommand()
{
    boolean newCommand = false;
   
    if(Serial.available() >= BUFFERLENGTH)    
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
        }	
     }     
    
} 

void CCSERVER::ccSendCommand()
{
    setNewCommand(); 
    ccSendPacket();
}


void CCSERVER::setNewCommand()
{
    //Serial.write(RECEIVERID);   
    //Serial.write(METAKEY);      
    //Serial.write(COLOR1);       
    //Serial.write(COLOR2);       
 
    _ccPacketHandler.buildPatternCommand(RECEIVERID, METAKEY, COLOR1, COLOR2); 
}              
     
byte CCSERVER::getBufferChecksum()
{
    byte checkSum = 0;

    for (byte i = 0; i < BUFFERLENGTH-1; ++i)

    {

        checkSum += buffer[i];

    }

    return checkSum;

}
           




