#ifndef SMARTBINCOMM_H
#DEFINE SMARTBINCOMM_H
#include "arduino.h"

#define PROX 0
#define GAS_00 1
#define GAS_02 2

#define ERROR -1
#define OK 0

class SmartBinComm {
	private:
		void Send(char[] message);
		SmartBinComm();
		
	public:
		SmartBinComm Connect();
		void SendSensorLabels();
		void SendGasValues(char[][] labels, float[] values);
		void SendSensorState(int sensor, int value);
}

#endif