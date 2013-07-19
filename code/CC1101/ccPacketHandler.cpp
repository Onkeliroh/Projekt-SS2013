#include "ccPacketHandler.h"

#include "ccpacket.h"

#include "cc1101.h"



//--- TORS ---//

/// default c-tor

ccPacketHandler::ccPacketHandler()

{

    _ccPacket.length = 10; /// maximum is 61, due to ccpacket.h's lines 33 to 53

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

    setReceiver(0); /// setting receiver

    setSender(_id); /// setting sender

    setAdminKey(255); /// setting administration key

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

    setAdminKey(255); /// setting administration key - default := 255

    setHash(ccHash()); /// hashing the data and saving in member _hash

}



void ccPacketHandler::fillPacket(byte data[61])

{

    clearPacket(); /// clear previous packet

    for (byte i = 0; i < sizeof(data); ++i) /// fill packet with given data

    {

        _ccPacket.data[i] = data[i]; /// set i-th byte equal to i-th byte of given data

    }

    setBuildCounter(9); /// default is 60

}



void ccPacketHandler::buildPacket(byte receiver, byte adminKey)

{

    clearPacket(); /// clear existing ccPacket

    setReceiver(receiver); /// set given receiver

    setSender(_id); /// set sender

    setAdminKey(adminKey); /// set given administration key

    setBuildCounter(3); /// set build counter to next free byte's position

}



void ccPacketHandler::addToPacket(byte data)

{

    if (_buildCounter != 9) /// build counter has reached end of ccPacket. default is 60

    {

        ++_buildCounter; /// increase build counter and go to next free byte

    }

    else

        Serial.println("ERROR - end ofccAcknowledge() ccPacket reached.");

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

    _ccPacket.data[0] = receiver; /// set receiver to a given receiver

}



void ccPacketHandler::setSender(byte sender)

{

    _ccPacket.data[1] = sender; /// set sender to a given sender

}



void ccPacketHandler::setAdminKey(byte adminKey)

{

    _ccPacket.data[2] = adminKey; /// set aministration key to a given key

}



void ccPacketHandler::setPackNum(byte packNum)

{

    _ccPacket.data[3] = packNum; /// set packet number to a given number

}



/// getters

CCPACKET ccPacketHandler::getPacket()

{

    return _ccPacket; /// get the whole packet

}



byte ccPacketHandler::getReceiver()

{

    return _ccPacket.data[0]; /// get the receiver's id

}



byte ccPacketHandler::getSender()

{

    return _ccPacket.data[1]; /// get the sender's id

}



byte ccPacketHandler::getAdminKey()

{

    return _ccPacket.data[2]; /// get the administration key

}



byte ccPacketHandler::getPackNum()

{

    return _ccPacket.data[3]; /// get the packet's number

}



/// forwarding

void ccPacketHandler::forwardToServer(byte data[61]) /// default size is 61

{

    fillPacket(data); /// fill packet with given data

    setReceiver(0); /// set server as receiver

    setSender(_id); /// set own id as sender

    sendPacket(); /// send the packet

}



void ccPacketHandler::forwardToClient(byte data[61]) /// default size is 61

{

    fillPacket(data); /// fill the packet with given data

    setSender(0); /// set server as sender

    sendPacket(); /// send the packet

}







//--- RECEIVING ---//

/// setters



/// getters



/// evaluation

byte ccPacketHandler::evaluatePacket(CCPACKET ccPacket)

{

    setPacket(ccPacket);

    Serial.print("IN: ");

    for (byte i = 0; i < _ccPacket.length; ++i)

    {

        Serial.print(_ccPacket.data[i]);

        Serial.print("|");

    }

    Serial.println("");



    switch (getAdminKey())

    {

        case 27: /// Wheel: "turn_left"-event

            if (_id == 0) /// only readable for server

                return 27;

            else

                return 0;

            break;

        case 28: /// Wheel: "turn_right"-event

            if (_id == 0) /// only readable for server

                return 28;

            else

                return 0;

            break;

        case 31: /// Accel: "shake"

            if (_id == 0) /// only readable for server

                return 31;

            else

                return 0;

            break;

        case 45: /// LED-strip: "move forward"

            if (_id != 0) /// only readable for clients

                return 45;

            else

                return 0;

            break;

        case 46: /// LED-strip: "move backward"

            if (_id != 0) /// only readable for clients

                return 46;

            else

                return 0;

            break;

        case 47: /// LED-strip: "change color"

            if (_id != 0) /// only readable for clients

                return 47;

            else

                return 0;

            break;

        case 200: /// acknowledge packet received

            if (getHash() == ccHash()) /// saved last hash is equal to acknowledged key

            {

                return 201;

            }

            else

            {

                return 202;

            }

            break;

        case 255: /// test packet received

            /// do something here

            return 255;

            break;

        default: /// unknown adminKey

            Serial.print("Unknown Administration Key: ");

            Serial.println(getAdminKey());

            clearPacket();

            return 0;

            break;

    }

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

    setAdminKey(200); /// set acknowlegde key

    setPackNum(ulf); /// set the hash as data

}
