#include "EEPROM.h"
#include "cc1101.h"
#include "ccPacketHandler.h"

#define LEDOUTPUT 4 // The LED is wired to the Arduino Output 4 (physical panStamp pin 19)


/////////////////////
//--- INSTANCES ---//
/////////////////////

CC1101 _cc1101; // The connection to the hardware chip CC1101 the RF Chip
ccPacketHandler _ccPacketHandler; // In charge of building, reading and forwarding ccPackets



///////////////////
//--- MEMBERS ---//
///////////////////

byte _syncWord = (19, 9); // The syncWord of sender and receiver must be the same
byte _serverAddress = 0;
byte _clientAddress = 3;

int _msBlink = 100; // blink-time in ms
int _x, _y, _z;
int _threshold = 200;

boolean _packetAvailable = false; // a flag that a wireless packet has been received
boolean _ccClear = true; // false as long as send packet has not been confirmed yet 

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
  _cc1101.setDevAddress(_clientAddress, false); // this device address need to match the target address in the sender
  _cc1101.enableAddressCheck(); // you can skip this line, because the default is to have the address check enabled
  _cc1101.setRxState();
  Serial.println("... RF Chip initialized"); 
  attachInterrupt(0, cc1101signalsInterrupt, FALLING); // Enable wireless reception interrupt
}

void accelInit()
{
  int _x = analogRead(0);
  int _y = analogRead(1);
  int _z = analogRead(2);  
}

// The setup method gets called on start of the system.
void setup()
{
  Serial.begin(115200); // 9600 // 38400
  Serial.println("Setting up client-node..."); 
  pinMode(LEDOUTPUT, OUTPUT); // setup the blinker output
  digitalWrite(LEDOUTPUT, LOW);
  cc1101Init();
  accelInit();
  _ccPacketHandler.setId(_clientAddress); // set ccPacketHandlers id
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
    delay(100);
  }
  accelRead();
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
    if(_cc1101.receiveData(&ccPacket) > 0) // some data was received
    {
      if (ccPacket.crc_ok && ccPacket.length > 1) // the whole ccPacket was properly received
      {
        ccHandle(_ccPacketHandler.evaluatePacket(ccPacket)); // set as ccPacket and evaluate its content
      }
    }        
    attachInterrupt(0, cc1101signalsInterrupt, FALLING); // Enable wireless reception interrupt
  }
}

// The ccHandle method decides what happens due to incoming packets.
void ccHandle(byte key)
{
  Serial.print("handling key ");
  Serial.println(key);
  switch (key)
  {
    case 200: // acknowledge packet received
      break;
    case 201: // acknowledge packet correct
      _ccClear = true;
      break;
    case 202: // acknowledge packet incorrect
      Serial.println("ERROR - false acknowledge received! Resending previous package...");
      break;
    case 255: // test packet received
      ccAcknowledge();
      break;
    default: // unknown packet received
      Serial.print("ERROR - unknown packet received, key: ");
      Serial.println(key);
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



/////////////////////////
//--- ACCELEROMETER ---//
/////////////////////////

void accelRead()
{
  int x = analogRead(0);
  int y = analogRead(1);
  int z = analogRead(2);
  
  int deltaX = _x - x;
  int deltaY = _y - y;
  int deltaZ = _z - z;
  
  _x = x;
  _y = y;
  _z = z;
  
  accelEval(deltaX, deltaY, deltaZ);
}

void accelEval(int dX, int dY, int dZ)
{
  if (maximum(dX, dY, dZ) > _threshold) //|| minimum(dX, dY, dZ) < (_threshold * (-1)))
    accelShake();
}

void accelShake()
{
  Serial.println("shake! shake! shake!");
  _ccPacketHandler.buildPacket(0, 31);
  ccSend();
  delay(500);
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

int maximum(int a, int b, int c)
{
  return max(max(a, b), max(b, c));
}

int minimum(int a, int b, int c)
{
  return min(min(a, b), min(b, c));
}

