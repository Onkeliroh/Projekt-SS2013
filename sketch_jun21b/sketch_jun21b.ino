
const float wheel_radius = 3.75;    //Wheel radius = 3.75 cm
const float pi = 3.14;           

const int hallSensor1 = 2;
const int hallSensor2 = 3;

unsigned int samplePeriod = 10;  //The sample period is set to 2 second (2000 ms)
unsigned int minute = 60000;      //60 000 ms equals a minute

boolean states1[ 2 ][ 8 ];
int steps[8];

String wheel_direction = "undefined";

const int trace_der[] = { 3,1,2,0 };
const int trace_izq[] = { 3,1,2,1 };


unsigned long  current_time = 0; //Since the function millis() returns an unsigned long value, this variable has to be of the same type
unsigned long  last_time = 0;    //The same reason as in the previous line
unsigned long  elapsed_time = 0;

int count = 0;
float rpm = 0;              //Number of revolutions per minute
float wheel_speed = 0;      //The units of this speed will be m/min
int direc = 0;             // direc = 1 You are going to the right   direc = 2 You are foing to the left

boolean testled = false;

void setup()
{
  Serial.begin( 19200 );   //  setup serial
  Serial.println( "Started" );
  
  attachInterrupt( 0, rpm_interrupt1, CHANGE ); 
  //attachInterrupt( 1, rpm_interrupt2, CHANGE );  
    
  pinMode( hallSensor1, INPUT);
  pinMode( hallSensor2, INPUT);
  pinMode( 13, OUTPUT);
  pinMode( 12, OUTPUT);
  pinMode( 11, OUTPUT);
}

boolean sensorupdated = false;

void loop()
{
//  current_time = millis(); 
//  elapsed_time = current_time - last_time;
//  
//  if ( elapsed_time >= samplePeriod )   //Uptade every one second, this will be equal to reading frecuency (Hz)
//  { 
//  
//     detachInterrupt( 0 ); //Disable interrupt when calculating
//     detachInterrupt( 1 );
     
//     Serial.println( "Start trace:" );
//          

//     
//     Serial.println( "End trace" );     
   
     
//     if ( count > 4 )
//     {  
       
//       for( int col=0; col <= count; col++ )
//       {
//           steps[ col ] = int (states1[ 1 ][ col ])*2 + int( states1[ 0 ][ col ] )*1;          
//           Serial.print( steps[ col ] ); 
//               
//       }
      if( sensorupdated) 
      {     

         for( int col=0; col < 7; col++ )
         {
            Serial.print( steps[col] ) ;          
            Serial.print( " " ) ;     
         }
             Serial.println() ; 

         for( int col=0; col < 4; col++ )
          {           
            
               if( (steps[ col +0 ] == trace_der[ 0 ]) && (steps[ col +1 ] == trace_der[ 1 ]) && (steps[ col +2 ] == trace_der[ 2 ]) && (steps[ col +3 ] == trace_der[ 3 ])  ){
                 wheel_direction="right";
                 
                 break;
               } else if( (steps[ col +0 ] == trace_izq[ 0 ])  && (steps[ col +1 ] == trace_izq[ 1 ]) && (steps[ col +2 ] == trace_izq[ 2 ]) && (steps[ col +3 ] == trace_izq[ 3 ])   )
               {              
                wheel_direction="left";
                
                break;
          
               } else {
                 wheel_direction="undefined";
               
                 }
                 
             }   
               
                  
          Serial.println( wheel_direction ); 
       }
       sensorupdated = false;
//      }
//      
// 
              
//     rpm = ( count / 8 ) * ( minute / samplePeriod );
//     
//     wheel_speed = ( ( 2 * pi * wheel_radius ) / 100  ) * rpm;  //By dividing by 100 be get the units m/min
//     
//     Serial.print("RPM  "); Serial.println( rpm ); 
//     Serial.print("Wheel's speed:  "); Serial.print( wheel_speed ); Serial.println("  m/min  ");
//        
//     count = 0;  
     
//     elapsed_time = 0;       
//     last_time = millis();     // Uptade last_time
           
 //    wheel_direction = "undefined";
     
//     attachInterrupt( 0, rpm_interrupt1, CHANGE );    //enable interrupt again   
//     attachInterrupt( 1, rpm_interrupt2, CHANGE );
     
//   }   
  
}

void rpm_interrupt1()
{
   if(testled){
    digitalWrite(13,HIGH);
    testled = false;
  } else {
    digitalWrite(13,LOW);
    testled = true;
  }
  digitalWrite(11,digitalRead( hallSensor1 ));
  digitalWrite(12,digitalRead( hallSensor2 ));
  
  detachInterrupt( 0 );   
  states1[ 0 ][ count ] = digitalRead( hallSensor1 );   //To determine direction
  states1[ 1 ][ count ] = digitalRead( hallSensor2 );
  steps[ count ] = int (states1[ 0 ][ count ])*2 + int( states1[ 1 ][ count ] )*1; 
  count++; 
  if( count > 6 ) 
  {
    count=0;    
  }
  sensorupdated = true;
  attachInterrupt( 0, rpm_interrupt1, CHANGE );
}

//void rpm_interrupt2()
//{
//  states1[ 0 ][ count ] = digitalRead( hallSensor1 );   //To determine direction
//  states1[ 1 ][ count ] = digitalRead( hallSensor2 );  
//  count++;    
//  if( count > 7) count=0;
//}
