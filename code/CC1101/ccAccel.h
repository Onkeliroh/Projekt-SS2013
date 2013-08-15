#ifndef CCACCEL_H

#define CCACCEL_H

#include "Arduino.h"

#define ANALOGPIN_0 0
#define ANALOGPIN_1 1
#define ANALOGPIN_2 2
#define THRESHOLD 100  //before: 200


class CCACCEL
{

	public:
                ///constructor
                CCACCEL();
                ///destructor
                ~CCACCEL();
        	
                void init();
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

extern CCACCEL ccAccel;

#endif // CCACCEL_H
