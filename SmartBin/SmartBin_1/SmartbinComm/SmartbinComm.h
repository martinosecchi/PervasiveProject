#ifndef SMARTBINCOMM_H
#DEFINE SMARTBINCOMM_H
#include "arduino.h"

class SmartBinComm {
	private:
		
	public:
		SmartBinComm();
		void SendValues(String[] labels, float[] values);
		void SendSensorState(int value);
}

#endif