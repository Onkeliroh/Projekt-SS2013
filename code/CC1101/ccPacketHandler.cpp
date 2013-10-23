#include "ccPacketHandler.h"


//--- TORS ---//

/// constructor

ccPacketHandler::ccPacketHandler()

{

    _ccPacket.length = CCPACKETHANDLER_LENGTH;
    

}



/// destructor

ccPacketHandler::~ccPacketHandler()

{



}


//--- STUFF ---//

void ccPacketHandler::clearPacket()

{

    for (byte i = 0; i < _ccPacket.length; ++i) 

    {

        _ccPacket.data[i] = 0;

    }

    setBuildCounter(0); 

}


void ccPacketHandler::buildPacket(byte receiver, byte sender, byte adminKey) 

{

    clearPacket(); 

    setReceiver(receiver); 

    setSender(sender); 

    setAdminKey(adminKey); 

    setBuildCounter(3); 

}

void ccPacketHandler::addToPacket(byte data)   

{
 
    _ccPacket.data[_buildCounter] = data;

    if (_buildCounter != 5) 

    {

        ++_buildCounter; 

    }

    else

        Serial.println("ERROR - end of ccAcknowledge() ccPacket reached.");

}

void ccPacketHandler::buildRSSIPacket(byte sender, byte rawRSSI, byte neighbourID)
{
    clearPacket(); 

    setReceiver(SERVER_01); 

    setSender(sender); 

    setAdminKey(NEAR_NODE_EVENT);

    setNeighbourId(neighbourID);

    //setDetectedRSSI(rawRSSI);
    //setBuildCounter(5); 
    

    setBuildCounter(4); 
}

void ccPacketHandler::buildPatternCommand(byte receiver, byte PatternKey, byte colorKey1, byte colorKey2)
{
    clearPacket(); 

    setReceiver(receiver);

    setSender(SERVER_01);
 
    setAdminKey(PatternKey);   
 
    setFirstColor(colorKey1);

    setSecondColor(colorKey2);

    setBuildCounter(5); 

}

void ccPacketHandler::buildRGBCommand(byte Red01, byte Blue01, byte Green01)
{
    clearPacket(); 

    setReceiver(3);

    setSender(SERVER_01); 
  
    setRed(Red01);

    setBlue(Blue01);
 
    setGreen(Green01);
     
    setBuildCounter(6); 

}



boolean ccPacketHandler::hashMatches()
{
    if(getHash() == ccHash())

    	return true;

    else

        return false;
}



/// hashing the data

byte ccPacketHandler::ccHash()

{

    byte ccHash = 0;

    for (byte i = 0; i < _ccPacket.length; ++i)

    {

        ccHash += _ccPacket.data[i];

    }

    return ccHash;

}



/// setters

void ccPacketHandler::setHash(byte ccHash)

{

    _hash = ccHash;

}



void ccPacketHandler::setBuildCounter(int counter)

{

    _buildCounter = counter;

}



/// getters

byte ccPacketHandler::getHash()

{

    return _hash;

}



int ccPacketHandler::getBuildCounter()

{

    return _buildCounter;

}


//--- SENDING ---//

void ccPacketHandler::sendPacket()

{



}



/// setters

void ccPacketHandler::setPacket(CCPACKET ccPacket)

{

    _ccPacket = ccPacket; 

}



void ccPacketHandler::setReceiver(byte receiver)

{

    _ccPacket.RECEIVER_ID = receiver; 
    

}



void ccPacketHandler::setSender(byte sender)

{

    _ccPacket.SENDER_ID = sender; 

}



void ccPacketHandler::setAdminKey(byte adminKey)

{

    _ccPacket.ADMINKEY = adminKey; 

}

void ccPacketHandler::setMetaKey(byte metaKey)

{

    _ccPacket.METAKEY = metaKey; 

}


void ccPacketHandler::setFirstColor(byte firstColor)

{

    _ccPacket.COLOR1 = firstColor; 

}

void ccPacketHandler::setSecondColor(byte secondColor)

{

    _ccPacket.COLOR2 = secondColor; 

}

void ccPacketHandler::setRed(byte colorRed)

{

    _ccPacket.RED = colorRed; 

}

void ccPacketHandler::setBlue(byte colorBlue)

{

    _ccPacket.BLUE = colorBlue; 

}

void ccPacketHandler::setGreen(byte colorGreen)

{

    _ccPacket.GREEN = colorGreen; 

}


void ccPacketHandler::setDetectedRSSI(byte rawRSSI) 

{

    _ccPacket.NEAR_NODE_RSSI = rawRSSI; 

}

void ccPacketHandler::setNeighbourId(byte neighbourID)  

{

    _ccPacket.NEAR_NODE_ID = neighbourID; /// set detected RSSI from nearby node

}




/// getters

CCPACKET ccPacketHandler::getPacket()

{

    return _ccPacket;

}



byte ccPacketHandler::getPacketReceiver()

{

    return _ccPacket.RECEIVER_ID; 

}



byte ccPacketHandler::getPacketSender()

{

    return _ccPacket.SENDER_ID; 

}



byte ccPacketHandler::getAdminKey()

{

    return _ccPacket.ADMINKEY; 

}


byte ccPacketHandler::getMetaKey()

{

     return _ccPacket.METAKEY;


}

byte ccPacketHandler::getFirstColor()

{

    return _ccPacket.COLOR1;  

}


byte ccPacketHandler::getSecondColor()

{

    return _ccPacket.COLOR2;  

}

byte ccPacketHandler::getRed()

{

    return _ccPacket.RED;  

}

byte ccPacketHandler::getBlue()

{

    return _ccPacket.BLUE;  

}

byte ccPacketHandler::getGreen()

{

    return _ccPacket.GREEN;  

}


byte ccPacketHandler::getPacketRSSI()

{

    return _ccPacket.rssi;

}



void ccPacketHandler::printPacket()

{
    
   for (byte i = 0; i < _ccPacket.length; ++i)
  {
      
       Serial.print("|"); 
      
       Serial.print(_ccPacket.data[i]);    
      
  }   
       Serial.println();
}


