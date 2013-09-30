    #include "PinChangeInt.h"
    
    const int hallSensor1 = 4;
    const int hallSensor2 = 5;
    
    const int greenDirec = 8;
    const int redDirec = 9;
    
    boolean states1[ 2 ][ 8 ];
    int steps[8];
    
    boolean compare1 = false;
    boolean compare2 = false;
    boolean compare3 = false;
    boolean compare4 = false;
    
    String wheel_direction = "undefined";
    
    const int trace_der1[] = { 3,1,3,0 }; //Traces depend on the sensors' positions and magnets' location on the wheel
    const int trace_der2[] = { 1,3,0,3 };
    const int trace_der3[] = { 3,0,3,1 };
    const int trace_der4[] = { 0,3,1,3 };
    
    const int trace_izq1[] = { 3,1,2,1 };
    const int trace_izq2[] = { 1,2,1,3 };
    const int trace_izq3[] = { 2,1,3,1 };
    const int trace_izq4[] = { 1,3,1,2 };
    
    int traceSize = 4;
    
    int count = 0;
    
    boolean testled = false;
    
    boolean sensorupdated = false;
    
    void setup()
    {
      
      pinMode( greenDirec, OUTPUT );
      pinMode( redDirec, OUTPUT );
      digitalWrite( greenDirec, LOW );
      digitalWrite( redDirec, LOW );
        
      pinMode( hallSensor1, INPUT ); 
      digitalWrite( hallSensor1, HIGH ); //To enable an internal 20K pullup resistor (see http://arduino.cc/en/Reference/DigitalWrite)
      PCintPort::attachInterrupt( hallSensor1, &rpm_interrupt1, CHANGE );  //Panstamp needs "PinChangeInt.h" to create interruptions
      //attachInterrupt( 0, rpm_interrupt1, CHANGE ); //  For arduino UNO, since it doesn't need "PinChangeInt.h"
      
      pinMode( hallSensor2, INPUT ); 
      digitalWrite( hallSensor2, HIGH ); //To enable an internal 20K pullup resistor (see http://arduino.cc/en/Reference/DigitalWrite)
      
      Serial.begin( 9600 );   //  setup serial
      
    }

    void loop()
    {
          if( sensorupdated) 
          {     
             PCintPort::detachInterrupt( hallSensor1 ); //Panstamp needs "PinChangeInt.h" to create interruptions
             //detachInterrupt( hallSensor1 );
             
             for( int col=0; col <= 3; col++ )
              {           
                                  
                  compare1 = compareArrays( trace_der1, steps, traceSize );
                  compare2 = compareArrays( trace_der2, steps, traceSize );
                  compare3 = compareArrays( trace_der3, steps, traceSize );
                  compare4 = compareArrays( trace_der4, steps, traceSize );
                           
                  if( compare1 || compare2 || compare3 || compare4 )
                  {
                     wheel_direction="right";
                     digitalWrite( greenDirec, LOW);
                     digitalWrite( redDirec, HIGH );
                     
                     break;
                   } 
                   else
                   { 
                     compare1 = compareArrays( trace_izq1, steps, traceSize );
                     compare2 = compareArrays( trace_izq2, steps, traceSize );
                     compare3 = compareArrays( trace_izq3, steps, traceSize );
                     compare4 = compareArrays( trace_izq4, steps, traceSize );
                  
                     if( compare1 || compare2 || compare3 || compare4 )
                     {    
                      wheel_direction="left";          
                                  
                      digitalWrite( greenDirec, HIGH );
                      digitalWrite( redDirec, LOW );                 
                      
                      break;
                
                     } 
                     else {
                       
                     wheel_direction="undefined";
                     digitalWrite( greenDirec, LOW );
                     digitalWrite( redDirec, LOW );
                   
                     }  
                   }              
                 }                  
             PCintPort::attachInterrupt( hallSensor1, &rpm_interrupt1, CHANGE );  //Panstamp needs "PinChangeInt.h" to create interruptions
             //attachInterrupt( 0, rpm_interrupt1, CHANGE );  // For arduino UNO, since it doesn't need "PinChangeInt.h"
           }
           sensorupdated = false;  
    }
    
    
    void rpm_interrupt1()
    {
      states1[ 0 ][ count ] = digitalRead( hallSensor1 );   //To determine direction
      states1[ 1 ][ count ] = digitalRead( hallSensor2 );
      steps[ count ] = int (states1[ 0 ][ count ])*2 + int( states1[ 1 ][ count ] )*1; 
      
      count++; 
      if( count > traceSize -1 ) 
      {
        count=0;           
      }
      
      sensorupdated = true;
    }
    
    boolean compareArrays( const int *traceArray, int *readArray, int sizeArray )
    {
      boolean areEqual = true;  //We start on the assumption that both arrays are equal
      int matches = 0;
      int index = 0;
      
      while ( ( areEqual ) && ( index < sizeArray ) )
      {
        if ( traceArray[ index ] == readArray[ index ]  )
        {
          index++;
        }
        else 
        {
          areEqual = false;
        }  
      }
      
      return areEqual; 
      
    }
