#ifndef CCSERVER_H

#define CCSERVER_H

#include "ccPacketHandler.h"
#include "ccNode.h"

#define SERVERDEBUG 1
#undef SERVERDEBUG  // Comment this out to run the library in debug mode (verbose messages)

#define BYPASS_JAVASERVER 1
#undef BYPASS_JAVASERVER //Comment this out when not interacting with Javaserver

#define BUFFERLENGTH 4

#define RECEIVERID  buffer[0]
#define METAKEY     buffer[1]
#define COLOR1      buffer[2]
#define COLOR2      buffer[3] 

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
                boolean ccGetNewPacket(void);
                void ccHandle(void); 
                int  ccRSSI(byte rawRSSI);
                void showRSSI();
                void lowBatteryAlert(void);

                void saveDataInBuffer();
                void setBuffer();
                void setNearNodeBuffer();
                void sendBufferToJavaServer();
                boolean newJavaCommand();
                void getJavaCommand();
                void setNewCommand();
                void ccSendCommand();

                void TestBuffer1();
                void TestBuffer2();
               
                byte buffer[BUFFERLENGTH];                  
};

#endif // CCSERVER_H
