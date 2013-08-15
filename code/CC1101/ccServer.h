#ifndef CCSERVER_H

#define CCSERVER_H

#include "ccPacketHandler.h"
#include "ccNode.h"


#define RSSI_OFFSET 74 
//According to the Design Note DN505 http://www.ti.com/lit/an/swra114d/swra114d.pdf

class CCSERVER: public CCNODE
{

	public:
                CCSERVER(byte id);
                ~CCSERVER();

                void setup();  
                boolean ccReceive(void);
                void ccHandle(void); 
                boolean isSender();
                int  ccRSSI(byte rawRSSI);
                void distanceAlert(void);
                void lowBatteryAlert(void);               
};

#endif // CCSERVER_H
