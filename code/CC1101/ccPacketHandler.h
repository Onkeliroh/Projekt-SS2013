#ifndef CCPACKETHANDLER_H

#define CCPACKETHANDLER_H



#include "ccpacket.h"

#include "cc1101.h"



class ccPacketHandler

{

    public:

        //--- TORS ---//

        /// default c-tor

        ccPacketHandler();

        /// copy c-tor

        ccPacketHandler(byte id);

        /// default d-tor

        virtual ~ccPacketHandler();



        //--- STUFF ---//

        void initialize();

        void clearPacket();

        void testPacket();

        void testPacket(byte receiver);

        void fillPacket(byte data[10]); /// default is 61

        void buildPacket(byte receiver, byte adminKey);

        void addToPacket(byte data);

        /// setters

        void setId(byte id);

        void setHash(byte ccHash);

        void setBuildCounter(int counter);

        /// getters

        byte getId();

        byte getHash();

        int getBuildCounter();



        //--- SENDING ---//

        void sendPacket();

        /// setters

        void setPacket(CCPACKET ccPacket);

        void setReceiver(byte receiver);

        void setSender(byte sender);

        void setAdminKey(byte adminKey);

        void setPackNum(byte packNum);

        /// getters

        CCPACKET getPacket();

        byte getReceiver();

        byte getSender();

        byte getAdminKey();

        byte getPackNum();

        /// forwarding

        void forwardToServer(byte ccData[10]); /// default is 61

        void forwardToClient(byte ccData[10]); /// default is 61



        //--- RECEIVING ---//

        /// setters



        /// getters



        /// evaluation

        byte evaluatePacket(CCPACKET ccPacket);

        void acknowledge();

        byte ccHash();

    protected:

    private:

        CCPACKET _ccPacket; /// length of 6 is minimum, since: 0 := receiver's address, 1 := sender's address, 2 := administration-key, 3,4,...,63 := payload

        byte _id; /// determines the operation-mode: 0 := server, 1,...,255 := client

        byte _hash; /// for checking the acknowledge packet

        int _buildCounter; /// used for building custom ccPacktes

};



#endif // CCPACKETHANDLER_H
