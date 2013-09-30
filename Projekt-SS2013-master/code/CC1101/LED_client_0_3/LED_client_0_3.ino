#include "LPD6803.h"
#include "EEPROM.h"
#include "cc1101.h"
#include "ccPacketHandler.h"
#include <TimerOne.h>

#define LEDOUTPUT 4 // The LED is wired to the Arduino Output 4 (physical panStamp pin 19)
#define CLOCKPIN  5 // 'yellow' wire
#define DATAPIN   3 // 'green' wire 

/////////////////////
//--- INSTANCES ---//
/////////////////////

CC1101 _cc1101; // The connection to the hardware chip CC1101 the RF Chip
ccPacketHandler _ccPacketHandler; // In charge of building, reading and forwarding ccPackets
LPD6803 _strip = LPD6803(50, DATAPIN, CLOCKPIN); // Set the first variable to the NUMBER of pixels. 20 = 20 pixels in a row

///////////////////
//--- MEMBERS ---//
///////////////////

byte _syncWord = (19, 9); // The syncWord of sender and receiver must be the same
byte _serverAddress = 1;
byte _clientId = 2;

int _msBlink = 100; // blink-time in ms

int _pillarHead = 0;
int _pillarLength = 4;
unsigned int _pillarColor = randomColor();

boolean _packetAvailable = false; // a flag that a wireless packet has been received
boolean _ccClear = true; // false as long as send packet has not been confirmed yet 
boolean _flagRainbow = false; // true := rainbow()

CCPACKET _ccPacket;



//////////////////////
//--- INTERRUPTS ---//
//////////////////////

// Handle interrupt from CC1101 (INT0)
void cc1101signalsInterrupt(void)
{
  _packetAvailable = true; // set the flag that NO package is available
}



///////////////////////////
//--- INITIALIZATIONS ---//
///////////////////////////

// Initializing RF-chip
void cc1101Init()
{
  _cc1101.init(); // initialize the RF Chip
  _cc1101.setCarrierFreq(CFREQ_868); // Set carrier frequency to default 868 or 915 MHz
  _cc1101.setSyncWord(&_syncWord, false);  
  _cc1101.setDevAddress(_clientId, false); // this device address need to match the target address in the sender
  _cc1101.enableAddressCheck(); // you can skip this line, because the default is to have the address check enabled
  _cc1101.setRxState();
  Serial.println("... RF Chip initialized"); 
  attachInterrupt(0, cc1101signalsInterrupt, FALLING); // Enable wireless reception interrupt
}

// Initializing the LED-strip
void stripInit()
{
  _strip.setCPUmax(50);
  _strip.begin();
  _strip.show();
}

// The setup method gets called on start of the system.
void setup()
{
  Serial.begin(115200); // 9600 // 38400
  Serial.println("Setting up client-node..."); 
  pinMode(LEDOUTPUT, OUTPUT); // setup the blinker output
  digitalWrite(LEDOUTPUT, LOW);   
  cc1101Init();
  stripInit();
  _ccPacketHandler.setId(_clientId); // set ccPacketHandlers id
  Serial.print("... ccPacketHandler startet with id "); 
  Serial.println(_ccPacketHandler.getId());
  
  // initial test packet
  delay(1000);
  //ccSend(0);
}



/////////////////////
//--- MAIN LOOP ---//
/////////////////////

// The loop method gets called on and on after the start of the system.
void loop()
{
  if(_packetAvailable) // something has come in
  {
    ccReceive(); 
  }
  else // nothing has come in - ccPackets sendable
  {
    blink();
  }
}



///////////////////////////
//--- CC1101-PROTOCOL ---//
///////////////////////////

// The ccSend method sends the current ccPacket.
void ccSend()
{
  if(_cc1101.sendData(_ccPacketHandler.getPacket()))
  {
    blink();
    _ccClear = false;
  }
  else
  {
    Serial.println("ERROR! - Failed to send packet.");
  }   
}

// The ccSend method sends ccPackets.
void ccSend(byte receiver)
{
  if(_ccClear)
  {
    _ccPacketHandler.testPacket(receiver); // build a test packet for server
    if(_cc1101.sendData(_ccPacketHandler.getPacket()))
    {
      blink();
      _ccClear = false;
    }
    else
    {
      Serial.println("ERROR! - Failed to send test packet.");
    }
    _packetAvailable = true;
  }
}

// the ccReceive method evaluates incoming ccPackages.
void ccReceive()
{
  if(_packetAvailable)
  {    
    _packetAvailable = false; // clear the flag
    CCPACKET ccPacket;
    detachInterrupt(0); // Disable wireless reception interrupt
    if(_cc1101.receiveData(&ccPacket) > 0 && ccPacket.RECEIVER_ID == _clientId) // some data was received
    {
      if (ccPacket.crc_ok && ccPacket.length > 1) // the whole ccPacket was properly received
      {
        _ccPacketHandler.printPacket(ccPacket);
        ccHandle(ccPacket); // set as ccPacket and evaluate its content
      }
    }         
    attachInterrupt(0, cc1101signalsInterrupt, FALLING); // Enable wireless reception interrupt
  }
}


