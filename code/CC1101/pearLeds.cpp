#include "pearLeds.h"


typedef struct
{
    byte section1[3];
    byte section2[3];
    byte section3[4]; 
    byte section4[4]; 
    byte section5[3]; 
    byte section6[4]; 
    byte section7[3]; 
    byte section8[2]; 
    byte section9[3];  
 
} FRONTLEDS;


FRONTLEDS _frontLeds =
{
    {4,5,6},
    {7,8,9},
    {0,1,2,3},
    {10,11,12,13},
    {16,17,18},
    {14,15,19,20},
    {22,23,21},
    {27,28},
    {24,25,26}
};


typedef struct
{
    byte section1[2];
    byte section2[1];
    byte section3[3]; 
    byte section4[3]; 
    byte section5[2]; 
    byte section6[4]; 
    byte section7[2]; 
    byte section8[2]; 
    byte section9[2]; 
 
} REARLEDS;

REARLEDS _rearLeds =
{
    {37,38},
    {39},
    {34,35,36},
    {40,41,42},
    {32,33},
    {30,31,43,44},
    {45,46},
    {29,49},
    {47,48}
};

//CONSTRUCTOR//

PEARLEDS::PEARLEDS()

{
    Color1 = ZERO; 
    Color2 = ZERO;
    Pattern = 0;
    patternState = 0;
    blueComp = 0;    
    greenComp = 0;
    redComp = 0;
    fadeDelta = FADINGDELTA;
    firstRainbowIndex = 0;
    secondRainbowIndex = 0;

}


//DESTRUCTOR//
PEARLEDS::~PEARLEDS()

{

	
}


void PEARLEDS::updateLedPattern()
{

    switch (Pattern)
    {        
        case BLINK:            
            blinkLeds();                
            break;
        case FADE:
            fade();                      
            break;
        case RAINBOW:
            rainbow();
            secondRainbowIndex = random(0, 96*3); 
            break;
        case LEDSON:
            setOneColorForAll(Color1);                 
            break; 
        case LEDSOFF:
            setOneColorForAll(BLACK);
            break;
        case ONESTRIPE:
            setOneStripe();     
            break;
        case STRIPES:
            setStripes();     
            break;             
        default:            
            break; 
    }
}

unsigned long PEARLEDS::getPatternPeriod(byte patternKey)
{
  unsigned long patternPeriod;
 
  switch(patternKey)
  {
    case BLINK:
      patternPeriod = PEARBLINK_PERIOD;
      break; 
    case FADE:
      patternPeriod = PEARFADE_PERIOD;
      break; 
    case RAINBOW:
      patternPeriod = PEARRAINBOW_PERIOD;
      break;      
    case LEDSON:
      patternPeriod = PEARLEDSON_PERIOD;
      break; 
    case LEDSOFF:
      patternPeriod = PEARLEDSOFF_PERIOD;
      break;
    case ONESTRIPE:
      patternPeriod = PEARONESTRIPE_PERIOD;
      break;
    case STRIPES:
      patternPeriod = PEARSTRIPES_PERIOD;
      break;
    default:
      patternPeriod = 0;
      break;
   }
 
   return patternPeriod;
}


void PEARLEDS::blinkLeds()
{
     switch(patternState)
    {
        case ZERO:
            setOneColorForAll(Color1);
	    ++patternState;
            break;       
        case ONE:
            setOneColorForAll(BLACK);
            ++patternState;
            break;   
        case TWO:
            setOneColorForAll(Color2);
            ++patternState; 
            break;       
        case THREE:
            setOneColorForAll(BLACK);       
            patternState = ZERO;  
            break; 
     }
}


