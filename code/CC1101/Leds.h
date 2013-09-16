#ifndef LEDS_H

#define LEDS_H

#include "LPD6803.h"
#include <TimerOne.h>

#define LEDOUTPUT 4 // The LED is wired to the Arduino Output 4 (physical panStamp pin 19)
#define CLOCKPIN  5 // 'yellow' wire
#define DATAPIN   3 // 'green' wire 
#define NUMLEDS   50

//*PATTERNKEYS*//
#define STRIPES                44
#define BACKWARD_CATERPILLAR   45
#define FORWARD_CATERPILLAR    46
#define CHANGE_COLOR           47


class LEDS
{

	public:
                ~LEDS();
            
                void setup();
                virtual void setLedPattern(byte keyPattern, byte firstColor, byte secondColor) = 0;
                      
                void ledStripInit();
                void caterpillarChangeColor();
                void caterpillarFw(); 
		void caterpillarBw();
                void setPatternStripes(uint16_t color1, uint16_t color2);
		unsigned int randomColor();
		void colorWipe(uint16_t c);
		void flipRainbow();
		void rainbow();
		unsigned int Wheel(byte WheelPos);		
                unsigned int findColor(byte colorIndex);
                unsigned int Color(byte r, byte g, byte b);
          
};


#endif // LEDS_H
