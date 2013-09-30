#include "pearLeds.h"

uint16_t Color1 = 0;
uint16_t Color2 = 0;

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

FRONTLEDS *pfrontLeds = &_frontLeds; 

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

REARLEDS *prearLeds = &_rearLeds; 

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

//void PEARLEDS::turnOnFrontSection(FRONTLEDS *p, byte sectionNumber)
//{




//}

//void PEARLEDS::turnOnFrontSection(FRONTLEDS *p, byte sectionNumber)
//{




//}


