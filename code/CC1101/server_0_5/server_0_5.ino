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
byte _clients[] = {1, 2};

int _msBlink = 100; // blink-time in ms

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

// Initializing RF Chip
void cc1101Init()
{
  _cc1101.init(); // initialize the RF Chip
  _cc1101.setCarrierFreq(CFREQ_868); // Set carrier frequency to default 868 or 915 MHz
  _cc1101.setSyncWord(&_syncWord, false);  
  _cc1101.setDevAddress(_serverAddress, false); // this device address need to match the target address in the sender
  _cc1101.enableAddressCheck(); // you can skip this line, because the default is to have the address check enabled
  _cc1101.setRxState();
  Serial.println("... RF Chip initialized"); 
  attachInterrupt(0, cc1101signalsInterrupt, FALLING); // Enable wireless reception interrupt
}

// The setup method gets called on start of the system.
void setup()
{
  Serial.begin(115200); // 9600 // 38400
  Serial.println("Setting up server-node..."); 
  pinMode(LEDOUTPUT, OUTPUT); // setup the blinker output
  digitalWrite(LEDOUTPUT, LOW);   
  cc1101Init();
  _ccPacketHandler.setId(_serverAddress); // set ccPacketHandlers id
  Serial.print("... ccPacketHandler startet with id "); 
  Serial.println(_ccPacketHandler.getId());
  
  // initial test packet
  delay(1000);
  ccSend(1);
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
  CCPACKET cp = _ccPacketHandler.getPacket();
  Serial.print("OUT: ");
    for (byte i = 0; i < cp.length; ++i)
    {
        Serial.print(cp.data[i]);
        Serial.print("|");
    }
  Serial.println("");
  if(_cc1101.sendData(cp))
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
}

// the ccReceive method evaluates incoming ccPackages.
void ccReceive()
{
  if(_packetAvailable)
  {    
    _packetAvailable = false; // clear the flag
    CCPACKET ccPacket; 
    detachInterrupt(0); // Disable wireless reception interrupt
    if(_cc1101.receiveData(&ccPacket) > 0 && ccPacket.data[0] == _serverAddress) // some data was received
    {
      if (ccPacket.crc_ok && ccPacket.length > 1) // the whole ccPacket was properly received
      {
        ccHandle(_ccPacketHandler.evaluatePacket(ccPacket)); // set as ccPacket and evaluate its content
      }
    }        
    attachInterrupt(0, cc1101signalsInterrupt, FALLING); // Enable wireless reception interrupt
  }
}

// The ccHandle method does some stuff...
void ccHandle(byte key)
{
  Serial.print("handling key ");
  Serial.println(key);
  switch (key)
  {
    case 27: // Wheel: "turn_left"-event
      ccAcknowledge();
      _ccPacketHandler.buildPacket((_ccPacket.data[1] - 1), 45); // new ccPacket to senders actuator with key 45
      ccSend();
      break;
    case 28: // Wheel: "turn_right"-event
      ccAcknowledge();
      _ccPacketHandler.buildPacket((_ccPacket.data[1] - 1), 46); // new ccPacket to senders actuator with key 45
      ccSend();
      break;
    case 200: // acknowledge pacekt received
      break;
    case 201: // acknowledge packet correct
      _ccClear = true;
      break;
    case 202: // acknowledge packet incorrect
      Serial.println("ERROR - False acknowledge received! Resending previous package...");
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

// The pushClientIds method pushes all clients' ids to the main server.
void pushClientIds()
{
  for(int i = 0; i < 5; ++i)
  {
    Serial.print(_clients[i]);
  }
  Serial.println();
}

