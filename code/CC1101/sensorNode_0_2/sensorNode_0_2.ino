#include "EEPROM.h"
#include "analogComp.h"
#include "ccSensorNode.h"
#include "accel.h"

#define SENSOR_NODE_ID                2
#define TWIN_NODE_ID                  3

#define KIDNEY_NEIGHBOR               4
#define KIDNEY_RSSI_THRESHOLD       -65  //Kidney powered by AA batteries

#define EGG_NEIGHBOR                  6
#define EGG_RSSI_THRESHOLD          -60  //Egg powered by AA batteries  

#define NEIGHBOR_A                     KIDNEY_NEIGHBOR
#define NEIGHBOR_A_THRES               KIDNEY_RSSI_THRESHOLD

#define NEIGHBOR_B                     EGG_NEIGHBOR 
#define NEIGHBOR_B_THRES               EGG_RSSI_THRESHOLD 

#define ACCEL_CHECK_PERIOD           22
#define STATE_CHANGE_INTERVAL      2000  // send message state each 2 secs even though the state hasn't change during this time
#define LAST_MESSAGE_TIMEOUT      30000 

#define enableRFChipInterrupt()        attachInterrupt(0, RFChipInterrupt, FALLING);
#define disableRFChipInterrupt()       detachInterrupt(0);

#define enableaLowBattInterrupt()      analogComparator.enableInterrupt(lowBattInterrupt, FALLING);
#define disableLowBattInterrupt()      analogComparator.disableInterrupt();

/////////////////////
//--- INSTANCES ---//
/////////////////////

CCSENSORNODE _sensorNode = CCSENSORNODE(SENSOR_NODE_ID, TWIN_NODE_ID);
ACCEL _accel = ACCEL(10,95);

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
unsigned long _lastStateChangeTime = 0;

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
    delay(5); //For the batteryLow section
    
}


/////////////////////
//--- MAIN LOOP ---//
/////////////////////

// The loop method gets called on and on after the start of the system.
void loop()
{
     
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
                _sensorNode.storeNeighborRSSI();    
              
                if(oneNeighborIsClose())
                {
                    _sensorNode.reportRSSI(); 
                    updateLastMssgTimestamp();                  
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


    if(_batteryIsLow)
    {
        _sensorNode.reportLowBatt();

        updateLastMssgTimestamp();

        disableLowBattInterrupt();         
    }    
          
}




void checkAccelEventAndReport()
{
       if(_accel.wasShaken())
       {
         if((pearState != shaken) || (millis() - _lastStateChangeTime > STATE_CHANGE_INTERVAL) )
         {
             _sensorNode.reportShakeEvent();
           
             updateLastMssgTimestamp();
           
             pearState = shaken;
           
             _lastStateChangeTime = millis();
           
          }
       }   
       else
       {
           if(_accel.wasKicked())
           {
             if((pearState != kicked) || (millis() - _lastStateChangeTime > STATE_CHANGE_INTERVAL) )
             {
                 _sensorNode.reportKickEvent();
               
                 updateLastMssgTimestamp();
               
                 pearState = kicked;
               
                 _lastStateChangeTime = millis();       
              }                        
           }
           else 
           {
               pearState = motionless;              
           }
         
       }           
                
}

boolean oneNeighborIsClose()
{
    boolean oneNeighborIsClose = false;
    
    if( (_sensorNode.isNeighborClose(NEIGHBOR_A, NEIGHBOR_A_THRES)) || (_sensorNode.isNeighborClose(NEIGHBOR_B,NEIGHBOR_B_THRES)) )
    {
        oneNeighborIsClose = true;     
    }
  
    return oneNeighborIsClose;  
  
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
