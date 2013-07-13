#ifndef CCPACKETHANDLER_H
#define CCPACKETHANDLER_H

#include "ccpacket.h"
#include "cc1101.h"


class ccPacketHandler
{
    public:
        // default c-tor
        ccPacketHandler();
        // d-tor
        virtual ~ccPacketHandler();
        // mutex
        void setMutex(int mutex);
        int getMutex();
        void detMutex();
        // setters
        void setPacket(CCPACKET ccpacket);
        void setReceiver(byte receiver);
        void setSender(byte sender);
        void setAdminKey(byte adminkey);
        void setPackNum(byte packnum);
        // getters
        CCPACKET getPacket();
        byte getReceiver();
        byte getSender();
        byte getAdminKey();
        byte getPackNum();
    protected:
    private:
        CCPACKET _ccPacket; // length of 6 is minimum, since: 0 = receiver's address, 1 = sender's address, 2 = administration-key, 3+4 = packet_-number, 5 = payload
        int _mutex;
};

#endif // CCPACKETHANDLER_H
