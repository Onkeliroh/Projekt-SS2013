#ifndef LEDS_H

#define LEDS_H

#include "LPD6803.h"
#include <TimerOne.h>

#define LEDOUTPUT 4 // The LED is wired to the Arduino Output 4 (physical panStamp pin 19)
#define CLOCKPIN  5 // 'yellow' wire
#define DATAPIN   3 // 'green' wire 
#define NUMLEDS   50

//*UNIVERSAL PATTERNKEYS*//
#define BLINK        0
#define FADE         1
#define RAINBOW      2
#define LEDSON       3
#define LEDSOFF      4
#define ONESTRIPE    5
#define STRIPES      6


//STATES
#define ZERO         0
#define ONE          1
#define TWO          2
#define THREE        3
#define FOUR         4


//COLORKEYS
#define BLACK        0



class LEDS
{

	public:
                ~LEDS();
            
                void setup();
		void setFirstColor(byte firstColorKey);
		void setSecondColor(byte secondColorKey);
		void setPattern(byte patternKey);
		void setPatternState(byte state);
		
                virtual void updateLedPattern() = 0;
                      
                void ledStripInit();
                void setOneColorForAll(uint16_t color1);
                void setSectionColor(byte section[], int sectionLength, uint16_t sectionColor);
		unsigned int randomColor();
		void colorWipe(uint16_t c);
		void flipRainbow();
		void rainbow();
		unsigned int Wheel(byte WheelPos);		
                uint16_t findColor(byte colorIndex);
                unsigned int Color(byte r, byte g, byte b);

        protected:

                uint16_t Color1; 
                uint16_t Color2;
                byte Pattern;
                byte patternState;
                byte fadeState;
          
};


#endif // LEDS_H
