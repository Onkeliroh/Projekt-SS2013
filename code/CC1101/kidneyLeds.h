#ifndef KIDNEYLEDS_H

#define KIDNEYLEDS_H

#include "Leds.h"

#define BLINKINTERVAL  50

//PATTERN KEYS

class KIDNEYLEDS : public LEDS
{

	public:
                KIDNEYLEDS();
                ~KIDNEYLEDS(void);
                void updateLedPattern();  
                void blinkLeds();
                void setStripes();
                void setOneStripe();                                         
};


#endif //PEARLEDS_H
