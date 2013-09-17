#include "pearLeds.h"

uint16_t Color1;
uint16_t Color2;


//CONSTRUCTOR//

PEARLEDS::PEARLEDS()

{

}


//DESTRUCTOR//
PEARLEDS::~PEARLEDS()

{

	
}


void PEARLEDS::setLedPattern(byte keyPattern, byte ColorKey1, byte ColorKey2)
{

    switch (keyPattern)
    {
        case CHANGE_COLOR: 
            colorWipe(randomColor());            
            break;
        case STRIPES:
            Color1 = findColor(ColorKey1);
            Color2 = findColor(ColorKey2);
            setPatternStripes(Color1,Color2);    
            break; 
        default: // unknown packet received
            Serial.print("ERROR");
            break; 
    }
}


//void PEARLEDS::setColorA1(unsigned int color)
//{
  //  for(int i = 0; i < _ledStrip.numPixels(); ++i)
    //{
      //  _ledStrip.setPixelColor(i, c);
    //}
    //_ledStrip.show();   
//}    


