#include "Leds.h"


LPD6803 _ledStrip = LPD6803(NUMLEDS, DATAPIN, CLOCKPIN); // Set the first variable to the NUMBER of pixels. 20 = 20 pixels in a row

//int _pillarHead = 0;
//int _pillarLength = 4;
//unsigned int _pillarColor;

uint16_t color1;
uint16_t color2;

boolean _flagRainbow = false; // true := rainbow()


//DESTRUCTOR//
LEDS::~LEDS()

{

	
}

//METHODS
void LEDS::setup()

{
    ledStripInit();    

}


void LEDS::setFirstColor(byte firstColorKey)
{
    Color1 = findColor(firstColorKey);

}

void LEDS::setSecondColor(byte secondColorKey)
{
    Color2 =  findColor(secondColorKey);

}

void LEDS::setPattern(byte patternKey)
{
    Pattern = patternKey;

}

void LEDS::setPatternState(byte state)
{
    patternState = state;

}


// Initializing the LED-strip
void LEDS::ledStripInit()
{
    _ledStrip.setCPUmax(40);
    _ledStrip.begin();
    _ledStrip.show();
   
}


void LEDS::setOneColorForAll(uint16_t color1)
{ 
  int i = 0;

  while(i < _ledStrip.numPixels())
  {
      _ledStrip.setPixelColor(i, color1);  
      ++i;  
      _ledStrip.show();      
   }
 }

void LEDS::setSectionColor(byte section[], int sectionLength, uint16_t sectionColor)
{
    for(int i=0; i< sectionLength; i++)
    {
        _ledStrip.setPixelColor(section[i], sectionColor);  
        _ledStrip.show(); 
    }
}



unsigned int LEDS::randomColor()
{
    return Color(random(0, 31), random(0, 31), random(0, 31));
}

// The colorWipe method switched all LEDs to one given color
void LEDS::colorWipe(uint16_t c)
{
  for(int i = 0; i < _ledStrip.numPixels(); ++i)
  {
    _ledStrip.setPixelColor(i, c);
    _ledStrip.show();
  }
  delay(50);
}

void LEDS::flipRainbow()
{
  if(_flagRainbow)
    _flagRainbow = false;
  else
    _flagRainbow = true;
}

// The rainbow method drives every LED through all possible colors
void LEDS::rainbow()//uint8_t wait)
{
  for (int j = 0; j < 96 * 3; ++j) // 3 cycles of all 96 colors in the wheel
  {
    for (int i = 0; i < _ledStrip.numPixels(); ++i) 
    {
      _ledStrip.setPixelColor(i, Wheel((i + j) % 96));      
    }  
    _ledStrip.show();   // write all the pixels out
  }
}

// Input a value 0 to 127 to get a color value.
// The colours are a transition r - g -b - back to r
unsigned int LEDS::Wheel(byte WheelPos)
{
  byte r, g, b;
  switch(WheelPos >> 5)
  {
    case 0:
      r = 31 - WheelPos % 32;   //Red down
      g = WheelPos % 32;      // Green up
      b = 0;                  //blue off
      break; 
    case 1:
      g = 31 - WheelPos % 32;  //green down
      b = WheelPos % 32;      //blue up
      r = 0;                  //red off
      break; 
    case 2:
      b = 31 - WheelPos % 32;  //blue down 
      r = WheelPos % 32;      //red up
      g = 0;                  //green off
      break; 
  }
  return(Color(r, g, b));
}

uint16_t LEDS::findColor(byte colorIndex)

{
    uint16_t pickedColor;
    
    switch(colorIndex)
    {
        case 0: //!
            pickedColor = Color(31, 31, 31); //white
            break; 
        case 1:  //"
            pickedColor = Color(0, 31, 0); //red (bright)
            break; 
        case 2:  //#
            pickedColor = Color(0, 15, 0); //red
            break;
        case 3:  //$
            pickedColor = Color(0, 7, 0); //red (dim)
            break;
        case 4:  //%
            pickedColor = Color(0, 0, 31); //green (bright)
            break;
        case 5:  //&
            pickedColor = Color(0, 0, 15); //green
            break;
        case 6:  //'
            pickedColor = Color(0, 0, 7);  //green (dim)
            break;
        case 7:  //(
            pickedColor = Color(31, 0, 0); //blue (bright)
            break;
        case 8:  //)
            pickedColor = Color(15, 0, 0); //blue
            break;
        case 9:  //*
            pickedColor = Color(7, 0, 0); //blue (dim)
            break; 
        case 10: // +
            pickedColor = Color(0, 31, 31);  //yellow (bright)
            break; 
        case 11: // ,
            pickedColor = Color(0, 15, 15); //yellow
            break; 
        case 12: // -
            pickedColor = Color(0, 7, 7); //yellow (dim)
            break;
        case 13: // .
            pickedColor = Color(31, 31, 0); //violet
            break;
        case 14: // /
            pickedColor = Color(0, 31, 10); //orange
            break;
        case 15: // 0
            pickedColor = Color(27, 0, 31); //turquoise
            break;
        case 16: // 1
            pickedColor = Color(0, 0, 0); //black
            break;

      }
  return pickedColor;
}

// Create a 16 bit color value from R, G, B
unsigned int LEDS::Color(byte r, byte g, byte b)
{
  //Take the lowest 5 bits of each value and append them end to end
  return(((unsigned int)g & 0x1F ) << 10 | ((unsigned int)b & 0x1F) << 5 | (unsigned int)r & 0x1F);
}
 