// The ccHandle method decides what happens due to incoming packets.
void ccHandle(CCPACKET ccPacket)
{
  Serial.print("handling key ");
  Serial.println(ccPacket.ADMINKEY);
  
  switch (ccPacket.ADMINKEY)
  {
    case BACKWARD_CATERPILLAR: 
      caterpillarBw();
      ccAcknowledge();
      break;
    case FORWARD_CATERPILLAR: 
      caterpillarFw();
      ccAcknowledge();
      break;
    case CHANGE_COLOR: 
      caterpillarChangeColor();
      _strip.setPixelColor(_pillarHead, _pillarColor);
      _strip.show();
      ccAcknowledge();
      break;
    case ACKNOWLEDGE_REQUEST: 
      break;
    case ACKNOWLEDGE_RESPONSE:
      _ccClear = true;
      break;
    case TEST: 
      //flipRainbow();
      ccAcknowledge();
      break;
    default: // unknown packet received
      Serial.print("ERROR - unknown packet received, key: ");
      Serial.println(ccPacket.ADMINKEY);
      break; 
  }
}

void ccAcknowledge()
{
  _ccPacket = _ccPacketHandler.getPacket(); // "use" current packet's data
  _ccPacketHandler.acknowledge(); // acknowledge the packet
  ccSend(); // send the acknowledgement-packet
  _ccClear = true;
}


/////////////////////
//--- LED-STRIP ---//
/////////////////////

void caterpillarChangeColor()
{
  _pillarColor = randomColor();
  Serial.print("COLOR: ");
  Serial.println(_pillarColor);
}

void caterpillarFw() 
{  
  if(_pillarHead != _strip.numPixels()) // head not at the end
  {
    _strip.setPixelColor(_pillarHead, _pillarColor); 
    ++_pillarHead;
  }
  else // head at the end
  {
    _pillarHead = 0;
  }
  
  if(_pillarHead < _pillarLength) // head not across the border
  {
    _strip.setPixelColor((_strip.numPixels() - (_pillarLength - _pillarHead)), Color(0, 0, 0));
  }
  else // head across the border
  {
    _strip.setPixelColor((_pillarHead - _pillarLength), Color(0, 0, 0));
  }
  _strip.show();
}

void caterpillarBw()
{  
  if(_pillarHead != -1) // head not at the end
  {
    _strip.setPixelColor(_pillarHead, _pillarColor); 
    --_pillarHead;
  }
  else // head at the end
  {
    _pillarHead = _strip.numPixels() - 1;
  }
  
  if(_pillarHead < (_strip.numPixels() - _pillarLength)) // head not across the border
  {
    _strip.setPixelColor((_pillarHead + _pillarLength), Color(0, 0, 0));
  }
  else
  {
    _strip.setPixelColor((_pillarLength - (_strip.numPixels() - _pillarHead)), Color(0, 0, 0));
  }
  _strip.show();
}

unsigned int randomColor()
{
  return Color(random(0, 63), random(0, 63), random(0, 63));
}

// The colorWipe method switched all LEDs to one given color
void colorWipe(uint16_t c)
{
  for(int i = 0; i < _strip.numPixels(); ++i)
  {
    _strip.setPixelColor(i, c);
  }
  _strip.show();
}

void flipRainbow()
{
  if(_flagRainbow)
    _flagRainbow = false;
  else
    _flagRainbow = true;
}

// The rainbow method drives every LED through all possible colors
void rainbow()//uint8_t wait)
{
  for (int j = 0; j < 96 * 3; ++j) // 3 cycles of all 96 colors in the wheel
  {
    for (int i = 0; i < _strip.numPixels(); ++i) 
    {
      _strip.setPixelColor(i, Wheel((i + j) % 96));      
    }  
    _strip.show();   // write all the pixels out
  }
}

// Input a value 0 to 127 to get a color value.
// The colours are a transition r - g -b - back to r
unsigned int Wheel(byte WheelPos)
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

// Create a 16 bit color value from R, G, B
unsigned int Color(byte r, byte g, byte b)
{
  //Take the lowest 5 bits of each value and append them end to end
  return(((unsigned int)g & 0x1F ) << 10 | ((unsigned int)b & 0x1F) << 5 | (unsigned int)r & 0x1F);
}



/////////////////
//--- STUFF ---//
/////////////////

// The blink method is called to let the system LED blink once for a cetrain time.
void blink()
{
  digitalWrite(LEDOUTPUT, HIGH);
  delay(_msBlink);
  digitalWrite(LEDOUTPUT, LOW);
  delay(_msBlink);
}

