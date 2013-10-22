#ifndef KIDNEYLEDS_H

#define KIDNEYLEDS_H

#include "Leds.h"

#define BLINKINTERVAL  50

#define KIDNEYBLINK_PERIOD        500
#define KIDNEYFADE_PERIOD         55   
#define KIDNEYRAINBOW_PERIOD      40
#define KIDNEYLEDSON_PERIOD       10000000
#define KIDNEYLEDSOFF_PERIOD      10000000
#define KIDNEYONESTRIPE_PERIOD    500
#define KIDNEYSTRIPES_PERIOD      500


//PATTERN KEYS

class KIDNEYLEDS : public LEDS
{

	public:
                KIDNEYLEDS();
                ~KIDNEYLEDS(void);
                void updateLedPattern(); 
                unsigned long getPatternPeriod(byte patternKey);
                void blinkLeds();
                void setStripes();
                void setOneStripe();                                         
};


#endif //PEARLEDS_H
