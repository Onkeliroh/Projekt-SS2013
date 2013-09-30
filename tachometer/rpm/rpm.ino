/*
  RPM reader (edge detection)
 
 This example work allows the measurement of RPM by detecting
 falling edges in the Voltage Output pin of a Hall effect switch	
 
 The circuit:
 * 1.2K resistor between pins 1(Vs) and 3(Q) of the Hall effect switch
 * To which pin is the hall effect effect switch attached??????????????????
  
 created  29 May 2013
 by Jenny Gonzalez
 
 */

#include "rpm.h"

const int vQPin = 4; //To which pin is the hall effect effect switch attached?????? 
                     //Remember that the chosen interruption pin is 2!!!!!!!!!!!!!!
                                                         
const int samplePeriod = 1000;  //The sample period is set to 1 second (1000 ms)


volatile unsigned long  current_time=0; //Since the function millis() returns an unsigned long value, this variable has to be of the same type
volatile unsigned long  last_time=0;  //The same reason as in the previous line
   
volatile prevState = LOW;
volatile int rpm;
volatile int rpm_count;

void initialization()
{

  attachInterrupt(0, rpm_interrupt, CHANGE); //  attachInterrupt(interrupt, function, mode)   
                                             //  Arduino boards have two external interrupts: numbers 0 (on digital pin 2) and 1 (on digital pin 3)
                                             //  Mode can be: LOW, CHANGE, RISING, FALLING
                                             //  See : http://www.arduino.cc/en/Reference/AttachInterrupt
}

void main()
{
  
 current_time = millis(); 
 
 if ( current_time - last_time == samplePeriod )   //Uptade every one second, this will be equal to reading frecuency (Hz)
 { 

   detachInterrupt(0);//Disable interrupt when calculating

   rpm = rpm_count * (60/7); // Convert frecuency to RPM, note: this works for one interruption per full rotation. For two interrups per full rotation use rpmcount * 30.

   rpm_count = 0;            // Restart the RPM counter
   last_time = millis();     // Uptade last_time
   attachInterrupt(0, rpm_interrupt, CHANGE);    //enable interrupt again
  
}

void rpm_interrupt()
{
    rpm_count++;  
}
