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

    for (byte i = 0; i < _ccPacket.length; ++i) /// fill packet with zeros

    {

        _ccPacket.data[i] = 0; /// set i-th byte := 0 for each byte in the packet

    }

    setBuildCounter(0); /// build counter reset to first byte of ccPacket

}



void ccPacketHandler::testPacket(byte sender)

{

    for (byte i = 0; i < _ccPacket.length; ++i) /// fill packet with zeros

    {

        _ccPacket.data[i] = i; /// set i-th byte := i for each byte in the packet

    }

    setReceiver(SERVER_01); /// setting receiver

    setSender(sender); /// setting sender

    setAdminKey(TEST); /// setting administration key

    setHash(ccHash()); /// hashing the data and saving in member _hash

}



void ccPacketHandler::testPacket(byte receiver, byte sender)

{

    for (byte i = 0; i < _ccPacket.length; ++i) /// fill packet with zeros

    {

        _ccPacket.data[i] = i; /// set i-th byte := i for each byte in the packet

    }

    setReceiver(receiver); /// setting receiver

    setSender(sender); /// setting sender

    setAdminKey(TEST); /// setting administration key - default := 255

    setHash(ccHash()); /// hashing the data and saving in member _hash

}


void ccPacketHandler::buildPacket(byte receiver, byte sender, byte adminKey) 

{

    clearPacket(); /// clear existing ccPacket

    setReceiver(receiver); /// set given receiver

    setSender(sender); /// set sender

    setAdminKey(adminKey); /// set given administration key

    setBuildCounter(3); /// set build counter to next free byte's position

}

void ccPacketHandler::addToPacket(byte data)    //modified by Jenny

{
 
    _ccPacket.data[_buildCounter] = data;

    if (_buildCounter != 9) /// build counter has reached end of ccPacket. default is 60

    {

        ++_buildCounter; /// increase build counter and go to next free byte

    }

    else

        Serial.println("ERROR - end of ccAcknowledge() ccPacket reached.");

}

void ccPacketHandler::buildRSSIPacket(byte sender, byte rawRSSI, byte neighbourID)
{
    clearPacket(); /// clear the packet

    setReceiver(SERVER_01); 

    setSender(sender); 

    setAdminKey(NEAR_NODE_EVENT);

    setNeighbourId(neighbourID);

    setDetectedRSSI(rawRSSI);

    

    setBuildCounter(5); /// set build counter to next free byte's position    
}

void ccPacketHandler::buildPatternCommand(byte receiver, byte PatternKey, byte colorKey1, byte colorKey2)
{
    clearPacket(); /// clear the packet

    setReceiver(receiver);

    setSender(SERVER_01);
 
    setAdminKey(PatternKey);   
 
    setFirstColor(colorKey1);

    setSecondColor(colorKey2);

    setBuildCounter(5); /// set build counter to next free byte's position    

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

    _ccPacket = ccPacket; /// set packet to a whole given packet

}



void ccPacketHandler::setReceiver(byte receiver)

{

    _ccPacket.RECEIVER_ID = receiver; /// set receiver to a given receiver
    

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



void ccPacketHandler::setPackNum(byte packNum)

{

    _ccPacket.PACKNUM = packNum; 

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

    return _ccPacket; /// get the whole packet

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



byte ccPacketHandler::getPackNum()

{

    return _ccPacket.PACKNUM; /// get the packet's number

}


byte ccPacketHandler::getPacketRSSI()

{

    return _ccPacket.rssi;

}



void ccPacketHandler::printPacket()

{
    for (byte i = 0; i < _ccPacket.length; ++i)
    {
        //Serial.write(_ccPacket.data[i]);   //for debugging
        Serial.print("|");
        Serial.print(_ccPacket.data[i]);        

    }   
    Serial.println();
}


/// acknowledge

void ccPacketHandler::acknowledge()

{

    byte sender = getPacketSender(); /// save sender

    byte receiver = getPacketReceiver(); /// save receiver

    byte ulf = ccHash(); /// hash the data

    clearPacket(); /// clear the packet

    setReceiver(sender); /// set sender as receiver

    setSender(receiver); /// set receiver as sender

    setAdminKey(ACKNOWLEDGE); /// set acknowlegde key

    setPackNum(ulf); /// set the hash as data

}

