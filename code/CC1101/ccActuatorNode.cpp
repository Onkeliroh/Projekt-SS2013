#include "ccActuatorNode.h"


LPD6803 _ledStrip = LPD6803(NUMLEDS, DATAPIN, CLOCKPIN); // Set the first variable to the NUMBER of pixels. 20 = 20 pixels in a row

int _pillarHead = 0;
int _pillarLength = 4;
unsigned int _pillarColor;

uint16_t color1;
uint16_t color2;

boolean _flagRainbow = false; // true := rainbow()
boolean _clear = true;


//CONSTRUCTOR//

CCACTUATORNODE::CCACTUATORNODE(byte id, byte twinNode)

{

    _id = id;
    _twinNode = twinNode;
    _syncWord = (19,9);    
              
}


//DESTRUCTOR//
CCACTUATORNODE::~CCACTUATORNODE()

{

	
}

//METHODS
void CCACTUATORNODE::setup()

{
    
    Serial.begin(BAUDRATE); 

    ledBlinkSetup();
  
    ledStripInit();

    rfChipInit();
    

}



boolean CCACTUATORNODE::ccGetNewPacket()

{   
    
    CCPACKET ccPacket; 
    boolean validPacket = false; 

    byte cc11 = _cc1101.receiveData(&ccPacket);
   
    if(cc11 > 0) // some data was received      
    {
        if (ccPacket.crc_ok && ccPacket.length > 1) // the whole ccPacket was properly received
        {  
            if(ccPacket.RECEIVER_ID == _id) 
            {                 
                validPacket = true; 
                _ccPacketHandler.setPacket(ccPacket);
            }   
                 
        }
    }
    
    return validPacket;
   
}


void CCACTUATORNODE::ccHandle()

{

    byte key = _ccPacketHandler.getAdminKey();
    byte firstColor;
    byte secondColor;
      
    switch (key)
    {
        case CHANGE_COLOR: 
            Serial.println("COLOR!!");            
            colorWipe(randomColor());            
            //ccAcknowledge();
            break;
        case ACKNOWLEDGE_REQUEST: 
            break;
        case ACKNOWLEDGE_RESPONSE:
            _clear = true;
            break;
        case STRIPES:
            firstColor = _ccPacketHandler.getFirstColor();
            secondColor = _ccPacketHandler.getSecondColor();
            color1 = findColor(firstColor);
            color2 = findColor(secondColor);
            setPatternStripes(color1, color2);    
            break; 
        case TEST: 
            //flipRainbow();
            //ccAcknowledge();
            break;
        default: // unknown packet received
            Serial.print("ERROR - unknown packet received, key: ");
            Serial.println(key);
            break; 
    }
}


// Initializing the LED-strip
void CCACTUATORNODE::ledStripInit()
{
    _ledStrip.setCPUmax(40);
    _ledStrip.begin();
    _ledStrip.show();
   
}

void CCACTUATORNODE::caterpillarChangeColor()
{
    _pillarColor = randomColor();  
}

void CCACTUATORNODE::caterpillarFw() 
{  
  if(_pillarHead != _ledStrip.numPixels()) // head not at the end
  {
    _ledStrip.setPixelColor(_pillarHead, _pillarColor); 
    ++_pillarHead;
  }
  else // head at the end
  {
    _pillarHead = 0;
  }
  
  if(_pillarHead < _pillarLength) // head not across the border
  {
    _ledStrip.setPixelColor((_ledStrip.numPixels() - (_pillarLength - _pillarHead)), Color(0, 0, 0));
  }
  else // head across the border
  {
    _ledStrip.setPixelColor((_pillarHead - _pillarLength), Color(0, 0, 0));
  }
  _ledStrip.show();
}

void CCACTUATORNODE::caterpillarBw()
{  
  if(_pillarHead != -1) // head not at the end
  {
    _ledStrip.setPixelColor(_pillarHead, _pillarColor); 
    --_pillarHead;
  }
  else // head at the end
  {
    _pillarHead = _ledStrip.numPixels() - 1;
  }
  
  if(_pillarHead < (_ledStrip.numPixels() - _pillarLength)) // head not across the border
  {
    _ledStrip.setPixelColor((_pillarHead + _pillarLength), Color(0, 0, 0));
  }
  else
  {
    _ledStrip.setPixelColor((_pillarLength - (_ledStrip.numPixels() - _pillarHead)), Color(0, 0, 0));
  }
  _ledStrip.show();
}

