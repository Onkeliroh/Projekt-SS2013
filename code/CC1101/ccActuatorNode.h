#ifndef CCACTUATORNODE_H

#define CCACTUATORNODE_H

#include "ccNode.h"
#include "LPD6803.h"

#define LEDOUTPUT 4 // The LED is wired to the Arduino Output 4 (physical panStamp pin 19)
#define CLOCKPIN  5 // 'yellow' wire
#define DATAPIN   3 // 'green' wire 


class CCACTUATORNODE: public CCNODE
{

	public:
                CCACTUATORNODE(byte id, byte twinId);
                ~CCACTUATORNODE(void);
            
                void setup();
                boolean ccReceive();
                void ccHandle();
       
                void ledStripInit();
                void caterpillarChangeColor();
                void caterpillarFw(); 
		void caterpillarBw();
		unsigned int randomColor();
		void colorWipe(uint16_t c);
		void flipRainbow();
		void rainbow();
		unsigned int Wheel(byte WheelPos);
		unsigned int Color(byte r, byte g, byte b);
          
};


#endif // CCACTUATORNODE_H
