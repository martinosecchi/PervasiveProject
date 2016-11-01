#include <AirQual_TGS26xx.h>

int pin = 0;
TGS26xx sensor;
void setup() {
  Serial.begin(9600);
//  Serial.println("Warming up the sensor.. it will take some time");
//  delay(10*60*100); //10 min initial action (warming up the sensor)
//  Serial.println("Starting calibration...");
  Serial.println("Skipping calibration, using defaults for 10k Ohm");
  sensor = TGS26xx::TGS2600(pin);
//  Serial.println("Calibration successfull.");
  Serial.print("Ro set to ");
  Serial.println(sensor.MQGetRo());
}

void loop() {
//val = analogRead(gasSensor); // read the value from the pot
//Serial.println( val );
Serial.print( analogRead(pin));
Serial.print("   rs/ro: ");
Serial.print(sensor.MQReadRoRS());

Serial.print(" gases: ");
Serial.print( sensor.GetGasPercentage(GAS_H2S));
Serial.print( " ");
Serial.print( sensor.GetGasPercentage(GAS_H2)); // TSG2600
Serial.print( " ");
Serial.print( sensor.GetGasPercentage(GAS_C4H10)); // TSG2600
Serial.print( " ");
Serial.print( sensor.GetGasPercentage(GAS_NH3));
Serial.print( " ");
Serial.print( sensor.GetGasPercentage(GAS_C2H5OH));
Serial.print( " ");
Serial.print( sensor.GetGasPercentage(GAS_C7H8)); // TSG2602
Serial.print( " ");
Serial.println();
delay( 1000); //30 seconds
}
