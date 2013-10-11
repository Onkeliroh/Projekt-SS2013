#ifndef CCSERVER_H

#define CCSERVER_H

#include "ccPacketHandler.h"
#include "ccNode.h"

#define BUFFERLENGTH 4

//From Java Server to Panstamp
#define RECEIVERID  buffer[0]
#define METAKEY     buffer[1]
#define COLOR1      buffer[2]
#define COLOR2      buffer[3] 

//From Panstamp to Java Server
#define SENDERID    0
#define KEY         1
#define DUMMY       2
#define NEARNODEID  2
#define CHECKSUM    3 

#define RSSI_OFFSET 74 

#define BUFFERHASH 4 

#define NULL 0


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
                void checkRSSI();
                void lowBatteryAlert(void);

                void setBuffer();
                void setNearNodeBuffer();
                void sendBufferToJavaServer();
                boolean newJavaCommand();
                void getJavaCommand();
                void setNewCommand();
                void ccSendCommand(); 
                byte getBufferChecksum();
                   
                void setTestColorCommand(byte COLORR, byte COLORB, byte COLORG);
                void sendColorCommand(byte red1, byte blue1, byte green1);

  
         
                byte buffer[BUFFERLENGTH];                  
};

#endif // CCSERVER_H
