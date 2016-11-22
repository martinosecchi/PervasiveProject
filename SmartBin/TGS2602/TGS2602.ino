
#include <WiFi.h>
#include <WiFiClient.h>
#include <WiFiServer.h>
#include <WiFiUdp.h>
#include <SmartbinComm.h>
#include <TGSSensor.h>

int gasPin = 0;
int pwPin = 13;
TGSSensor sensor;
SmartBinComm comm;
long calibration_samples = 300; //one per second
long pulse, cm;
float gas;
//int sendstuff = 0;
//int rate = 10;  // ~10 minutes
void setup() {
  Serial.begin(9600);
  pinMode(pwPin, INPUT); //distance sensor
  if (Serial) Serial.println("Warming up the sensor.. this will take some time");
//  delay(15L*60L*1000L); //15 min initial action (warming up the sensor)
  if (Serial) Serial.println("Starting calibration...");
  sensor = TGSSensor(gasPin, calibration_samples);
  if (Serial){
    Serial.println("Calibration successfull.");
    Serial.print("Ro set to ");
    Serial.println(sensor.ro);
  }

  comm = SmartBinComm();
  comm.Connect();
  comm.RegisterBin(sensor.ro);
}

void loop() {

pulse = pulseIn(pwPin, HIGH);
cm =  (pulse / 147) * 2.54;
gas = sensor.GetRsRo();

  comm.PostGoogleAppEngine(String(gas), String(cm));

//if(sendstuff==rate){
//  sendstuff = 0;
//}
//sendstuff++;

if (Serial){
  Serial.print(cm);
  Serial.print(" cm   analog gas: ");
  Serial.print( analogRead(gasPin));
  Serial.print("   rs/ro: ");
  Serial.print(gas);
  Serial.println();
}

delay(60L * 1000);
}

