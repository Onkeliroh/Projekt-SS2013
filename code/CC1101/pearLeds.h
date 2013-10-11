#ifndef PEARLEDS_H

#define PEARLEDS_H

#include "Leds.h"

#define BLINKINTERVAL  50

//PATTERN KEYS

class PEARLEDS : public LEDS
{

	public:
                PEARLEDS();
                ~PEARLEDS(void);
                void updateLedPattern();  
                void blinkLeds();
                void setStripes();
                void setOneStripe();
                void fade();
             
};


#endif //PEARLEDS_H
