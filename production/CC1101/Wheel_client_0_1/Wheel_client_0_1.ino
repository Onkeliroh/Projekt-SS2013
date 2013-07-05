#include "EEPROM.h"
#include "cc1101.h"
#include "ccPacketHandler.h"
#include <TimerOne.h>
#include "PinChangeInt.h"
    
    
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
byte _clientAddress = 2;

int _msBlink = 100; // blink-time in ms
const int hallSensor1 = 4;
const int hallSensor2 = 5;
const int greenDirec = 8;
const int redDirec = 9;
boolean states1[ 2 ][ 8 ];
int steps[8];
    
boolean compare1 = false;
boolean compare2 = false;
boolean compare3 = false;
boolean compare4 = false;
    
byte wheel_direction = 0; // 0 := no known direction, 1 := turning left, 2 := turing right 

const int trace_der1[] = { 3,1,3,0 };
const int trace_der2[] = { 1,3,0,3 };
const int trace_der3[] = { 3,0,3,1 };
const int trace_der4[] = { 0,3,1,3 };
    
const int trace_izq1[] = { 3,1,2,1 };
const int trace_izq2[] = { 1,2,1,3 };
const int trace_izq3[] = { 2,1,3,1 };
const int trace_izq4[] = { 1,3,1,2 };
    
int traceSize = 4;
int count = 0;

boolean sensorupdated = false;
    
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

// Init Wheel
void wheelInit()
{
  pinMode( greenDirec, OUTPUT );
  pinMode( redDirec, OUTPUT );
  digitalWrite( greenDirec, LOW );
  digitalWrite( redDirec, LOW );
    
  pinMode(hallSensor1, INPUT ); 
  digitalWrite( hallSensor1, HIGH ); //To enable an internal 20K pullup resistor (see http://arduino.cc/en/Reference/DigitalWrite)
  PCintPort::attachInterrupt( hallSensor1, &rpm_interrupt1, CHANGE );  
  pinMode(hallSensor2, INPUT ); 
  digitalWrite( hallSensor2, HIGH ); //To enable an internal 20K pullup resistor (see http://arduino.cc/en/Reference/DigitalWrite)
      
}

// The setup method gets called on start of the system.
void setup()
{
  Serial.begin(115200); // 9600 // 38400
  Serial.println("Setting up client-node..."); 
  pinMode(LEDOUTPUT, OUTPUT); // setup the blinker output
  digitalWrite(LEDOUTPUT, LOW);   
  cc1101Init();
  wheelInit();
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
  }
  if(sensorupdated) 
  {     
   PCintPort::detachInterrupt( hallSensor1 );
  
   for( int col=0; col <= 3; col++ )
   {           
     compare1 = compareArrays( trace_der1, steps, traceSize );
     compare2 = compareArrays( trace_der2, steps, traceSize );
     compare3 = compareArrays( trace_der3, steps, traceSize );
     compare4 = compareArrays( trace_der4, steps, traceSize );
              
     if( compare1 || compare2 || compare3 || compare4 )
     {
       wheelEval(1); 
       
       break;
     } 
     else
     { 
       compare1 = compareArrays( trace_izq1, steps, traceSize );
       compare2 = compareArrays( trace_izq2, steps, traceSize );
       compare3 = compareArrays( trace_izq3, steps, traceSize );
       compare4 = compareArrays( trace_izq4, steps, traceSize );
  
       if( compare1 || compare2 || compare3 || compare4 )
       {    
         wheelEval(2);                  
          
         break;  
       } 
       else
       {
         wheelEval(0);     
       }  
     }              
   }         
   PCintPort::attachInterrupt( hallSensor1, &rpm_interrupt1, CHANGE );  
  }
  sensorupdated = false; 
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
    if(_cc1101.receiveData(&ccPacket) > 0 && ccPacket.data[0] == _clientAddress) // some data was received
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

void ccSendWheelDirection()
{
  switch (wheel_direction)
  {
    case 1: // right
      _ccPacketHandler.buildPacket(0, 28);
      break;
    case 2: // left
      _ccPacketHandler.buildPacket(0, 27);
      break;
    default:
      break;
  }
  ccSend();  
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



/////////////////
//--- WHEEL ---//
/////////////////

//For Panstamp
void rpm_interrupt1()
{
  states1[ 0 ][ count ] = digitalRead( hallSensor1 );   // To determine direction
  states1[ 1 ][ count ] = digitalRead( hallSensor2 );
  steps[ count ] = int (states1[ 0 ][ count ])*2 + int( states1[ 1 ][ count ] )*1; 
  ++count; 
  
  if( count > traceSize -1 ) 
  {
    count=0;        
  }
  
  sensorupdated = true;
}

// some wicked stuff going on in here...   
boolean compareArrays( const int *traceArray, int *readArray, int sizeArray )
{
  boolean areEqual = true;  //We start on the assumption that both arrays are equal
  int matches = 0;
  int index = 0;
  
  while ( ( areEqual ) && ( index < sizeArray ) )
  {
    if ( traceArray[ index ] == readArray[ index ]  )
    {
      ++index;
    }
    else 
    {
      areEqual = false;
    }  
  }
  return areEqual; 
}

// 
void wheelEval(byte dir)
{
  switch (dir)
  {
    case 1: // right
      if(wheel_direction != 1)
      {
        wheel_direction = 1;
        ccSendWheelDirection();//turnRight();
      }        
      digitalWrite( greenDirec, HIGH );
      digitalWrite( redDirec, LOW );
      break;
    case 2: // left
      if(wheel_direction != 2)
      {
        wheel_direction = 2;
        ccSendWheelDirection();//turnLeft(); 
      }
      digitalWrite( greenDirec, LOW );
      digitalWrite( redDirec, HIGH );
      break;
    default: 
      digitalWrite( greenDirec, LOW );
      digitalWrite( redDirec, LOW );
      break;
  }
}

// "turn_right"event triggered
void turnRight()
{
  Serial.println("'turn_right'event triggered");
}

// "turn_left"event triggered
void turnLeft()
{
  Serial.println("'turn_left'event triggered");
}

