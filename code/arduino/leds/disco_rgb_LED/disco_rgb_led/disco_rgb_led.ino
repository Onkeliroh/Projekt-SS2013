#include <cstdlib>

const int ledPin1 = 9;
const int ledPin2 = 10;
const int ledPin3 = 11;

void setup()  { 
  // nothing happens in setup 
} 

void loop()  { 
	for(int fadeValue = 0 ; fadeValue <= 150; fadeValue +=5){
		analogWrite(ledPin1, fadeValue);         
		delay(30);
	}
	for(int fadeValue = 0 ; fadeValue <= 150; fadeValue +=5){
		analogWrite(ledPin2, fadeValue);
		delay(30);
	}
	for(int fadeValue = 0 ; fadeValue <= 150; fadeValue +=5){
		analogWrite(ledPin3, fadeValue);
		delay(30);
	}
    // wait for 30 milliseconds to see the dimming effect    
    delay(500);                            


	for(int fadeValue = 150 ; fadeValue >= 0; fadeValue -=5){
		analogWrite(ledPin1, fadeValue);         
	    delay(30);                            
	}
	for(int fadeValue = 150 ; fadeValue >= 0; fadeValue -=5){
		analogWrite(ledPin2, fadeValue);         
	    delay(30);                            
	}
	for(int fadeValue = 150 ; fadeValue >= 0; fadeValue -=5){
		analogWrite(ledPin3, fadeValue);         
		delay(30);
	}
    delay(500);                            

}


