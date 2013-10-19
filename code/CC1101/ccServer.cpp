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
        case KICK_EVENT:
            setBuffer();
            sendBufferToJavaServer();
            cleanBuffer();
            break;
        case NEAR_NODE_EVENT:            
            checkRSSI();  
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


void CCSERVER::checkRSSI()
{

    CCPACKET ccPacket = _ccPacketHandler.getPacket();
    int rssi_dBm = ccRSSI(ccPacket.NEAR_NODE_RSSI);
    byte emisorID = ccPacket.NEAR_NODE_ID;
  
    //if(rssi_dBm > 202)  //212 
    //{
        //setNearNodeBuffer();      
        //sendBufferToJavaServer();
        //cleanBuffer();           

        Serial.print("Near Node Nr " );
        Serial.print(emisorID);
        Serial.print(" Signal strength ");
        Serial.println(rssi_dBm);
    //}
     
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
        //Serial.println(buffer[i]); 
                                   
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

void CCSERVER::sendColorCommand(byte red1, byte blue1, byte green1)
{
   setTestColorCommand(red1, blue1, green1);
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

void CCSERVER::setTestColorCommand(byte COLORR, byte COLORB, byte COLORG)
{
    //Serial.write(RECEIVERID);   
    //Serial.write(METAKEY);      
    //Serial.write(COLOR1);       
    //Serial.write(COLOR2);       
 
    _ccPacketHandler.buildRGBCommand(COLORR, COLORB, COLORG); 
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
           




