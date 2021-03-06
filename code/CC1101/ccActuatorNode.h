#ifndef CCACTUATORNODE_H

#define CCACTUATORNODE_H

#include "ccNode.h"

#define MAXKEYPATTERN 25
#define TENSECONDS 10000000   //10000000 microseconds

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
		byte getRed();
		byte getBlue();
		byte getGreen();
                void ccHandle();  
                         
};


#endif // CCACTUATORNODE_H
