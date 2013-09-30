#include "EEPROM.h"
#include "cc1101.h"
#include "ccPacketHandler.h"

#define LEDOUTPUT 4 // The LED is wired to the Arduino Output 4 (physical panStamp pin 19)

#define RSSI_OFFSET 74 //According to the Design Note DN505 http://www.ti.com/lit/an/swra114d/swra114d.pdf

/////////////////////
//--- INSTANCES ---//
/////////////////////

CC1101 _cc1101; // The connection to the hardware chip CC1101 the RF Chip
ccPacketHandler _ccPacketHandler = ccPacketHandler(SERVER_01);// In charge of building, reading and forwarding ccPackets
CCPACKET _ccPacket;

///////////////////
//--- MEMBERS ---//
///////////////////

byte _syncWord = (19, 9); // The syncWord of sender and receiver must be the same
byte _serverAddress = 1;
//Why the server cannot have address 0: http://code.google.com/p/panstamp/wiki/LowLevelLibrary
byte _clients[] = {2, 3, 4}; // 2 := LED-strip, 3 := Wheel, 4 := Accel
byte _ledClient = 2; 

int _msBlink = 100; // blink-time in ms

boolean _packetAvailable = false; // a flag that a wireless packet has been received
boolean _ccClear = true; // false as long as send packet has not been confirmed yet

int rssi_dBm;
byte emisorId;
byte nodeLowBattery;

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
  //Serial.println("... RF Chip initialized"); 
  attachInterrupt(0, cc1101signalsInterrupt, FALLING); // Enable wireless reception interrupt
}

// The setup method gets called on start of the system.
void setup()
{
  Serial.begin(115200); // 9600 // 38400
//  Serial.println("Setting up server-node..."); 
  pinMode(LEDOUTPUT, OUTPUT); // setup the blinker output
  digitalWrite(LEDOUTPUT, LOW);   
  cc1101Init();  
}



/////////////////////
//--- MAIN LOOP ---//
/////////////////////
byte incomingByte;
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
  
  while (Serial.available())
  {
    // read the incoming byte:
    incomingByte = Serial.read();
    // say what you got:   
    Serial.print("I received: ");    
    Serial.println(incomingByte);
   
  }
}

//////////////////
//--- CC1101 ---//
//////////////////

// the ccReceive method evaluates incoming ccPackages.
void ccReceive()
{   
  _packetAvailable = false; // clear the flag
  CCPACKET ccPacket; 
  detachInterrupt(0); // Disable wireless reception interrupt
    
  if(_cc1101.receiveData(&ccPacket) > 0) // some data was received
  {     
    if (ccPacket.crc_ok && ccPacket.length > 1) // the whole ccPacket was properly received
    {
      _ccPacketHandler.printPacket(ccPacket);
      ccHandle(ccPacket); // set as ccPacket and evaluate its content    
    }
    
  }        
  attachInterrupt(0, cc1101signalsInterrupt, FALLING); // Enable wireless reception interrupt
}

void ccHandle(CCPACKET ccPacket)
{
//  Serial.print("handling key ");
//  Serial.println(ccPacket.ADMINKEY);
  
  switch (ccPacket.ADMINKEY)
  {
    case TURN_LEFT_EVENT: 
      _ccPacketHandler.buildPacket(_ledClient, BACKWARD_CATERPILLAR); 
      ccSend();
      break;
    case TURN_RIGHT_EVENT: 
      _ccPacketHandler.buildPacket(_ledClient, FORWARD_CATERPILLAR); 
      ccSend();
      break;
    case SHAKE_EVENT: 
      ccAcknowledge();   
      _ccPacketHandler.buildPacket(_ledClient, CHANGE_COLOR); 
      ccSend();  
      break;
    case NEAR_NODE_EVENT: 
      distanceAlert();
      ccAcknowledge();
      break;    
    case LOW_BATTERY:
      lowBatteryAlert();      
      break;
    case ACKNOWLEDGE_REQUEST:
      if (_ccPacketHandler.hashMatches(ccPacket)) 
        _ccClear = true;
//      else
//        Serial.println("ERROR - false acknowledge received! Resending previous package...");
      break;     
    case TEST: // test packet received
      ccAcknowledge();
      break;
    default: // unknown packet received
//      Serial.print("ERROR - unknown packet received, key: ");
//      Serial.println(ccPacket.ADMINKEY);
      break; 
  }
}

void distanceAlert()
{
  _ccPacket = _ccPacketHandler.getPacket(); // "use" current packet's data
  rssi_dBm = ccRSSI(_ccPacket.NEAR_NODE_RSSI);
  emisorId = _ccPacket.NEAR_NODE_ID;
  
  Serial.print("Detected RSSI " );
  Serial.print(rssi_dBm);
  Serial.print(" from device with Id: \t");
  Serial.println(emisorId);
  
}


void lowBatteryAlert()
{
  _ccPacket = _ccPacketHandler.getPacket(); // "use" current packet's data
  nodeLowBattery = _ccPacket.SENDER_ID;
  
  Serial.print("Node Nr. " );
  Serial.print(nodeLowBattery);
  Serial.println("  is running out of Battery!!!");  
}


void ccAcknowledge()
{
  _ccPacket = _ccPacketHandler.getPacket(); // "use" current packet's data
  _ccPacketHandler.acknowledge(); // acknowledge the packet
  ccSend(); // send the acknowledgement-packet
  _ccClear = true;
}


int ccRSSI(byte rawRSSI)
{
  if (rawRSSI >= 128)
    rssi_dBm = (((int)rawRSSI - 256) / 2) - RSSI_OFFSET;
  else
    rssi_dBm = ((int)rawRSSI / 2) - RSSI_OFFSET;
    
  return rssi_dBm;  
}

// The ccSend method sends the current ccPacket.
void ccSend()
{
  CCPACKET cp = _ccPacketHandler.getPacket();
  
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



/////////////////
//--- STUFF ---//
/////////////////

// The blink method is called to let the system LED blink once for a certain time.
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

