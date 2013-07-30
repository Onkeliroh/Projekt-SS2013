#include "EEPROM.h"
#include "cc1101.h"

// The LED is wired to the Arduino Output 4 (physical panStamp pin 19)
#define LEDOUTPUT 4
// The syncWord of sender and receiver must be the same
byte syncWord = (19, 9);
byte serverAddress = 4;
byte clientAddress = 6;

// The connection to the hardware chip CC1101 the RF Chip
CC1101 cc1101;

// blink-time in ms
int msBlink = 100;
// token-byte to be send back and forth
byte ball = 0;
// mutex
boolean ccMutex = false; // default for client = false (no sending allowed)

// a flag that a wireless packet has been received 
boolean packetAvailable = false;
/**
* Handle interrupt from CC1101 (INT0)
*/
void cc1101signalsInterrupt(void)
{
  // set the flag that a package is available
  packetAvailable = true;
}

/**
* The setup method gets called on start of the system.
*/
void setup()
{
  Serial.begin(2400); // 9600 // 38400
  Serial.println("start");
  // setup the blinker output
  pinMode(LEDOUTPUT, OUTPUT);
  digitalWrite(LEDOUTPUT, LOW);  
  // initialize the RF Chip
  cc1101.init();
  // Set carrier frequency to default 868 or 915 MHz
  //cc1101.setCarrierFreq(CFREQ_868);
  cc1101.setSyncWord(&syncWord, false);  
  // this device address need to match the target address in the sender
  cc1101.setDevAddress(clientAddress, false);
  cc1101.enableAddressCheck(); // you can skip this line, because the default is to have the address check enabled
  cc1101.setRxState();
  // Enable wireless reception interrupt
  attachInterrupt(0, cc1101signalsInterrupt, FALLING);
  Serial.println("device initialized");
  Serial.print("client-setup done! id is: ");
  Serial.println(clientAddress);
}

/**
* The loop method gets called on and on after the start of the system.
*/
void loop()
{
  if(ccMutex)
  {
    ccSend();
  }
  else
  {
    ccReceive();
  }
}

void blink()
{
  digitalWrite(LEDOUTPUT, HIGH);
  delay(msBlink);
  digitalWrite(LEDOUTPUT, LOW);
  delay(msBlink);
}

void ccSend()
{
  CCPACKET data;
  data.length = 2;
  data.data[0] = serverAddress; // the first byte in the data is the destination's address
  data.data[1] = ball;
  if(cc1101.sendData(data))
  {
    Serial.println(data.data[1]);
    blink();
  }
  else
  {
    Serial.print("failed to send!");
  }  
  ccMutex = false;   
}

void ccReceive()
{
  if(packetAvailable)
  {
    // clear the flag
    packetAvailable = false;    
    // create new cc1101-packet
    CCPACKET packet;    
    // Disable wireless reception interrupt
    detachInterrupt(0);    
    if(cc1101.receiveData(&packet) > 0)
    {
      if (packet.crc_ok && packet.length > 1)
      {
        if(packet.data[1] == 0 || packet.data[1] == ball)
        {
          ccHandle(packet.data[1]);
          Serial.print("crc_ok: ");
          Serial.print(packet.crc_ok);
          Serial.print(" rssi: ");
          Serial.print(packet.rssi);
          Serial.print(" lqi: ");
          Serial.println(packet.lqi);
        }
      }
    }    
    // Enable wireless reception interrupt
    attachInterrupt(0, cc1101signalsInterrupt, FALLING);
  }
}

void ccHandle(byte in)
{
  Serial.println("handle");
  delay(500);
  ball = in + 1;
  ccMutex = true;
}

