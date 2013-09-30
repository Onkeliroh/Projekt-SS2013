#include "ccNode.h"


//DESTRUCTOR//

CCNODE::~CCNODE(void)
{


}


//CCNODE class methods
void CCNODE::ledBlinkSetup()

{
    Serial.begin(BAUDRATE); // 9600 // 38400
    pinMode(LEDOUTPUT, OUTPUT); // setup the blinker output
    digitalWrite(LEDOUTPUT, LOW);   

}

void CCNODE::rfChipInit(void)

{
    _cc1101.init(); // initialize the RF Chip
    _cc1101.setCarrierFreq(CFREQ_868); // Set carrier frequency to default 868 or 915 MHz
    _cc1101.setSyncWord(&_syncWord, false);  
    _cc1101.setDevAddress(_id, false); // this device address need to match the target address in the sender
    _cc1101.enableAddressCheck(); // you can skip this line, because the default is to have the address check enabled
    _cc1101.setRxState(); 
	
}


void CCNODE::ccSendPacket(void)

{

    CCPACKET ccPacket = _ccPacketHandler.getPacket();
  
    if(_cc1101.sendData(ccPacket))
    {
        
        _ccPacketHandler._ccClear = false;

    }
    else
    {
        //Serial.println("ERROR! - Failed to send packet.");  
    }  
 
}


void CCNODE::ccAcknowledge(void)

{
    _ccPacketHandler.acknowledge(); 
    ccSendPacket(); 
    _ccPacketHandler._ccClear = true;

}

void CCNODE::ccPrintPacket(void)

{

    _ccPacketHandler.printPacket();

}


void CCNODE::ledBlink()

{

    digitalWrite(LEDOUTPUT, HIGH);
    delay(BLINKTIME);
    digitalWrite(LEDOUTPUT, LOW);
    delay(BLINKTIME);

}






