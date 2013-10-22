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

void LEDS::fade()
{
    int colorScenario;

    setBlueRedGreenComp(Color1);

    colorScenario = findColorCase();
 
    adjustFadeDelta();

    switch (colorScenario)
    {
           case 1: 
               greenComp = greenComp + fadeDelta;
               break;
           case 2:
               redComp = redComp + fadeDelta;               
               break;
           case 3:  
               if(greenComp +fadeDelta*(greenComp/redComp) < 0)
               {
                   fadeDelta = FADINGDELTA;                   
               }  
               if(greenComp +fadeDelta*(greenComp/redComp) > 31)
               {
                   fadeDelta = -FADINGDELTA;                               
               }  
               greenComp = byte(greenComp + fadeDelta*(greenComp/redComp));
               redComp = redComp + fadeDelta;
               break;              
           case 4:  
               blueComp = blueComp + fadeDelta;
               break;
           case 5:   
               if(greenComp +fadeDelta*(greenComp/blueComp) < 0)
               {
                   fadeDelta = FADINGDELTA;
               }  
               if(greenComp +fadeDelta*(greenComp/blueComp) > 31)
               {
                   fadeDelta = -FADINGDELTA;
               }             
               greenComp = byte(greenComp + fadeDelta*(greenComp/blueComp));
               blueComp = blueComp + fadeDelta;
               break;
           case 6:  
               if(redComp +fadeDelta*(redComp/blueComp) < 0)
               {
                   fadeDelta = FADINGDELTA;
               }  
               if(redComp +fadeDelta*(redComp/blueComp) > 31)
               {
                   fadeDelta = -FADINGDELTA;
               } 
               redComp = byte(redComp + fadeDelta*(redComp/blueComp));
	       blueComp = blueComp + fadeDelta;
               break;
           case 7:                   
               redComp = byte(redComp + fadeDelta*(redComp/blueComp));
               greenComp = byte(greenComp + fadeDelta*(greenComp/blueComp));
               blueComp = blueComp + fadeDelta;
               break;   
           default:
               break;  
      }
 
      Color1 = Color(blueComp, redComp, greenComp); 

      setOneColorForAll(Color1); 
  
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
uint16_t LEDS::Color(byte b, byte r, byte g)
{
  //Take the lowest 5 bits of each value and append them end to end
  return(((unsigned int)r & 0x1F ) << 10 | ((unsigned int)g & 0x1F) << 5 | (unsigned int)b & 0x1F);
}
 
void LEDS::setBlueRedGreenComp(uint16_t Color)
{
    blueComp = getBlueComp(Color);
    greenComp = getGreenComp(Color);
    redComp = getRedComp(Color);
}

byte LEDS::getBlueComp(uint16_t Color)
{
    return byte(Color & 0x1F);
}

byte LEDS::getGreenComp(uint16_t Color)
{
    return byte((Color1 >> 5) & 0x1F);
}

byte LEDS::getRedComp(uint16_t Color)
{
    return byte((Color1 >> 10) & 0x1F);
}

boolean LEDS::componentNotNull(byte Component)
{

    boolean componentNotNull = false;

    if(Component > 0)
    {
        componentNotNull = true;
    } 
   
    return componentNotNull;
}

byte LEDS::highestComponent()
{
    byte highestComponent = (byte)max(max(blueComp, greenComp), max(greenComp,redComp));
   
    return highestComponent;

}

byte LEDS::lowestComponent()
{
    byte lowestComponent = (byte)min(min(blueComp, greenComp), min(greenComp,redComp));
   
    return lowestComponent;

}

int LEDS::findColorCase()
{
    int colorCase = 0;

    if(componentNotNull(blueComp))
    {
        colorCase = 4;
    }

    if(componentNotNull(redComp))
    {
        colorCase = colorCase + 2;        
    }

    if(componentNotNull(greenComp)) 
    {
        colorCase = colorCase + 1;        
    }

    return colorCase;

}


void LEDS::adjustFadeDelta()
{

    if(highestComponent() + fadeDelta > 31 )
    {
        fadeDelta = -FADINGDELTA;  //cannot increase the component beyond 31
    }
    else
    {
        if((highestComponent() == FADINGDELTA)||(highestComponent() < FADINGDELTA) )   
	{
	    fadeDelta = FADINGDELTA;  //cannot decrease the component beyond 1
	}
    }
   
}





