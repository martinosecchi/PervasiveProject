#include "SmartbinComm.h"
#include <WiFi.h>
#include <DateTime.h>
char ssid[] = "Linksys01802_5GHz";     //  your network SSID (name) 
char pass[] = "N>='Hr+ld:= 6'wp)EIFXs_(T'rf'?x_\"{\"7g&ZT%$a5\\U(+@fg|)TIj'CCw3?]";    // your network password
int status = WL_IDLE_STATUS;     // the Wifi radio's status

SmartBinComm Connect() {
  SmartBinComm conn = new SmartBinComm();
  
  status = WiFi.begin(ssid, pass);

  // if you're not connected, stop here:
  if ( status != WL_CONNECTED) return NULL;
  // if you are connected, print out info about the connection:
  else return conn;
}

post() {

Serial.println("connecting...");
  String PostData="{ \"test\" : { \"rsro\": \"1.0\", \"distance\": \"0.5 m \"} } ";

  Serial.println(PostData);
  if (client.connect()) {
    Serial.println("connected");
  client.println("POST /data/bin1.json HTTP/1.1");
  client.println("Host:  https://smarterbin.firebaseio.com");
  client.println("User-Agent: Arduino/1.0");
  client.println("Connection: close");
  client.println("Content-Type: application/json;");
  client.print("Content-Length: ");
  client.println(PostData.length());
  client.println();
  client.println(PostData);
  } else {
    Serial.println("connection failed");
  }
}


void loop() {
  // do nothing
	post();
	delay(30000L);
}

void SmartBinComm::SendSensorLabels();
void SmartBinComm::SendGasValues(String[] labels, float[] values) {
	
};
void SmartBinComm::SendSensorState(int sensor, int value);