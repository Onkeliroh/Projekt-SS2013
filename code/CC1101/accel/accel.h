#ifndef ACCEL_H

#define ACCEL_H

#include "Arduino.h"

#define ANALOGPIN_0 0
#define ANALOGPIN_1 1
#define ANALOGPIN_2 2

#define ACCEL_CHECK_PERIOD 5  //5 ms equals 0.005 secs and the sample frequency is 200 Hz
                              //This is 4 times the Bandwith of the ADXL335 mounted in the GY-61 board which is 50Hz
                              //The sample-rate must be at least 2 times the highest frequency component
                              //of the original signal to avoid aliasing. See http://en.wikipedia.org/wiki/Aliasing

#define ACCEL_BUFFERLENGTH 100  //100 samples are taken and the time elapsed between each reading is 0.005s; therefore, each
                               // 0.5 seconds there's an update in the accelerometer "status"

class ACCEL
{

	public:
                ///constructor
                ACCEL(int minShakenValue, int minKickedValue);
                ///destructor
                ~ACCEL();
                
                void resetBuffers();
                void cleanBuffer(int buffer[]);
                void readAccel();
		boolean bufferIsFull();
		void resetBufferIndex();
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
                int bufferIndex;
     
};


#endif // ACCEL_H
