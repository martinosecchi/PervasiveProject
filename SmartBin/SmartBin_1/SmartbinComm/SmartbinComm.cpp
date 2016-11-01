#include "SmartbinComm.h"
#include <WiFi.h>
char ssid[] = "yourNetwork";     //  your network SSID (name) 
char pass[] = "12345678";    // your network password
int status = WL_IDLE_STATUS;     // the Wifi radio's status

SmartBinComm Connect() {
  SmartBinComm conn = new SmartBinComm();
  
  status = WiFi.begin(ssid, pass);

  // if you're not connected, stop here:
  if ( status != WL_CONNECTED) return NULL;
  // if you are connected, print out info about the connection:
  else return conn;
}

send() {
}

void loop() {
  // do nothing
}

void SmartBinComm::SendSensorLabels();
void SmartBinComm::SendGasValues(String[] labels, float[] values) {
	
};
void SmartBinComm::SendSensorState(int sensor, int value);