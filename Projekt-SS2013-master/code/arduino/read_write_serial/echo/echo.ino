/*
  Hello World Spam
 */
 
 int led = 13;
 
 void setup()
 {
   Serial.begin(115200);
   pinMode(led, OUTPUT);
 }
 
 void loop()
 {
   digitalWrite(led, LOW);
   if (Serial.available() > 0)
   {
     digitalWrite(led, HIGH);
     int input;
     input = Serial.read();
     Serial.print((char)input);
   }
   delay(100);
 }
