const int led[] = {9, 10, 11};

//methods
void shine_all();
void shine(int);
void dimm_all();

//

void setup()
{
	pinMode(led[0], OUTPUT);
	pinMode(led[1], OUTPUT);
	pinMode(led[2], OUTPUT);
}

void loop()
{
	shine_all();
	delay(100);
	
	dimm_all();
	delay(100);
	
	shine(0);
	delay(50);
	shine(1);
	delay(50);
	shine(2);
	delay(50);
	
	
	
	delay(100);
}

void shine_all()
{
	digitalWrite(led[0], HIGH);
	digitalWrite(led[1], HIGH);
	digitalWrite(led[2], HIGH);
}

void dimm_all()
{
	digitalWrite(led[0], LOW);
	digitalWrite(led[1], LOW);
	digitalWrite(led[2], LOW);
}

void shine(int nr)
{
	digitalWrite(led[nr], HIGH);
}