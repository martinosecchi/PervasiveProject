
//ultrasonic stuff
const int anPin = 0;
long anVolt, inches, cm;
int sum = 0; 
int avg_range = 60; //values to average



void setup(){

  Serial.begin(9600);

}



void loop() {

//  ultrasonic averaging values
  for (int i = 0; i < avg_range ; i++){
    anVolt = analogRead(anPin) / 2;
    sum += anVolt;
    delay(10);
  }


// ultrasonic
  inches = sum / avg_range; 
  cm = inches * 2.54; //fucking americans

  Serial.print(cm);
  Serial.print("cm");
  Serial.println();

  sum = 0;
  delay(500);

}
