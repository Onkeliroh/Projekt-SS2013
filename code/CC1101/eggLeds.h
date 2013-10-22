#ifndef EGGLEDS_H

#define EGGLEDS_H

#include "Leds.h"

#define BLINKINTERVAL  50

#define EGGBLINK_PERIOD        500
#define EGGFADE_PERIOD         55
#define EGGRAINBOW_PERIOD      25
#define EGGLEDSON_PERIOD       10000000
#define EGGLEDSOFF_PERIOD      10000000
#define EGGONESTRIPE_PERIOD    500
#define EGGSTRIPES_PERIOD      500



//PATTERN KEYS

class EGGLEDS : public LEDS
{

	public:
                EGGLEDS();
                ~EGGLEDS(void);
                void updateLedPattern();
                unsigned long getPatternPeriod(byte patternKey);
                void blinkLeds();
                void setOneStripe();
                             
};


#endif //EGGLEDS_H
