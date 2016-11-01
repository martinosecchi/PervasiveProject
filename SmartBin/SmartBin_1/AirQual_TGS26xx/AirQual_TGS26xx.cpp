/**
  AirQual_TGS26xx.h - Library for interfacing with the Figaro TGS26xx series of sensors.
  Created by Ivan Naumovski, inau @ github.com.
**/
#include "Arduino.h"
#include <SPI.h>
#include <Wire.h> 
#include "AirQual_TGS26xx.h"

int TGS26xx::GetGasPercentage(int gas_id) {
	float rs_ro_ratio;
	float ro; //= Ro;

	if (_type == SEN_00){
	  ro = Ro3;
		rs_ro_ratio = MQRead(Ro3);
	}else if (_type == SEN_02){
    ro = Ro6;
		rs_ro_ratio = MQRead(Ro6);
	}else 
		return 0;

	if (_type == SEN_00 ){
		if ( gas_id == GAS_C2H5OH ) {
		  return MQGetPercentage(rs_ro_ratio,ro,C2H5OH_terCurve);  //TGS2600
		} else if ( gas_id == GAS_C4H10 ) {
		   return MQGetPercentage(rs_ro_ratio,ro,C4H10Curve);   //TGS2600
		} else if ( gas_id == GAS_H2 ) {
		   return MQGetPercentage(rs_ro_ratio,ro,H2_terCurve);  //TGS2600
		}    
   }
   if (_type == SEN_02 ){
		if ( gas_id == GAS_C7H8 ) {
		  return MQGetPercentage(rs_ro_ratio,ro,C7H8Curve);  //TGS2602
		} else if ( gas_id == GAS_H2S ) {
		  return MQGetPercentage(rs_ro_ratio,ro,H2S_Curve);  //TGS2602
		} else if ( gas_id == GAS_NH3 ) {
		  return MQGetPercentage(rs_ro_ratio,ro,NH3_Curve);  //TGS2602
		} else if ( gas_id == GAS_C2H5OH ) {
		  return MQGetPercentage(rs_ro_ratio,ro,C2H5OH_quarCurve);  //TGS2602
		}
   }	
  return 0;	
}
float TGS26xx::MQReadRoRS(){
  float ro = MQGetRo();
  return MQRead(ro);
}

  float TGS26xx::MQGetRo(){
      if (_type == SEN_00){
        return Ro3;
      }else if (_type == SEN_02){
        return Ro6;
      }else 
        return 0;
  }
int TGS26xx::MQGetPercentage(float rs_ro_ratio, float ro,  float *pcurve) {
	  return (double)(pcurve[0] * pow(((double)rs_ro_ratio/ro), pcurve[1]));
}
float TGS26xx::MQCalibration(int mq_pin, double ppm, double rl_value, float *pcurve) {
	int i;
	float val=0;

	for (i=0;i<CALIBRATION_SAMPLE_TIMES;i++) {            //take multiple samples
	  val += MQResistanceCalculation(analogRead(mq_pin),rl_value);
	  delay(CALIBRATION_SAMPLE_INTERVAL);
	}
	val = val/CALIBRATION_SAMPLE_TIMES;                   //calculate the average value
	//Ro = Rs * sqrt(a/ppm, b) = Rs * exp( ln(a/ppm) / b )

	return  (long)val*exp((log(pcurve[0]/ppm)/pcurve[1]));
}
float TGS26xx::MQResistanceCalculation(int raw_adc,float rl_value) {
  return  (long)((long)(1024*1000*(long)rl_value)/raw_adc-(long)rl_value);	
}

float TGS26xx::MQRead(float rl_value)
{
  int i;
  float rs=0;
 
  for (i=0;i<READ_SAMPLE_TIMES;i++) {
    rs += MQResistanceCalculation(analogRead(_pin),rl_value);
    delay(READ_SAMPLE_INTERVAL);
  }
 
  rs = rs/READ_SAMPLE_TIMES;
 
  return rs;  
}
