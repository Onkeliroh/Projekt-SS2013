#include "EEPROM.h"
#include "cc1101.h"

// The LED is wired to the Arduino Output 4 (physical panStamp pin 19)
#define LEDOUTPUT 19
// The syncWord of sender and receiver must be the same
byte syncWord = (19, 9);
byte receiverAddress = 5;

// The connection to the hardware chip CC1101 the RF Chip
CC1101 cc1101;

// blink-time in ms
int msBlink = 100;

// a flag that a wireless packet has been received 
boolean packetAvailable = false;         

/**
* Let the LED Output blink one time.
* 
* With small pause after the blink to see two consecutive blinks. 
*/
void blink()
{
      digitalWrite(LEDOUTPUT, HIGH);
      delay(msBlink);
      digitalWrite(LEDOUTPUT, LOW);
      delay(msBlink);
}

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

  // blink once to signal the setup
  blink();
  
  // initialize the RF Chip
  cc1101.init();
  // Set carrier frequency to 868 or 915 MHz
  cc1101.setCarrierFreq(CFREQ_868);
  cc1101.setSyncWord(&syncWord, false);  
  // this device address need to match the target address in the sender
  cc1101.setDevAddress(receiverAddress, false);
  cc1101.enableAddressCheck(); // you can skip this line, because the default is to have the address check enabled
  cc1101.setRxState();
  // Enable wireless reception interrupt
  attachInterrupt(0, cc1101signalsInterrupt, FALLING);
  Serial.println("device initialized");

  Serial.println("setup done");
}

/**
* The loop method gets called on and on after the start of the system.
*/
void loop()
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
      Serial.print("rssi: ");
      Serial.print(packet.rssi);
      Serial.print("\tlqi: ");
      Serial.print(packet.lqi);
      Serial.print("\tcrc_ok: ");
      Serial.println(packet.crc_ok);
      if (packet.crc_ok && packet.length > 1)
      {
        Serial.print(packet.data[1]);
        Serial.print("\t+ ");
        Serial.print(packet.data[2]);
        Serial.print("\t= ");
        Serial.println(packet.data[1] + packet.data[2]);
        //blink();
      }
    }
    
    // Enable wireless reception interrupt
    attachInterrupt(0, cc1101signalsInterrupt, FALLING);
  }
}
