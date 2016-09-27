int gasSensor = 0; 
int val = 0;

void setup() {
Serial.begin(9600);
}

void loop() {
val = analogRead(gasSensor); // read the value from the pot
Serial.println( val );
delay(100);
}
