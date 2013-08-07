#ifndef CCPACKETHANDLER_H

#define CCPACKETHANDLER_H

#include "ccpacket.h"

#include "cc1101.h"


#define CCPACKETHANDLER_LENGTH          10  
#define CCPACKETHANDLER_MAXLENGTH       61

#define RECEIVER_ID     data[0] 
#define SENDER_ID       data[1]
#define ADMINKEY        data[2]
#define PACKNUM         data[3]
#define NEAR_NODE_RSSI  data[4]
#define NEAR_NODE_ID    data[5]

///*ADMIN KEYS*///

/*EVENTS*/
#define TURN_LEFT_EVENT        27
#define TURN_RIGHT_EVENT       28
#define SHAKE_EVENT            31

#define NEAR_NODE_EVENT        51
#define LOW_BATTERY            52

/*COMMANDS*/
#define BACKWARD_CATERPILLAR   45
#define FORWARD_CATERPILLAR    46
#define CHANGE_COLOR           47

#define ACKNOWLEDGE_REQUEST   200
#define ACKNOWLEDGE_RESPONSE  201

/*TEST CASES*/
#define TEST                  255

/*Ids*/
#define BROADCAST               0
#define SERVER_01               1



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

        void buildPacket(byte receiver, byte adminKey);

        void addToPacket(byte data);

        void buildRSSIPacket(byte rawRSSI, byte neighbourID);

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

        void setDetectedRSSI(byte rawRSSI);        

        void setNeighbourId(byte neighbourID);


        /// getters

        CCPACKET getPacket();

        byte getReceiver();

        byte getSender();

        byte getAdminKey();

        byte getPackNum();
        
        //--- RECEIVING ---//

        /// setters



        /// getters



        byte printPacket(CCPACKET ccPacket); 
        //prints the received data on screen 

        boolean hashMatches(CCPACKET ccPacket);

        void acknowledge();

        byte ccHash();

    protected:

    private:

        CCPACKET _ccPacket; /// length of 6 is minimum, since: 0 := receiver's address, 1 := sender's address, 2 := administration-key, 3,4,...,63 := payload

        byte _id; /// determines the operation-mode: 1 := server, 2,...,255 := client

        byte _hash; /// for checking the acknowledge packet

        int _buildCounter; /// used for building custom ccPackets

};



#endif // CCPACKETHANDLER_H
