#ifndef CCSERVER_H

#define CCSERVER_H

#include "ccPacketHandler.h"
#include "ccNode.h"

#define SERVERDEBUG 1
#undef SERVERDEBUG  // Comment this out to run the library in debug mode (verbose messages)

#define BYPASS_JAVASERVER 1
#undef BYPASS_JAVASERVER //Comment this out when not interacting with Javaserver

#define BUFFERLENGTH 5

#define RSSI_OFFSET 74 

#define BUFFERHASH 4 

//According to the Design Note DN505 http://www.ti.com/lit/an/swra114d/swra114d.pdf

class CCSERVER: public CCNODE
{

	public:
                CCSERVER(byte id);
                ~CCSERVER();

                void cleanBuffer();
                void setup();  
                boolean ccReceive(void);
                void ccHandle(void); 
                boolean isSender();
                int  ccRSSI(byte rawRSSI);
                void distanceAlert(void);
                void lowBatteryAlert(void);

                void saveDataInBuffer();
                void setBufferHash();
                void setNearNodeBuffer();
                void setBuffer();
                void sendBufferToJavaServer();

                void setRandomBuffer(); //For testing.   

       private:
                byte buffer[BUFFERLENGTH];                
};

#endif // CCSERVER_H
