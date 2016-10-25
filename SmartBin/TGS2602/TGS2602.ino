#include <AirQual_TGS26xx.h>

int pin = 0;
TGS26xx sensor;
void setup() {
  Serial.begin(9600);
  Serial.println("Calibration started");
  sensor = TGS26xx::TGS2602(pin);
  Serial.println("Calibration successfull");
}

void loop() {
//val = analogRead(gasSensor); // read the value from the pot
//Serial.println( val );
Serial.print( analogRead(pin));
Serial.print(" : ");
Serial.print( sensor.GetGasPercentage(GAS_H2S));
Serial.print( " ");
Serial.print( sensor.GetGasPercentage(GAS_NH3));
Serial.print( " ");
Serial.print( sensor.GetGasPercentage(GAS_C2H5OH));
Serial.print( " ");
Serial.print( sensor.GetGasPercentage(GAS_C7H8));
Serial.print( " ");
Serial.println();
delay(100);
}
