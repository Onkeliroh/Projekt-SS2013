#include "accel.h"

//CONSTRUCTOR//

ACCEL::ACCEL()
{
    _X = analogRead(ANALOGPIN_0);
    _Y = analogRead(ANALOGPIN_1);
    _Z = analogRead(ANALOGPIN_2);

    _deltaX = _X;
    _deltaY = _Y;
    _deltaZ = _Z;    
         
}


//DESTRUCTOR//

ACCEL::~ACCEL()
{

    
                            
}


void ACCEL::update()
{

    _X = analogRead(ANALOGPIN_0);
    _Y = analogRead(ANALOGPIN_1);
    _Z = analogRead(ANALOGPIN_2);

}

void ACCEL::setDelta()

{
    int prevX = _X;
    int prevY = _Y;
    int prevZ = _Z;
        
    update(); 
 
    _deltaX = _X - prevX;  
    _deltaX = abs(_deltaX);
    _deltaY = _Y - prevY;  
    _deltaY = abs(_deltaY);
    _deltaZ = _Z - prevZ;
    _deltaZ = abs(_deltaZ);
}

int ACCEL::maxDelta()
{
    return max(max(_deltaX, _deltaY), max(_deltaY, _deltaZ));
}

int ACCEL::minDelta()
{
    return min(min(_deltaX, _deltaY), min(_deltaY, _deltaZ));
}

boolean ACCEL::wasShaken()
{
   boolean wasShaken = false;   

   setDelta();
  
   if (minDelta() > THRESHOLD) 
   {
       wasShaken = true;
   }

   return wasShaken;     
}

