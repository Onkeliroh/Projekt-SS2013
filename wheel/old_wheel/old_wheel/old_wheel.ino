/**
 * Wheel with magnets
 * Author: Jenny Gonzalez
 * Last Changes: 21.06.2012
 * Features:
 * Magnets are detected using pin interruptions 
 */

const float wheel_radius = 3.75;    //Wheel radius = 3.75 cm
const float pi = 3.14;           

const int hallSensor1 = 2;
const int hallSensor2 = 3;

unsigned int samplePeriod = 1500;  //The sample period is set to 2 second (2000 ms)
unsigned int minute = 60000;      //60 000 ms equals a minute

boolean states1[ 2 ][ 400 ];
int steps[ 200 ];

String wheel_direction = "undefined";
String wheel_direction = "undefined";

const int trace_der[] = { 0,1,3,1,3,2,3,2 };   //This trace appears when turning the wheel CW (clockwise)
const int trace_izq[] = { 0,2,3,2,3,1,3,1 };   //This trace appears when turning the wheel CCW

unsigned long  current_time = 0; //Since the function millis() returns an unsigned long value, this variable has to be of the same type
unsigned long  last_time = 0;    //The same reason as in the previous line
unsigned long  elapsed_time = 0;

int count = 0;
float rpm = 0;              //Number of revolutions per minute
float wheel_speed = 0;      //The units of this speed will be m/min

void setup()
{
  Serial.begin( 9600 );        //  setup serial
  Serial.println( "Started" );
  
  attachInterrupt( 0, rpm_interrupt1, CHANGE ); 
  attachInterrupt( 1, rpm_interrupt2, CHANGE );  
    
  pinMode( hallSensor1, INPUT);
  pinMode( hallSensor2, INPUT);
  
}

void loop()
{
  current_time = millis(); 
  elapsed_time = current_time - last_time;
  
  if ( elapsed_time >= samplePeriod )   
  { 
  
     detachInterrupt( 0 ); //Disable interruptions
     detachInterrupt( 1 );
     
     if ( count > 12 )
     {  
       
       
       
       
       
//       Serial.println( "Trace's start" );  
//       
//        for( int col=0; col < count; col++ )
//       {
//           steps[ col ] = int (states1[ 1 ][ col ])*2 + int( states1[ 0 ][ col ] )*1; //Convert binary readings into decimal numbers
//           Serial.print( steps[ col ] );           
//       }
//       
//       Serial.println( "Trace's end" );  
//       
//              
//       int col=0;
//       while( ( wheel_direction == "undefined" ) && ( col < count-4 ) )
//       {           
//          if ( steps[ col ] == 0 )
//          {
//             if( (steps[ col +1 ] == trace_der[ 1 ]) && (steps[ col +2 ] == trace_der[ 2 ]) && (steps[ col +3 ] == trace_der[ 3 ])  )
//             wheel_direction="right";
//             else 
//             {
//              if( (steps[ col +1 ] == trace_izq[ 1 ]) && (steps[ col +2 ] == trace_izq[ 2 ]) && (steps[ col +3 ] == trace_izq[ 3 ])   )
//              wheel_direction="left";
//             } 
//           } 
//           col++;  
//         }    
                
        Serial.println( wheel_direction );           
      }
      else
      {
        Serial.println( "Too slow or stopped");  
      }

         
     
     Serial.print("Wheel's speed:  "); Serial.print( wheel_speed ); Serial.println("  m/min  ");
        
     count = 0;  
     
     elapsed_time = 0;
       
     last_time = millis();     // Uptade last_time
     
      
     wheel_direction="undefined";
     
     attachInterrupt( 0, rpm_interrupt1, CHANGE );    //enable interrupt again   
     attachInterrupt( 1, rpm_interrupt2, CHANGE );
     
   }   
  
}


int find_speed()
{
  
  rpm = ( count / 20 ) * ( minute / samplePeriod );
     
  wheel_speed = ( ( 2 * pi * wheel_radius ) / 100  ) * rpm;  //By dividing by 100 be get the units m/min
 
  return wheel_speed;
}

String find_direction()
{
   
  
  for( int col=0; col < count; col++ )
   {
       steps[ col ] = int (states1[ 1 ][ col ])*2 + int( states1[ 0 ][ col ] )*1; //Convert binary readings into decimal numbers          
   }
               
   int col=0;
   while( ( wheel_dir == "undefined" ) && ( col < count-4 ) )
   {           
      if ( steps[ col ] == 0 )
      {
         if( (steps[ col +1 ] == trace_der[ 1 ]) && (steps[ col +2 ] == trace_der[ 2 ]) && (steps[ col +3 ] == trace_der[ 3 ])  )
         wheel_dir = "right";
         else 
         {
          if( (steps[ col +1 ] == trace_izq[ 1 ]) && (steps[ col +2 ] == trace_izq[ 2 ]) && (steps[ col +3 ] == trace_izq[ 3 ])   )
          wheel_dir = "left";
         } 
       } 
       col++;  
     }    
 
   return wheel_dir;   
}



void rpm_interrupt1()
{
  states1[ 0 ][ count ] = digitalRead( hallSensor1 );   //To determine direction
  states1[ 1 ][ count ] = digitalRead( hallSensor2 );
  count++; 
}

void rpm_interrupt2()
{
  states1[ 0 ][ count ] = digitalRead( hallSensor1 );   //To determine direction
  states1[ 1 ][ count ] = digitalRead( hallSensor2 );  
  count++;    
}