void CCACTUATORNODE::setPatternStripes(uint16_t color1, uint16_t color2)
{ 
  int i = 0;

  while(i < _ledStrip.numPixels())
  {
      _ledStrip.setPixelColor(i, color1);  
      ++i;  
      _ledStrip.setPixelColor(i, color2);
      ++i;
      _ledStrip.show();
      delay(50);
 }
 
}


unsigned int CCACTUATORNODE::randomColor()
{
    return Color(random(0, 31), random(0, 31), random(0, 31));
}

// The colorWipe method switched all LEDs to one given color
void CCACTUATORNODE::colorWipe(uint16_t c)
{
  for(int i = 0; i < _ledStrip.numPixels(); ++i)
  {
    _ledStrip.setPixelColor(i, c);
  }
  _ledStrip.show();
}

void CCACTUATORNODE::flipRainbow()
{
  if(_flagRainbow)
    _flagRainbow = false;
  else
    _flagRainbow = true;
}

// The rainbow method drives every LED through all possible colors
void CCACTUATORNODE::rainbow()//uint8_t wait)
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
unsigned int CCACTUATORNODE::Wheel(byte WheelPos)
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

unsigned int CCACTUATORNODE::findColor(byte colorIndex)

{
    unsigned int pickedColor;
    
    switch(colorIndex)
    {
        case 0: //!
            pickedColor = Color(0, 0, 31); //green
            break; 
        case 1:  //"
            pickedColor = Color(0, 15, 31); //lemon green
            break; 
        case 2:  //#
            pickedColor = Color(0, 8, 0); //red
            break;
        case 3:  //$
            pickedColor = Color(0, 15, 0); //red
            break;
        case 4:  //%
            pickedColor = Color(0, 31, 0); //red
            break;
        case 5:  //&
            pickedColor = Color(31, 0, 31); //ligth blue
            break;
        case 6:  //'
            pickedColor = Color(15, 0, 31);  //blue
            break;
        case 7:  //(
            pickedColor = Color(15, 0, 15); //blue
            break;
        case 8:  //)
            pickedColor = Color(0, 31, 31); //yellow
            break;
        case 9:  //*
            pickedColor = Color(0, 15, 15); //yellow
            break; 
        case 10: // +
            pickedColor = Color(5, 5, 31);  //turquoise
            break; 
        case 11: // ,
            pickedColor = Color(10, 10, 31); //yellow or lemon green
            break; 
        case 12: // -
            pickedColor = Color(15, 15, 31); //blue 
            break;
        case 13: // .
            pickedColor = Color(20, 20, 31); //
            break;
        case 14: // /
            pickedColor = Color(25, 25, 31);
            break;
        case 15: // 0
            pickedColor = Color(5, 5, 0);
            break;
        case 16: // 1
            pickedColor = Color(10, 10, 0);
            break;
        case 17:  //2
            pickedColor = Color(15, 15, 0);
            break;
        case 18:  //3
            pickedColor = Color(20, 20, 0);
            break;
        case 19:  //4
            pickedColor = Color(25, 25, 0);
            break; 
        case 20:  //5
            pickedColor = Color(5, 15, 30);
            break; 
        case 21:  //6
            pickedColor = Color(30, 15, 5);
            break;
        case 22:  //7
            pickedColor = Color(0, 20, 5);
            break;
        case 23:  //8
            pickedColor = Color(31, 31, 31);
            break; //9       
      }
  return pickedColor;
}

// Create a 16 bit color value from R, G, B
unsigned int CCACTUATORNODE::Color(byte r, byte g, byte b)
{
  //Take the lowest 5 bits of each value and append them end to end
  return(((unsigned int)g & 0x1F ) << 10 | ((unsigned int)b & 0x1F) << 5 | (unsigned int)r & 0x1F);
}
 
