#include <TGSSensor.h>

int pin = 0;
TGSSensor sensor;
void setup() {
  delay(3000);
  Serial.begin(9600);
  Serial.println("Warming up the sensor.. it will take some time");
  delay(10L*60L*1000L); //10 min initial action (warming up the sensor)
  Serial.println("Starting calibration...");
  sensor = TGSSensor(pin);
  Serial.println("Calibration successfull.");
  Serial.print("Ro set to ");
  Serial.print(sensor.ro);
}

void loop() {

Serial.print( analogRead(pin));
Serial.print("   rs/ro: ");
Serial.print(sensor.GetRsRo());
Serial.println();
delay(2* 1000);
}