void PEARLEDS::setStripes()
{
    
   if(patternState > 0) 
   {
	    setOneColorForAll(Color1);
	    setSectionColor(_frontLeds.section3, sizeof(_frontLeds.section3), Color2);
	    setSectionColor(_frontLeds.section4, sizeof(_frontLeds.section4), Color2);
	    setSectionColor(_frontLeds.section8, sizeof(_frontLeds.section8), Color2);
	    setSectionColor(_frontLeds.section9, sizeof(_frontLeds.section9), Color2);
	    setSectionColor(_rearLeds.section3, sizeof(_rearLeds.section3), Color2);
	    setSectionColor(_rearLeds.section4, sizeof(_rearLeds.section4), Color2);
	    setSectionColor(_rearLeds.section8, sizeof(_rearLeds.section8), Color2);
	    setSectionColor(_rearLeds.section9, sizeof(_rearLeds.section9), Color2);
	    patternState = ZERO;
    }
    else 
    {  
            setOneColorForAll(Color1);
            setSectionColor(_frontLeds.section1, sizeof(_frontLeds.section1), Color2);
	    setSectionColor(_frontLeds.section2, sizeof(_frontLeds.section2), Color2);
	    setSectionColor(_frontLeds.section5, sizeof(_frontLeds.section5), Color2);
	    setSectionColor(_frontLeds.section6, sizeof(_frontLeds.section6), Color2);
	    setSectionColor(_frontLeds.section7, sizeof(_frontLeds.section7), Color2);	    
	    setSectionColor(_rearLeds.section1, sizeof(_rearLeds.section1), Color2);
	    setSectionColor(_rearLeds.section2, sizeof(_rearLeds.section2), Color2);
	    setSectionColor(_rearLeds.section5, sizeof(_rearLeds.section5), Color2);
	    setSectionColor(_rearLeds.section6, sizeof(_rearLeds.section6), Color2);
	    setSectionColor(_rearLeds.section7, sizeof(_rearLeds.section7), Color2);
	    ++patternState;        
    }  
}



void PEARLEDS::setOneStripe()
{
    switch(patternState)
    {
        case ZERO:
	    setOneColorForAll(Color1);
	    setSectionColor(_frontLeds.section1, sizeof(_frontLeds.section1), Color2);
	    setSectionColor(_rearLeds.section1, sizeof(_rearLeds.section1), Color2);
            setSectionColor(_frontLeds.section2, sizeof(_frontLeds.section2), Color2);
	    setSectionColor(_rearLeds.section2, sizeof(_rearLeds.section2), Color2);
            ++patternState;
            break;       
       case ONE:
            setOneColorForAll(Color1);
            setSectionColor(_frontLeds.section3, sizeof(_frontLeds.section3), Color2);
	    setSectionColor(_rearLeds.section3, sizeof(_rearLeds.section3), Color2);
            setSectionColor(_frontLeds.section4, sizeof(_frontLeds.section4), Color2);
	    setSectionColor(_rearLeds.section4, sizeof(_rearLeds.section4), Color2);
            ++patternState;
            break;   
        case TWO:
            setOneColorForAll(Color1);
            setSectionColor(_frontLeds.section5, sizeof(_frontLeds.section5), Color2);
	    setSectionColor(_rearLeds.section5, sizeof(_rearLeds.section5), Color2);
            setSectionColor(_frontLeds.section6, sizeof(_frontLeds.section6), Color2);
	    setSectionColor(_rearLeds.section6, sizeof(_rearLeds.section6), Color2);
            setSectionColor(_frontLeds.section7, sizeof(_frontLeds.section7), Color2);
	    setSectionColor(_rearLeds.section7, sizeof(_rearLeds.section7), Color2);
            ++patternState; 
            break;       
        case THREE:
	    setOneColorForAll(Color1);           
            setSectionColor(_frontLeds.section8, sizeof(_frontLeds.section8), Color2);
	    setSectionColor(_rearLeds.section8, sizeof(_rearLeds.section8), Color2);
            setSectionColor(_frontLeds.section9, sizeof(_frontLeds.section9), Color2);
	    setSectionColor(_rearLeds.section9, sizeof(_rearLeds.section9), Color2);
            patternState = ZERO;  
            break; 
     }
}


