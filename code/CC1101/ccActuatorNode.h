#ifndef CCACTUATORNODE_H

#define CCACTUATORNODE_H

#include "ccNode.h"

class CCACTUATORNODE: public CCNODE
{

	public:
                CCACTUATORNODE(byte id, byte twinId);
                ~CCACTUATORNODE(void);
            
                void setup();
                boolean ccGetNewPacket();
                boolean keyforLeds();
                byte getKey();
                byte getFirstColor();
                byte getSecondColor();
                void ccHandle();  
                         
};


#endif // CCACTUATORNODE_H
