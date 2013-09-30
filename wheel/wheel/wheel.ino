/*
 Hall effect sensor reading
*/

#include <TimerOne.h>

const int hallSensor1 = 2;
const int hallSensor2 = 3;

const int samplePeriod = 2000;  //The sample period is set to 2 second (2000 ms)  

boolean prev_state1 = 0;
boolean prev_state2 = 0;
boolean curr_state1 = 0;
boolean curr_state2 = 0;
boolean states[ 2 ][ 300 ];


unsigned long  current_time = 0; //Since the function millis() returns an unsigned long value, this variable has to be of the same type
unsigned long  last_time = 0;    //The same reason as in the previous line


int count = 0;

void setup()
{
  Serial.begin( 9600 );   //  setup serial
       
  pinMode( hallSensor1, INPUT);
  pinMode( hallSensor2, INPUT);
  
  Timer1.initialize(20); // set a timer of length 10 microseconds (or 0.0001 sec  )
  Timer1.attachInterrupt( timerIsr ); // attach the service routine here
  
}

void loop()
{
  current_time = millis(); 
  
  if ( current_time - last_time >= samplePeriod )   //this will be equal to reading frecuency (Hz)
  {    
     for( byte col=0; col < count; col++ )
     {
       Serial.print( states[ 0 ][ col ] ); Serial.print( states[ 1 ][ col ] ); Serial.print("  ");            
     }
               
     Serial.print("Edges  "); Serial.println( count ); 
        
     count = 0;  
       
     last_time = millis();     // Uptade last_time    
   }     
}


void timerIsr()
{    
     curr_state1 = digitalRead( hallSensor1 );
     curr_state2 = digitalRead( hallSensor2 );
     
     if( ( curr_state1 != prev_state1 ) || ( curr_state2 != prev_state2 ) )
     {
       states[ 0 ][ count ] = curr_state1; 
       states[ 1 ][ count ] = curr_state2; 
       count++;         
      }
      
     prev_state1 = curr_state1;
     prev_state2 = curr_state2;
    
}

