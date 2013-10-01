#ifndef ACCEL_H

#define ACCEL_H

#include "Arduino.h"

#define ANALOGPIN_0 0
#define ANALOGPIN_1 1
#define ANALOGPIN_2 2
#define THRESHOLD  10

#define HIGHVALUE 600

class ACCEL
{

	public:
                ///constructor
                ACCEL();
                ///destructor
                ~ACCEL();
        	
                void update();
                void setDelta();                
                int  maxDelta();
                int  minDelta();
                boolean wasShaken();

                int _X;
                int _Y;
                int _Z;

                int _deltaX;
                int _deltaY;
                int _deltaZ;
                                       
};


#endif // ACCEL_H