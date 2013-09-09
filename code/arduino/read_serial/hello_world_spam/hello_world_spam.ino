/*
  Hello World Spam
 */
 
 const int LED = 13;
 
 void setup()
 {
   Serial.begin(9600);
   pinMode(LED,OUTPUT);
 }
 
 void loop()
 {
    digitalWrite(LED,LOW);
    Serial.print(3);
    Serial.print(3);
    Serial.print(4);
    Serial.println(8);
    delay(1000);
    
    
    
    if ( Serial.available() )
    {
      digitalWrite(LED,HIGH);
      delay(100);
      digitalWrite(LED,LOW);
    }
    
      
 }
