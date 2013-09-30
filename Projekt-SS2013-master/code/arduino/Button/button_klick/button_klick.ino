const int pin_button = 12;
const int pin_button_source = 13;

const int leds[] = {9, 10, 11};

void setup()
{
	Serial.begin(9600);
	pinMode(pin_button, INPUT); 
	pinMode(pin_button_source, OUTPUT); 
	
	pinMode(leds[1], OUTPUT);
	pinMode(leds[2], OUTPUT);
	pinMode(leds[3], OUTPUT);
	
	digitalWrite(pin_button_source, HIGH);
}

void loop()
{
	if ( digitalRead(pin_button) == HIGH)
	{
		digitalWrite(leds[1], HIGH);
		digitalWrite(leds[2], HIGH);
		digitalWrite(leds[3], HIGH);
	}
	else
	{
		digitalWrite(leds[1], LOW);
		digitalWrite(leds[2], LOW);
		digitalWrite(leds[3], LOW);
	}
	delay(100);
}
