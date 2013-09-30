#include "ccPacketHandler.h"

// default c-tor
ccPacketHandler::ccPacketHandler()
{
    _ccPacket.length = 61; // maximum is 61, due to ccpacket.h's lines 33 to 53
    _mutex = 0; // 0 -> server is talking
}

// d-tor
ccPacketHandler::~ccPacketHandler()
{

}

// mutex
void ccPacketHandler::setMutex(int mutex)
{
    _mutex = mutex;
}

int ccPacketHandler::getMutex()
{
    return _mutex;
}

void ccPacketHandler::detMutex()
{
    int mutex = getMutex();
}

// setters
void ccPacketHandler::setPacket(CCPACKET ccpacket)
{
    _ccPacket = ccpacket;
}

void ccPacketHandler::setReceiver(byte receiver)
{
    _ccPacket.data[0] = receiver;
}

void ccPacketHandler::setSender(byte sender)
{
    _ccPacket.data[1] = sender;
}

void ccPacketHandler::setAdminKey(byte adminkey)
{
    _ccPacket.data[2] = adminkey;
}

void ccPacketHandler::setPackNum(byte packnum)
{
    _ccPacket.data[3] = packnum;
}

// getters
CCPACKET ccPacketHandler::getPacket()
{
    return _ccPacket;
}

byte ccPacketHandler::getReceiver()
{
    return _ccPacket.data[0];
}

byte ccPacketHandler::getSender()
{
    return _ccPacket.data[1];
}

byte ccPacketHandler::getAdminKey()
{
    return _ccPacket.data[2];
}

byte ccPacketHandler::getPackNum()
{
    return _ccPacket.data[3];
}
