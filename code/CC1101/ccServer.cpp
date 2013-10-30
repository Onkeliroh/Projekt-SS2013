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
            setNearNodeBuffer();
            sendBufferToJavaServer();
            cleanBuffer();
            break;    
        case ACKNOWLEDGE:
            setBuffer();         
            sendBufferToJavaServer();
            cleanBuffer();
            break;   
        case LOW_BATTERY:
            setBuffer();         
            sendBufferToJavaServer();
            cleanBuffer();
            break;
        case INRANGE:
            setBuffer();         
            sendBufferToJavaServer(); 
            cleanBuffer();  
            break;    
        default:             
            break; 
    }
}


/////////////////////////////////////////
///////// BUFFER and JAVA COMMAND////////
/////////////////////////////////////////

void CCSERVER::cleanBuffer()
{
    for(int i=0;i<BUFFERLENGTH;i++) 
    {   
        buffer[i]= NULL;       
    }
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
    delay(1);   //Otherwise the communication is kaputt     
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



void CCSERVER::setNewCommand()
{
    _ccPacketHandler.buildPatternCommand(RECEIVERID, METAKEY, COLOR1, COLOR2); 
}          



void CCSERVER::ccSendCommand()
{
    setNewCommand(); 
    ccSendPacket();
}



