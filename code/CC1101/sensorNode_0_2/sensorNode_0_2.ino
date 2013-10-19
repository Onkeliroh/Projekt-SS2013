#include "EEPROM.h"
#include "analogComp.h"
#include "ccSensorNode.h"
#include "accel.h"

#define SENSOR_NODE_ID              2
#define TWIN_NODE_ID                3

#define KIDNEY_NEIGHBOR             4
#define KIDNEY_RSSI_THRESHOLD     200

#define EGG_NEIGHBOR                6
#define EGG_RSSI_THRESHOLD        -50     //PEAR threshold for EGG is -55

#define ACCEL_CHECK_PERIOD         22
#define SHAKE_INTERVAL           1500 
#define LAST_MESSAGE_TIMEOUT   100000 


#define enableRFChipInterrupt()     attachInterrupt(0, RFChipInterrupt, FALLING);
#define disableRFChipInterrupt()    detachInterrupt(0);

#define enableaLowBattInterrupt()     analogComparator.enableInterrupt(lowBattInterrupt, FALLING);
#define disableLowBattInterrupt()     analogComparator.disableInterrupt();

/////////////////////
//--- INSTANCES ---//
/////////////////////

CCSENSORNODE _sensorNode = CCSENSORNODE(SENSOR_NODE_ID, TWIN_NODE_ID);
ACCEL _accel = ACCEL(10,100);

///////////////////
//--- MEMBERS ---//
///////////////////

boolean _packetAvailable = false;

boolean _batteryIsLow = false;

enum state{
    motionless,
    shaken,
    kicked};

state pearState = motionless;
    
unsigned long _lastTimeAccelCheck = 0;
unsigned long _lastMssgTime = 0;

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
//    enableaLowBattInterrupt();
    enableRFChipInterrupt(); 
    delay(5); //For the batteryLow System
}


/////////////////////
//--- MAIN LOOP ---//
/////////////////////

// The loop method gets called on and on after the start of the system.
void loop()
{
     
//    if(_batteryIsLow)
//    {
//        _sensorNode.reportLowBatt();
//
//        updateLastMssgTimestamp();
//
//        disableLowBattInterrupt();         
//    }
    
    if(_packetAvailable)
    {
        disableRFChipInterrupt();
        
        if(_sensorNode.ccGetNewPacket())
        {
            _sensorNode.ccHandle();  
        }
        else
        {
            if(!_sensorNode.isPacketsSender())
            {
//                _sensorNode.reportRSSI();   
              
              
               _sensorNode.storeNeighborRSSI();    
              
               if(_sensorNode.neighborSender() == KIDNEY_NEIGHBOR) 
               {
                   if(_sensorNode.neighborIsClose(KIDNEY_RSSI_THRESHOLD))
                   {
                       _sensorNode.reportRSSI(); 
                       updateLastMssgTimestamp();

                   } 
               }
               else
               {
                   if(_sensorNode.neighborSender() == EGG_NEIGHBOR) 
                   {
                       if(_sensorNode.neighborIsClose(EGG_RSSI_THRESHOLD))
                       {
                           _sensorNode.reportRSSI(); 
                           updateLastMssgTimestamp();
                       } 
                    } 
               }
                              
               
            }          
        }
        
        _packetAvailable = false;  
        
        enableRFChipInterrupt();  
    }
    else 
    {
        if(millis() - _lastTimeAccelCheck > ACCEL_CHECK_PERIOD )
        {
            updateLastAccelCheckTime();
              
            if(_accel.bufferIsFull())
            {
                _accel.resetBufferIndex();
           
                _accel.setAccelDelta();
                
                checkAccelEventAndReport();
           
                _accel.resetBuffers();               
                  
            }
            else 
            {
                _accel.readAccel();              
                  
            }
        }
               
    }  
            
        
    if(longTimeSinceLastMssg())
    {
        reportInRange(); 
        updateLastMssgTimestamp(); 
    }                
          
}




void checkAccelEventAndReport()
{
       if((_accel.wasShaken()) && (pearState != shaken))
       {
           _sensorNode.reportShakeEvent();
           
           updateLastMssgTimestamp();
           
           pearState = shaken;
       }
       else
       {
           if(_accel.wasKicked() && (pearState != kicked))
           {
               _sensorNode.reportKickEvent();
               
               updateLastMssgTimestamp();
               
               pearState = kicked;
           }
           else 
           {
               pearState = motionless;              
           }
         
       }           
                
}


boolean longTimeSinceLastMssg()
{
    boolean _longTimeHasPassed = false;      
  
    if(millis() - _lastMssgTime > LAST_MESSAGE_TIMEOUT ) 
    {
         _longTimeHasPassed = true; 
    }
    
    return _longTimeHasPassed;
  
}

void reportInRange()
{
    _sensorNode.sendInRangePacket();
    
}

void updateLastAccelCheckTime()
{
    _lastTimeAccelCheck = millis();  
}


void updateLastMssgTimestamp()
{
    _lastMssgTime = millis();  
}
