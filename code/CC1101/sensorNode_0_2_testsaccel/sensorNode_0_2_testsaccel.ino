#include "EEPROM.h"
#include "analogComp.h"
#include "ccSensorNode.h"
#include "accel.h"

#define SENSOR_NODE_ID 2
#define TWIN_NODE_ID 3

#define enableRFChipInterrupt()     attachInterrupt(0, RFChipInterrupt, FALLING);
#define disableRFChipInterrupt()    detachInterrupt(0);

#define enableaLowBattInterrupt()     analogComparator.enableInterrupt(lowBattInterrupt, FALLING);
#define disableLowBattInterrupt()     analogComparator.disableInterrupt();

//#define X_IDLE_ACCEL 530
//#define Y_IDLE_ACCEL 507
//#define Z_IDLE_ACCEL 620


/////////////////////
//--- INSTANCES ---//
/////////////////////

CCSENSORNODE _sensorNode = CCSENSORNODE(SENSOR_NODE_ID, TWIN_NODE_ID);
ACCEL _accel = ACCEL();

byte x_lowpart;
byte x_highpart;
byte y_lowpart;
byte y_highpart;
byte z_lowpart;
byte z_highpart;

byte accelValues[6];


///////////////////
//--- MEMBERS ---//
///////////////////

boolean _packetAvailable = false;

boolean _batteryIsLow = false;


//////////////////////
//--- INTERRUPTS ---//
//////////////////////

// Handle interrupt from CC1101 (INT0)
void RFChipInterrupt()
{
    _packetAvailable = true;            // set the flag thar a package is available
   
}

// Handle interrupt from Analog Comparator 
void lowBattInterrupt(void)
{
    _batteryIsLow = true;  
}


///////////////////////////
//--- INITIALIZATIONS ---//
///////////////////////////


void setup()
{
    _sensorNode.setup();
    enableaLowBattInterrupt();
    enableRFChipInterrupt(); 
    delay(5); //For the batteryLow System
}


/////////////////////
//--- MAIN LOOP ---//
/////////////////////

// The loop method gets called on and on after the start of the system.
void loop()
{
//    if(_packetAvailable)
//    {
//        disableRFChipInterrupt();
//        
//        if(_sensorNode.ccGetNewPacket())
//        {
//            //_sensorNode.ccPrintPacket();  
//            _sensorNode.ccHandle();  
//        }
//        else
//        {
//            if(!_sensorNode.isPacketsSender())
//            {
//                _sensorNode.reportRSSI();
//                _sensorNode.ccPrintPacket();  
//            }          
//        }
//        
//        _packetAvailable = false;   
//        enableRFChipInterrupt();  
//    }
      
      
//    if(_accel.wasShaken())
//    {
//        _sensorNode.reportAccelEvent();  
//        _sensorNode._ccPacketHandler.buildAccelPacket(1,2,31, _accel._deltaX, _accel._deltaY, _accel._deltaZ) ;
          _accel.update();

        
        accelValues[0] = highByte(_accel._X);
        accelValues[1] = lowByte(_accel._X);
        accelValues[2] = highByte(_accel._Y);
        accelValues[3] = lowByte(_accel._Y);
        accelValues[4] = highByte(_accel._Z);
        accelValues[5] = lowByte(_accel._Z);
         
        _sensorNode._ccPacketHandler.buildAccelPacket(1,2,31, accelValues) ;
        _sensorNode.ccSendPacket();

//    }
    
    
//    if(_batteryIsLow)
//    {
//        _sensorNode.reportLowBatt(); 
//        disableLowBattInterrupt();         
//    }
    
   
    
}

