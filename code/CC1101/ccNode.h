#ifndef CCNODE_H

#define CCNODESH

#include "ccPacketHandler.h"
#include "ccpacket.h"
#include "cc1101.h"

#define LEDOUTPUT   4     //For the ledBl‌ink function (physical panStamp pin 19)
#define BLINKTIME 100     // blink-time in ms


class CCNODE

{

	public:
        	///destructor
        	~CCNODE(void);
                
                virtual void setup(void) = 0;  
                void  ledBlinkSetup();
                void  rfChipInit();
                void  ccSend();
                void  ccAcknowledge();
                void  ccPrintPacket();
                void  ledBlink();
                
                virtual boolean ccReceive(void) = 0;
                virtual void ccHandle(void)  = 0;

                byte _id;  
                byte _twinNode;
                byte _syncWord;                  
                CC1101 _cc1101;                           
                ccPacketHandler _ccPacketHandler;
                                                
};

#endif // CCNODE_H
