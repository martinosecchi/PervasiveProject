/**
  AirQual_TGS26xx.h - Library for interfacing with the Figaro TGS26xx series of sensors.
  Created by Ivan Naumovski, inau @ github.com.
**/
#ifndef AirQual_TGS26xx_h
#define AirQual_TGS26xx_h

#include "Arduino.h"

#define         GAS_H2                       (0)
#define         GAS_C2H5OH                   (1) //Alcohol, Ethanol
#define         GAS_C4H10                    (2)
#define         GAS_C7H8                     (3) //Toluene
#define         GAS_H2S                      (4) //Hydrogen Sulfide
#define         GAS_NH3                      (5) //Ammonia

class TGS26xx
{
  public:
    TGS2600(int pin);
    TGS2602(int pin);
	int  GetGasPercentage(float rs_ro_ratio, float ro, int gas_id);
  private:
	int   MQGetPercentage(float rs_ro_ratio, float ro, float *pcurve);
	float MQCalibration(int mq_pin, double ppm, double rl_value, float *pcurve )
	float MQResistanceCalculation(int raw_adc,float rl_value);
    int   _pin;
	float           C2H5OH_secCurve[2]  = {0.2995093465,  -3.148170562};	//TGS2600
	float           C2H5OH_terCurve[2]  = {2142.297846,   -2.751369226};  //MQ138 (3,200) (1.8,1000) (0.7,10000)
	float           C2H5OH_quarCurve[2] = {0.5409499131,  -2.312489623};  //TGS2602   (0.75,1)  (0.3,10)  (0.17,30) 
	float           C4H10Curve[2]       = {0.3555567714,  -3.337882361}; 	//TGS2600
	float           H2_terCurve[2]      = {0.3417050674,  -2.887154835}; 	//TGS2600
	float           C7H8Curve[2]        = {37.22590719,    2.078062258}; 	//TGS2602   (0.3;1)		(0.8;10) 	(0.4;30)
	float           H2S_Curve[2]        = {0.05566582614, -2.954075758}; 	//TGS2602   (0.8,0.1) 	(0.4,1) 	(0.25,3)
	float           NH3_Curve[2]        = {0.585030495,   -3.448654502};  //TGS2602   (0.8,1) 	(0.5,10) 	(0.3,30) 
	float           Ro                  = 10000;                          //Ro is initialized to 10 kilo ohms

};


#endif