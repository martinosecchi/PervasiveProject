/**
  AirQual_TGS26xx.h - Library for interfacing with the Figaro TGS26xx series of sensors.
  Created by Ivan Naumovski, inau @ github.com.
**/
#ifndef AirQual_TGS26xx_h
#define AirQual_TGS26xx_h

#include "Arduino.h"

#define         SEN_02                       (1)
#define         SEN_00                       (0)
#define         GAS_H2                       (0)
#define         GAS_C2H5OH                   (1) //Alcohol, Ethanol
#define         GAS_C4H10                    (2)
#define         GAS_C7H8                     (3) //Toluene
#define         GAS_H2S                      (4) //Hydrogen Sulfide
#define         GAS_NH3                      (5) //Ammonia

#define         CALIBRATION_SAMPLE_TIMES     (50)    //define how many samples you are going to take in the calibration phase
#define         CALIBRATION_SAMPLE_INTERVAL  (500)   //define the time interal(in milisecond) between each samples in the
                                                     //cablibration phase
#define         READ_SAMPLE_INTERVAL         (50)    //define how many samples you are going to take in normal operation
#define         READ_SAMPLE_TIMES            (5)     //define the time interal(in milisecond) between each samples in 

class TGS26xx
{
  public:
    static TGS26xx TGS2600(int pin) {
    	TGS26xx sensor = TGS26xx(pin, SEN_00);
		sensor.Ro3 = sensor.MQCalibration(pin,10,sensor.RL3,sensor.C2H5OH_terCurve);
	    return  sensor;
	 };
    static TGS26xx TGS2602(int pin) { 
    	TGS26xx sensor = TGS26xx(pin, SEN_02); 
    	sensor.Ro6 = sensor.MQCalibration(pin,1,sensor.RL6,sensor.C7H8Curve);
    	return sensor; 
    };
	int  GetGasPercentage(int gas_id);
	TGS26xx(){
		_pin = -1;
		_type = -1;
	}
  private:
	TGS26xx(int pin, int type) {
		_pin = pin;
		_type = type;
	};
    int _pin;
	int _type;
	float           C2H5OH_secCurve[2]  = {0.2995093465,  -3.148170562};	//TGS2600
	float           C2H5OH_terCurve[2]  = {2142.297846,   -2.751369226};  //MQ138 (3,200) (1.8,1000) (0.7,10000)
	float           C2H5OH_quarCurve[2] = {0.5409499131,  -2.312489623};  //TGS2602   (0.75,1)  (0.3,10)  (0.17,30) 
	float           C4H10Curve[2]       = {0.3555567714,  -3.337882361}; 	//TGS2600
	float           H2_terCurve[2]      = {0.3417050674,  -2.887154835}; 	//TGS2600
	float           C7H8Curve[2]        = {37.22590719,    2.078062258}; 	//TGS2602   (0.3;1)		(0.8;10) 	(0.4;30)
	float           H2S_Curve[2]        = {0.05566582614, -2.954075758}; 	//TGS2602   (0.8,0.1) 	(0.4,1) 	(0.25,3)
	float           NH3_Curve[2]        = {0.585030495,   -3.448654502};  //TGS2602   (0.8,1) 	(0.5,10) 	(0.3,30) 
	float           Ro                  = 10000;                          //Ro is initialized to 10 kilo ohms
	
	//depend on HW configurations
	float Ro3 = 2.511;    //TGS2600 0.05 this has to be tuned 10K Ohm
	float RL3 = 0.893;    //TGS2600 Sainsmart
	float Ro6 = 2.511;    //TGS2602 0.05 this has to be tuned 10K Ohm
	float RL6 = 0.893;    //TGmq136S2602 Gas Sensor V1.3 auto-ctrl.com 

	int   MQGetPercentage(float rs_ro_ratio, float ro, float *pcurve);
	float MQCalibration(int mq_pin, double ppm, double rl_value, float *pcurve );
	float MQResistanceCalculation(int raw_adc,float rl_value);
	float MQRead(float rl_value);
};

#endif