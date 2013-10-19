#include "accel.h"

//CONSTRUCTOR//

ACCEL::ACCEL(int minShakenValue, int minKickedValue)
{
   
    shakenThreshold = minShakenValue;
    kickedThreshold = minKickedValue;
}


//DESTRUCTOR//

ACCEL::~ACCEL()
{

    
                            
}


void ACCEL::resetBuffers()
{
    cleanBuffer(_X);
    cleanBuffer(_Y);
    cleanBuffer(_Z);    

}

void ACCEL::cleanBuffer(int buffer[])
{
   for(int i=0; i<ACCEL_BUFFERLENGTH ; i++)
   {
       buffer[i] = 0;
   }

}

void ACCEL::update(int index)
{

    _X[index] = analogRead(ANALOGPIN_0);
    _Y[index] = analogRead(ANALOGPIN_1);
    _Z[index] = analogRead(ANALOGPIN_2);

}

void ACCEL::setAccelDelta()

{
    findMinValues();
    findMaxValues();

    for(int i = 0; i < 3 ; i++)
    {
        _delta[i] = abs(_maxValues[i] - _minValues[i]);     
    }

}


void ACCEL::findMinValues()
{
    
    _minValues[0] = setMinValue(_X);
    _minValues[1] = setMinValue(_Y);
    _minValues[2] = setMinValue(_Z);

}


int ACCEL::setMinValue(int buffer[])
{
   
   int minValue = buffer[0];
 
   for(int i=1; i<ACCEL_BUFFERLENGTH; i++)
   {
       minValue = min(minValue, buffer[i]);
   }
   
   return minValue;

}

void ACCEL::findMaxValues()
{
    
    _maxValues[0] = setMaxValue(_X);
    _maxValues[1] = setMaxValue(_Y);
    _maxValues[2] = setMaxValue(_Z);

}


int ACCEL::setMaxValue(int buffer[])
{
    int maxValue = buffer[0];
 
    for(int i=1; i<ACCEL_BUFFERLENGTH; i++)
    {
        maxValue = max(maxValue, buffer[i]);
    }
   
    return maxValue;

}


int ACCEL::maxDelta()

{
    return max(max(_delta[0], _delta[1]), max(_delta[1], _delta[2]));
}

int ACCEL::minDelta()

{
    return min(min(_delta[0], _delta[1]), min(_delta[1], _delta[2]));
}

boolean ACCEL::wasShaken()

{
    boolean wasShaken = false;   

    if( (minDelta()> shakenThreshold) && (maxDelta() < kickedThreshold) )
    {
        wasShaken = true;
    }

    return wasShaken;     
}

boolean ACCEL::wasKicked()

{
    boolean wasKicked = false;   

    if( minDelta()>= kickedThreshold )
    {
        wasKicked = true;
    }

    return wasKicked;     
}



