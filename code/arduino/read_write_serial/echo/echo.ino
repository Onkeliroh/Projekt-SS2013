/*
  Hello World Spam
 */
 
 void setup()
 {
   Serial.begin(9600);
 }
 
 void loop()
 {
   if (Serial.available() > 0)
   {
     int input;
     input = Serial.read();
     Serial.print((char)input);
   }
 }
