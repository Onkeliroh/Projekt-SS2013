#include "ccAccel.h"


//CONSTRUCTOR//

CCACCEL::CCACCEL()

{
    _X = analogRead(ANALOGPIN_0);
    _Y = analogRead(ANALOGPIN_1);
    _Z = analogRead(ANALOGPIN_2);

    _deltaX = 0;
    _deltaY = 0;
    _deltaZ = 0;
         
}


//DESTRUCTOR//

CCACCEL::~CCACCEL()
{


}

void CCACCEL::update()
{

    _X = analogRead(ANALOGPIN_0);
    _Y = analogRead(ANALOGPIN_1);
    _Z = analogRead(ANALOGPIN_2);

}

void CCACCEL::setDelta()

{
    int prevX = _X;
    int prevY = _Y;
    int prevZ = _Z;

    update(); 
 
    _deltaX = _X - _prevX;
    _deltaY = _Y - _prevY;
    _deltaZ = _Z - _prevZ;
 
}

int CCACCEL::maxDelta()
{
    return max(max(_deltaX, _deltaY), max(_deltaY, _deltaZ));
}

int CCACCEL::minDelta()
{
    return min(min(_deltaX, _deltaY), min(_deltaY, _deltaZ));
}

boolean CCACCEL::wasShaken()
{
   boolean wasShaken = false;   
 
   setDelta();
  
   if (maxDelta() > THRESHOLD) //|| minDelta() < (_threshold * (-1)))
       wasShaken = true;

   return wasShaken;     
}

extern CCACCEL ccAccel = CCACCEL();


