#include "kidneyLeds.h"

typedef struct
{
    byte section1[2];
    byte section2[3];
    byte section3[2]; 
    byte section4[2]; 
    byte section5[4]; 
    byte section6[3]; 
    byte section7[2]; 
    byte section8[2]; 
   
} FRONTLEDS_KIDNEY;


FRONTLEDS_KIDNEY _frontLedsKidney =
{
    {5,6},
    {2,3,4},
    {0,1},
    {7,8},
    {9,10,11,12},
    {17,18,19},
    {13,14},
    {15,16}
};

 
typedef struct
{
    byte section1[2];
    byte section2[2];
    byte section3[2]; 
    byte section4[2]; 
    byte section5[4]; 
    byte section6[2]; 
    byte section7[2]; 
    byte section8[2]; 
   
} REARLEDS_KIDNEY;


REARLEDS_KIDNEY _rearLedsKidney =
{
    {32,33},
    {34,35},
    {36,37},
    {30,31},
    {26,27,28,29},
    {20,21},
    {24,25},
    {23,22}
};


//CONSTRUCTOR//

KIDNEYLEDS::KIDNEYLEDS()

{
    Color1 = ZERO; 
    Color2 = ZERO;
    Pattern = 0;
    patternState = 0;       

}


//DESTRUCTOR//
KIDNEYLEDS::~KIDNEYLEDS()

{
   
}

void KIDNEYLEDS::updateLedPattern()
{
    switch (Pattern)
    {        
        case BLINK:
            blinkLeds();                
            break;
        case FADE:            
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

void KIDNEYLEDS::blinkLeds()
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

void KIDNEYLEDS::setStripes()
{
    
   if(patternState > 0) 
   {
	    setOneColorForAll(Color1);
	    setSectionColor(_frontLedsKidney.section1, sizeof(_frontLedsKidney.section1), Color2);
	    setSectionColor(_frontLedsKidney.section2, sizeof(_frontLedsKidney.section2), Color2);
	    setSectionColor(_frontLedsKidney.section3, sizeof(_frontLedsKidney.section3), Color2);
            setSectionColor(_frontLedsKidney.section7, sizeof(_frontLedsKidney.section7), Color2);
	    setSectionColor(_frontLedsKidney.section8, sizeof(_frontLedsKidney.section8), Color2);
	    setSectionColor(_rearLedsKidney.section1, sizeof(_rearLedsKidney.section1), Color2);
	    setSectionColor(_rearLedsKidney.section2, sizeof(_rearLedsKidney.section2), Color2);
	    setSectionColor(_rearLedsKidney.section3, sizeof(_rearLedsKidney.section3), Color2);
            setSectionColor(_rearLedsKidney.section7, sizeof(_rearLedsKidney.section7), Color2);
	    setSectionColor(_rearLedsKidney.section8, sizeof(_rearLedsKidney.section8), Color2);
	    patternState = ZERO;
    }
    else 
    {  
            setOneColorForAll(Color1);
            setSectionColor(_frontLedsKidney.section4, sizeof(_frontLedsKidney.section4), Color2);
	    setSectionColor(_frontLedsKidney.section5, sizeof(_frontLedsKidney.section5), Color2);
	    setSectionColor(_frontLedsKidney.section6, sizeof(_frontLedsKidney.section6), Color2);
	    setSectionColor(_rearLedsKidney.section4, sizeof(_rearLedsKidney.section4), Color2);
	    setSectionColor(_rearLedsKidney.section5, sizeof(_rearLedsKidney.section5), Color2);
	    setSectionColor(_rearLedsKidney.section6, sizeof(_rearLedsKidney.section6), Color2);
	    ++patternState;        
    }  
}



void KIDNEYLEDS::setOneStripe()
{
    switch(patternState)
    {
        case ZERO:
	    setOneColorForAll(Color1);
	    setSectionColor(_frontLedsKidney.section1, sizeof(_frontLedsKidney.section1), Color2);
	    setSectionColor(_rearLedsKidney.section1, sizeof(_rearLedsKidney.section1), Color2);
            setSectionColor(_frontLedsKidney.section2, sizeof(_frontLedsKidney.section2), Color2);
	    setSectionColor(_rearLedsKidney.section2, sizeof(_rearLedsKidney.section2), Color2);
            setSectionColor(_frontLedsKidney.section3, sizeof(_frontLedsKidney.section3), Color2);
	    setSectionColor(_rearLedsKidney.section3, sizeof(_rearLedsKidney.section3), Color2);
            ++patternState;
            break;       
       case ONE:
            setOneColorForAll(Color1);
            setSectionColor(_frontLedsKidney.section4, sizeof(_frontLedsKidney.section4), Color2);
	    setSectionColor(_rearLedsKidney.section4, sizeof(_rearLedsKidney.section4), Color2);
            setSectionColor(_frontLedsKidney.section5, sizeof(_frontLedsKidney.section5), Color2);
	    setSectionColor(_rearLedsKidney.section5, sizeof(_rearLedsKidney.section5), Color2);
            setSectionColor(_frontLedsKidney.section6, sizeof(_frontLedsKidney.section6), Color2);
	    setSectionColor(_rearLedsKidney.section6, sizeof(_rearLedsKidney.section6), Color2);
            ++patternState;
            break;   
        case TWO:
            setOneColorForAll(Color1);
            setSectionColor(_frontLedsKidney.section7, sizeof(_frontLedsKidney.section7), Color2);
	    setSectionColor(_rearLedsKidney.section7, sizeof(_rearLedsKidney.section7), Color2);
            setSectionColor(_frontLedsKidney.section8, sizeof(_frontLedsKidney.section8), Color2);
	    setSectionColor(_rearLedsKidney.section8, sizeof(_rearLedsKidney.section8), Color2);
            patternState = ZERO;
            break;   
        default:
            setOneColorForAll(Color1);
	    setSectionColor(_frontLedsKidney.section1, sizeof(_frontLedsKidney.section1), Color2);
	    setSectionColor(_rearLedsKidney.section1, sizeof(_rearLedsKidney.section1), Color2);
            setSectionColor(_frontLedsKidney.section2, sizeof(_frontLedsKidney.section2), Color2);
	    setSectionColor(_rearLedsKidney.section2, sizeof(_rearLedsKidney.section2), Color2);
            setSectionColor(_frontLedsKidney.section3, sizeof(_frontLedsKidney.section3), Color2);
	    setSectionColor(_rearLedsKidney.section3, sizeof(_rearLedsKidney.section3), Color2);
            patternState = ONE;
            break;   

        
     }
}


