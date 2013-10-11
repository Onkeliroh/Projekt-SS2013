#ifndef ACCEL_H

#define ACCEL_H

#include "Arduino.h"

#define ANALOGPIN_0 0
#define ANALOGPIN_1 1
#define ANALOGPIN_2 2
#define THRESHOLD  10

#define ACCEL_BUFFERLENGTH 20

#define HIGHVALUE 600

class ACCEL
{

	public:
                ///constructor
                ACCEL(int minShakenValue, int minKickedValue);
                ///destructor
                ~ACCEL();
                
                void resetBuffers();
                void cleanBuffer(int buffer[]);
                void update(int index);
                void setAccelDelta();
                void findMinValues();
                int  setMinValue(int buffer[]);
                void findMaxValues();
                int setMaxValue(int buffer[]);

                int  maxDelta();
                int  minDelta();
                boolean wasShaken();
                boolean wasKicked();

        private:
                int _X[ACCEL_BUFFERLENGTH];
                int _Y[ACCEL_BUFFERLENGTH];
                int _Z[ACCEL_BUFFERLENGTH];

                int _minValues[3];
                int _maxValues[3];

                int _delta[3];

                int shakenThreshold;
                int kickedThreshold;

//                int _deltaX[ACCEL_BUFFERLENGTH];
//		int _deltaY[ACCEL_BUFFERLENGTH];
//		int _deltaZ[ACCEL_BUFFERLENGTH];     
                                                      
};


#endif // ACCEL_H
