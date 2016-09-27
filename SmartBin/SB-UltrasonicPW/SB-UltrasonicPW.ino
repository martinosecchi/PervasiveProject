
const int pwPin = 3;
long pulse, inches, cm;

void setup(){
  Serial.begin(9600);
}



void loop(){

  pinMode(pwPin, INPUT);
  pulse = pulseIn(pwPin, HIGH);
  inches = pulse / 147;
  cm = inches * 2.54;

  Serial.print(cm);
  Serial.print("cm");
  Serial.println();

  delay(500);

}
