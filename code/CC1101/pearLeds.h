#ifndef PEARLEDS_H

#define PEARLEDS_H

#include "Leds.h"

#define BLINKINTERVAL  50

#define PEARBLINK_PERIOD        500
#define PEARFADE_PERIOD         65
#define PEARRAINBOW_PERIOD      60
#define PEARLEDSON_PERIOD       10000000
#define PEARLEDSOFF_PERIOD      10000000
#define PEARONESTRIPE_PERIOD    500
#define PEARSTRIPES_PERIOD      500

//PATTERN KEYS

class PEARLEDS : public LEDS
{

	public:
                PEARLEDS();
                ~PEARLEDS(void);
                void updateLedPattern(); 
                unsigned long getPatternPeriod(byte patternKey);
                void blinkLeds();
                void setStripes();
                void setOneStripe();
                             
};


#endif //PEARLEDS_H
