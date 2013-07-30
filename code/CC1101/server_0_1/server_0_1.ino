#include "EEPROM.h"
#include "cc1101.h"

#define LEDOUTPUT 19  // Arduino's digital pin 4 = panStamp's pin 19

// The syncWord of sender and receiver must be the same
byte syncWord = (19, 9);
byte senderAddress = 4;
byte receiverAddress = 5;

// The connection to the hardware chip CC1101 the RF Chip
CC1101 cc1101;

// counter to get increment in each loop
byte counter;
byte retnuoc;

// blink-time in ms
int msBlink = 100;

// delay for 1000ms loop-length
int msLoopLength = 1000 - (msBlink * 2);

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
  //blink();
  
  // reset the counters
  counter = 0;
  retnuoc = 255;
  // initialize the RF Chip
  cc1101.init();
  // Set carrier frequency to 868 or 915 MHz
  cc1101.setCarrierFreq(CFREQ_868);  
  cc1101.setSyncWord(&syncWord, false);
  cc1101.setDevAddress(senderAddress, false);
  Serial.println("device initialized");

  Serial.print("set sender address to ");
  Serial.println(senderAddress);
  Serial.println("done");
}

/**
* The loop method gets called on and on after the start of the system.
*/
void loop()
{  
  CCPACKET data;
  data.length = 3;
  counter++;
  retnuoc--;
  // the first byte in the data is the destination address
  // it must match the device address of the receiver
  //Serial.println("set address to ");
  data.data[0] = receiverAddress;
  data.data[1] = counter;
  data.data[2] = retnuoc;
  if(cc1101.sendData(data))
  {
    //Serial.print("ok ");
    Serial.print(counter);
    Serial.print("\t+ ");
    Serial.print(retnuoc);
    Serial.print("\t= ");
    Serial.println(counter + retnuoc);
    //blink();
  }
  else
  {
    Serial.print("failed ");
    Serial.println("FU");
  }     
  delay(msLoopLength);
}
