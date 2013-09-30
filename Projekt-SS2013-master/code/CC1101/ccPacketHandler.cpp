#include "ccPacketHandler.h"

#include "ccpacket.h"

#include "cc1101.h"



//--- TORS ---//

/// default c-tor

ccPacketHandler::ccPacketHandler()

{

    _ccPacket.length = CCPACKETHANDLER_LENGTH; 

    _id = 255; /// last possible id, since id-value is a byte

}



/// copy c-tor

ccPacketHandler::ccPacketHandler(byte id)

{

    _ccPacket.length = 10; /// maximum is 61, due to ccpacket.h's lines 33 to 53

    _id = id; /// set id to given id

}



/// default d-tor

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



void ccPacketHandler::testPacket()

{

    for (byte i = 0; i < _ccPacket.length; ++i) /// fill packet with zeros

    {

        _ccPacket.data[i] = i; /// set i-th byte := i for each byte in the packet

    }

    setReceiver(SERVER_01); /// setting receiver

    setSender(_id); /// setting sender

    setAdminKey(TEST); /// setting administration key

    setHash(ccHash()); /// hashing the data and saving in member _hash

}



void ccPacketHandler::testPacket(byte receiver)

{

    for (byte i = 0; i < _ccPacket.length; ++i) /// fill packet with zeros

    {

        _ccPacket.data[i] = i; /// set i-th byte := i for each byte in the packet

    }

    setReceiver(receiver); /// setting receiver

    setSender(_id); /// setting sender

    setAdminKey(TEST); /// setting administration key - default := 255

    setHash(ccHash()); /// hashing the data and saving in member _hash

}


void ccPacketHandler::buildPacket(byte receiver, byte adminKey) 

{

    clearPacket(); /// clear existing ccPacket

    setReceiver(receiver); /// set given receiver

    setSender(_id); /// set sender

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

        Serial.println("ERROR - end ofccAcknowledge() ccPacket reached.");

}

void ccPacketHandler::buildRSSIPacket(byte rawRSSI, byte neighbourID)
{
    clearPacket(); /// clear the packet

    setReceiver(SERVER_01); 

    setSender(_id); 

    setAdminKey(NEAR_NODE_EVENT);

    setDetectedRSSI(rawRSSI);

    setNeighbourId(neighbourID);

    setBuildCounter(5); /// set build counter to next free byte's position    
}

boolean ccPacketHandler::hashMatches(CCPACKET ccPacket)
{
    setPacket(ccPacket);

    if(getHash() == ccHash())

    	return true;

    else

        return false;
}



/// hashing the data

byte ccPacketHandler::ccHash()

{

    byte ccHash = 0;

    for (byte i = 3; i < _ccPacket.length; ++i)

    {

        ccHash += _ccPacket.data[i];

    }

    return ccHash;

}



/// setters

void ccPacketHandler::setId(byte id)

{

    _id = id;

}



void ccPacketHandler::setHash(byte ccHash)

{

    _hash = ccHash;

}



void ccPacketHandler::setBuildCounter(int counter)

{

    _buildCounter = counter;

}



/// getters

byte ccPacketHandler::getId()

{

    return _id;

}



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



byte ccPacketHandler::getReceiver()

{

    return _ccPacket.RECEIVER_ID; 

}



byte ccPacketHandler::getSender()

{

    return _ccPacket.SENDER_ID; 

}



byte ccPacketHandler::getAdminKey()

{

    return _ccPacket.ADMINKEY; 

}



byte ccPacketHandler::getPackNum()

{

    return _ccPacket.PACKNUM; /// get the packet's number

}

//--- RECEIVING ---//

/// setters



/// getters



byte ccPacketHandler::printPacket(CCPACKET ccPacket)

{
    setPacket(ccPacket);

    for (byte i = 0; i < _ccPacket.length; ++i)
    {

        Serial.print(_ccPacket.data[i]);

        Serial.print("|");

    }

    Serial.println("");

}


/// acknowledge

void ccPacketHandler::acknowledge()

{

    byte sender = getSender(); /// save sender

    byte receiver = getReceiver(); /// save receiver

    byte ulf = ccHash(); /// hash the data

    clearPacket(); /// clear the packet

    setReceiver(sender); /// set sender as receiver

    setSender(receiver); /// set receiver as sender

    setAdminKey(ACKNOWLEDGE_REQUEST); /// set acknowlegde key

    setPackNum(ulf); /// set the hash as data

}

