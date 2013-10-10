#include "pearLeds.h"

uint16_t Color1 = 0;
uint16_t Color2 = 0;
byte stripeState = 0;

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


}


//DESTRUCTOR//
PEARLEDS::~PEARLEDS()

{

	
}


void PEARLEDS::setLedPattern(byte keyPattern, byte ColorKey1, byte ColorKey2)
{

    switch (keyPattern)
    {        
        case BLINK:
            Color1 = findColor(ColorKey1);
            Color2 = findColor(ColorKey2);
            setPatternStripes(Color1,Color2);                    
            break;
        case FADE:            
            break;
        case RAINBOW:
            rainbow();
            break;
        case LEDSON:
            Color1 = findColor(ColorKey1);
            Color2 = findColor(ColorKey2);
            setPatternStripes(Color1,Color2);                  
            break; 
        case LEDSOFF:
            colorWipe(BLACK);                 
            break;           
        default: // unknown packet received
            Serial.print("ERROR");
            break; 
    }
}

void PEARLEDS::setStripes(uint16_t color)
{
    switch(stripeState)
    {
        case ZERO:
            setColors(BLACK, BLACK, BLACK);
            setSectionColor(_frontLeds.section1, sizeof(_frontLeds.section1), color);
	    setSectionColor(_frontLeds.section2, sizeof(_frontLeds.section2), color);
	    setSectionColor(_frontLeds.section5, sizeof(_frontLeds.section5), color);
	    setSectionColor(_frontLeds.section6, sizeof(_frontLeds.section6), color);
	    setSectionColor(_frontLeds.section7, sizeof(_frontLeds.section7), color);	    
	    setSectionColor(_rearLeds.section1, sizeof(_rearLeds.section1), color);
	    setSectionColor(_rearLeds.section2, sizeof(_rearLeds.section2), color);
	    setSectionColor(_rearLeds.section5, sizeof(_rearLeds.section5), color);
	    setSectionColor(_rearLeds.section6, sizeof(_rearLeds.section6), color);
	    setSectionColor(_rearLeds.section7, sizeof(_rearLeds.section7), color);
	    ++stripeState;
            break;
       	case ONE:
            setColors(BLACK, BLACK, BLACK);
	    setSectionColor(_frontLeds.section3, sizeof(_frontLeds.section3), color);
	    setSectionColor(_frontLeds.section4, sizeof(_frontLeds.section4), color);
	    setSectionColor(_frontLeds.section8, sizeof(_frontLeds.section8), color);
	    setSectionColor(_frontLeds.section9, sizeof(_frontLeds.section9), color);
	    setSectionColor(_rearLeds.section3, sizeof(_rearLeds.section3), color);
	    setSectionColor(_rearLeds.section4, sizeof(_rearLeds.section4), color);
	    setSectionColor(_rearLeds.section8, sizeof(_rearLeds.section8), color);
	    setSectionColor(_rearLeds.section9, sizeof(_rearLeds.section9), color);
	    stripeState = ZERO;
            break;
    }  
}



void PEARLEDS::setOneStripe(uint16_t color)
{
    switch(stripeState)
    {
        case ZERO:
	    setColors(BLACK, BLACK, BLACK);
	    setSectionColor(_frontLeds.section1, sizeof(_frontLeds.section1), color);
	    setSectionColor(_rearLeds.section1, sizeof(_rearLeds.section1), color);
            setSectionColor(_frontLeds.section2, sizeof(_frontLeds.section2), color);
	    setSectionColor(_rearLeds.section2, sizeof(_rearLeds.section2), color);
            ++stripeState;
            break;       
       case ONE:
            setColors(BLACK, BLACK, BLACK);
            setSectionColor(_frontLeds.section3, sizeof(_frontLeds.section3), color);
	    setSectionColor(_rearLeds.section3, sizeof(_rearLeds.section3), color);
            setSectionColor(_frontLeds.section4, sizeof(_frontLeds.section4), color);
	    setSectionColor(_rearLeds.section4, sizeof(_rearLeds.section4), color);
            ++stripeState;
            break;   
        case TWO:
            setColors(BLACK, BLACK, BLACK);
            setSectionColor(_frontLeds.section5, sizeof(_frontLeds.section5), color);
	    setSectionColor(_rearLeds.section5, sizeof(_rearLeds.section5), color);
            setSectionColor(_frontLeds.section6, sizeof(_frontLeds.section6), color);
	    setSectionColor(_rearLeds.section6, sizeof(_rearLeds.section6), color);
            setSectionColor(_frontLeds.section7, sizeof(_frontLeds.section7), color);
	    setSectionColor(_rearLeds.section7, sizeof(_rearLeds.section7), color);
            ++stripeState; 
            break;       
        case THREE:
	    setColors(BLACK, BLACK, BLACK);            
            setSectionColor(_frontLeds.section8, sizeof(_frontLeds.section8), color);
	    setSectionColor(_rearLeds.section8, sizeof(_rearLeds.section8), color);
            setSectionColor(_frontLeds.section9, sizeof(_frontLeds.section9), color);
	    setSectionColor(_rearLeds.section9, sizeof(_rearLeds.section9), color);
            stripeState = ZERO;  
            break; 
     }
}


