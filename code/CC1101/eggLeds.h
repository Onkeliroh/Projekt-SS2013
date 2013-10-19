#ifndef EGGLEDS_H

#define EGGLEDS_H

#include "Leds.h"

#define BLINKINTERVAL  50

//PATTERN KEYS

class EGGLEDS : public LEDS
{

	public:
                EGGLEDS();
                ~EGGLEDS(void);
                void updateLedPattern();  
                void blinkLeds();
                void setOneStripe();
                void fade();
             
};


#endif //EGGLEDS_H
