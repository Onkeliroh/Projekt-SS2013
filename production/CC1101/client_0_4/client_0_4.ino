#include "EEPROM.h"
#include "cc1101.h"
#include "ccPacketHandler.h"

#define LEDOUTPUT 4 // The LED is wired to the Arduino Output 4 (physical panStamp pin 19)

// instances
CC1101 _cc1101; // The connection to the hardware chip CC1101 the RF Chip
ccPacketHandler _ccPacketHandler; // In charge of building, reading and forwarding ccPackets

// member variables
byte _syncWord = (19, 9); // The syncWord of sender and receiver must be the same
byte _serverAddress = 0;
byte _clientAddress = 1;
byte _testByte = 0;
int _msBlink = 100; // blink-time in ms
boolean _packetAvailable = false; // a flag that a wireless packet has been received 

// Handle interrupt from CC1101 (INT0)
void cc1101signalsInterrupt(void)
{
  _packetAvailable = true; // set the flag that NO package is available
}

// Initializing RF Chip
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

// The setup method gets called on start of the system.
void setup()
{
  Serial.begin(115200); // 9600 // 38400
  Serial.println("Setting up..."); 
  pinMode(LEDOUTPUT, OUTPUT); // setup the blinker output
  digitalWrite(LEDOUTPUT, LOW);   
  cc1101Init();
  _ccPacketHandler.setId(_clientAddress); // set ccPacketHandlers id
  Serial.print("... ccPacketHandler startet with id "); 
  Serial.println(_ccPacketHandler.getId());
}

// The loop method gets called on and on after the start of the system.
void loop()
{
  if(_packetAvailable) // something has come in
  {
    blink();
    if((random(1, 10) % 2) == 0)
    {
      _packetAvailable = false;
    }    
  }
  else // nothing has come in - ccPackets sendable
  {
    ccSend(0);
    Serial.print("ccPacket send to ");
    Serial.println(_serverAddress);
    delay(250);
  }
}

// The blink method is called to let the system LED blink once for a cetrain time.
void blink()
{
  digitalWrite(LEDOUTPUT, HIGH);
  delay(_msBlink);
  digitalWrite(LEDOUTPUT, LOW);
  delay(_msBlink);
}

// The ccSend method sends ccPackets.
void ccSend(byte receiver)
{
  _ccPacketHandler.testPacket(receiver); // build a test packet for server
  if(_cc1101.sendData(_ccPacketHandler.getPacket()))
  {
    blink();
  }
  else
  {
    Serial.println("ERROR! - Failed to send test packet.");
  }
  _packetAvailable = true;
}

// the ccReceive method evaluates incoming ccPackages.
void ccReceive()
{
  if(_packetAvailable)
  {    
    _packetAvailable = false; // clear the flag    
    CCPACKET packet; // create new cc1101-packet
    detachInterrupt(0); // Disable wireless reception interrupt
    if(_cc1101.receiveData(&packet) > 0) // some data was received
    {
      if (packet.crc_ok && packet.length > 1) // the whole ccPacket was properly received
      {
        _ccPacketHandler.evaluatePacket(packet); // set as ccPacket and evaluate its content
      }
    }        
    attachInterrupt(0, cc1101signalsInterrupt, FALLING); // Enable wireless reception interrupt
  }
}

// The ccHandle method does some stuff...
void ccHandle()
{
  Serial.println("handle");
  delay(500);
}

