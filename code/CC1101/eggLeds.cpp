#include "eggLeds.h"

byte stripeState = 0;

typedef struct
{
    byte section1[2];
    byte section2[2];
    byte section3[1]; 
    byte section4[1];  
} EGGFRONTLEDS;


EGGFRONTLEDS _eggFrontLeds =
{
    {0,1},
    {4,5},
    {2},
    {3}
};


typedef struct
{
    byte section1[2];
    byte section2[2];
    byte section3[1]; 
    byte section4[1]; 
 
} EGGREARLEDS;

EGGREARLEDS _eggRearLeds =
{
    {7,6},
    {10,11},
    {8},
    {9}
};

//CONSTRUCTOR//

EGGLEDS::EGGLEDS()

{
    Color1 = ZERO; 
    Color2 = ZERO;
    Pattern = 0;
    patternState = 0;

}


//DESTRUCTOR//
EGGLEDS::~EGGLEDS()

{

	
}


void EGGLEDS::updateLedPattern()
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
            break;
        case LEDSON:
            setOneColorForAll(Color1);                 
            break; 
        case LEDSOFF:
            setOneColorForAll(BLACK);
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

void EGGLEDS::blinkLeds()
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

void EGGLEDS::fade()
{
   
   
}


void EGGLEDS::setOneStripe()
{
    
   if(patternState > 0) 
   {
	    setOneColorForAll(Color1);
	    setSectionColor(_eggFrontLeds.section3, sizeof(_eggFrontLeds.section3), Color2);
	    setSectionColor(_eggFrontLeds.section4, sizeof(_eggFrontLeds.section4), Color2);
	    setSectionColor(_eggRearLeds.section3, sizeof(_eggRearLeds.section3), Color2);
	    setSectionColor(_eggRearLeds.section4, sizeof(_eggRearLeds.section4), Color2);
	    patternState = ZERO;
    }
    else 
    {  
            setOneColorForAll(Color1);
            setSectionColor(_eggFrontLeds.section1, sizeof(_eggFrontLeds.section1), Color2);
	    setSectionColor(_eggFrontLeds.section2, sizeof(_eggFrontLeds.section2), Color2);
            setSectionColor(_eggRearLeds.section1, sizeof(_eggRearLeds.section1), Color2);
	    setSectionColor(_eggRearLeds.section2, sizeof(_eggRearLeds.section2), Color2);
	    ++patternState;        
    }  
}

