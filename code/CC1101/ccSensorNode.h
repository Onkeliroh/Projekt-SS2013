#ifndef CCSENSORNODE_H

#define CCSENSORNODE_H

#include "ccNode.h"
#include "analogComp.h"


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
                
                //REPORTS TO THE SERVER
                void reportShakeEvent();
                void reportKickEvent();
                void reportRSSI();
                void reportLowBatt();
                void sendRSSI(byte rawRSSI,byte nearNodeId);  
                void sendInRangePacket();
                
};


#endif // CCSENSORNODE_H
