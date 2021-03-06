#ifndef CCSENSORNODE_H

#define CCSENSORNODE_H

#include "ccNode.h"
#include "analogComp.h"

#define RSSI_OFFSET 74   //According to the Design Note DN505 http://www.ti.com/lit/an/swra114d/swra114d.pdf


class CCSENSORNODE: public CCNODE
{

	public:
                CCSENSORNODE(byte id, byte twinNode);
                ~CCSENSORNODE(void);

                void setup();  
                void initBattMonitor();
                
                boolean ccGetNewPacket(void);
                void ccHandle();
                boolean isPacketsSender();
                byte neighborSender();
                int calculateTrueRSSI(byte rawRSSI);
                void storeNeighborRSSI();
                boolean neighborRssiIsHigh(int neighborThreshold);
                boolean isNeighborClose(int neighborId, int neighborRssiThreshold);
                
                //REPORTS TO THE SERVER
                void reportShakeEvent();
                void reportKickEvent();
                void reportRSSI();
                void reportDetectedNearNode();
                void reportLowBatt();
                void sendRSSI(byte rawRSSI,byte nearNodeId);  
                void sendInRangePacket();
 
        private:
                int _neighborRSSI;
                
};


#endif // CCSENSORNODE_H
