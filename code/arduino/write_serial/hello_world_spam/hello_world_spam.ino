/*
  Hello World Spam
 */
 
 int i = 0;
 
 void setup()
 {
   Serial.begin(9600);
 }
 
 void loop()
 {
   
   Serial.print("Hello World!\t");
   Serial.println(i);
   ++i;
 }
