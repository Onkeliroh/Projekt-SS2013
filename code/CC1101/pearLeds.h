#ifndef PEARLEDS_H

#define PEARLEDS_H

#include "Leds.h"

class PEARLEDS : public LEDS
{

	public:
                PEARLEDS();
                ~PEARLEDS(void);
                void setLedPattern(byte keyPattern, byte ColorKey1, byte ColorKey2);           
};


#endif //PEARLEDS_H
