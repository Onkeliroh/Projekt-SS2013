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
                
                boolean ccReceive();
                void ccHandle();
                boolean bubbleIsSender();
                
                          
                //REPORTS TO THE SERVER
                void reportAccelEvent();
                void reportRSSI();
                void reportLowBatt();
                void sendRSSI(byte rawRSSI,byte nearNodeId);    
                
};


#endif // CCSENSORNODE_H
