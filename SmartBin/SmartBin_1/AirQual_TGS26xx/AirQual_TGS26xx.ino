 /* 
  ORIGINAL SOURCE:	https://github.com/empierre/arduino
  
  Contribution: epierre
  Based on David Gironi http://davidegironi.blogspot.fr/2014/01/cheap-co2-meter-using-mq135-sensor-with.html
  
  Precaution:
     The gasses detected by these gas sensors can be deadly in high concentrations. Always be careful to perform gas tests in well ventilated areas.
 
  Note:
     THESE GAS SENSOR MODULES ARE NOT DESIGNED FOR OR APPROVED FOR ANY APPLICATION INVOLVING HEALTH OR HUMAN SAFETY. THESE GAS SENSOR MODULES ARE FOR EXPERIMENTAL PURPOSES ONLY.

  License: Attribution-NonCommercial-ShareAlike 3.0 Unported (CC BY-NC-SA 3.0)
  
  Heavily modified by: Ivan Naumovski, inau @ Github
  
  Removed alot of irrelevant code to reduce code size, and improve readability.
  Now its targeted for TGS26xx series Air Quality Sensors - (TGS2600 and 2602)
  Furthermore a sonar sensor has been retrofitted for our specific application.
*/

#include <SPI.h>
#include <Wire.h> 

/************************Hardware Related Macros************************************/
#define         TGS2600_SENSOR				(0)
#define         TGS2602_SENSOR				(1)
#define         SONAR_SENSOR				(2)
#define         RL_VALUE                    (990) //define the load resistance on the board, in ohms
/***********************Software Related Macros************************************/
#define         CALIBRATION_SAMPLE_TIMES     (50)    //define how many samples you are going to take in the calibration phase
#define         CALIBRATION_SAMPLE_INTERVAL  (500)   //define the time interal(in milisecond) between each samples in the
                                                     //cablibration phase
#define         READ_SAMPLE_INTERVAL         (50)    //define how many samples you are going to take in normal operation
#define         READ_SAMPLE_TIMES            (5)     //define the time interal(in milisecond) between each samples in 
/**********************Application Related Macros**********************************/
#define         GAS_H2                       (8)
#define         GAS_C2H5OH                   (9) //Alcohol, Ethanol
#define         GAS_C4H10                   (10)
#define         GAS_C7H8                    (18) //Toluene
#define         GAS_H2S                     (19) //Hydrogen Sulfide
#define         GAS_NH3                     (20) //Ammonia
/*****************************Globals***********************************************/
float           C2H5OH_secCurve[2]  = {0.2995093465,  -3.148170562};	//TGS2600
float           C2H5OH_terCurve[2]  = {2142.297846,   -2.751369226};  //MQ138 (3,200) (1.8,1000) (0.7,10000)
float           C2H5OH_quarCurve[2] = {0.5409499131,  -2.312489623};  //TGS2602   (0.75,1)  (0.3,10)  (0.17,30) 
float           C4H10Curve[2]       = {0.3555567714,  -3.337882361}; 	//TGS2600
float           H2_terCurve[2]      = {0.3417050674,  -2.887154835}; 	//TGS2600
float           C7H8Curve[2]        = {37.22590719,   2.078062258}; 	//TGS2602   (0.3;1)		(0.8;10) 	(0.4;30)
float           H2S_Curve[2]        = {0.05566582614, -2.954075758}; 	//TGS2602   (0.8,0.1) 	(0.4,1) 	(0.25,3)
float           NH3_Curve[2]        = {0.585030495,   -3.448654502};  //TGS2602   (0.8,1) 	(0.5,10) 	(0.3,30) 
float           Ro                  = 10000;                          //Ro is initialized to 10 kilo ohms

unsigned long SLEEP_TIME = 600; // Sleep time between reads (in seconds)
//VARIABLES
float Ro3 = 2.511;    //TGS2600 0.05 this has to be tuned 10K Ohm
float RL3 = 0.893;    //TGS2600 Sainsmart
float Ro6 = 2.511;    //TGS2602 0.05 this has to be tuned 10K Ohm
float RL6 = 0.893;    //TGmq136S2602 Gas Sensor V1.3 auto-ctrl.com 
int val = 0;          // variable to store the value coming from the sensor

boolean metric = true; 

#define CHILD_ID_TGS2600 3
#define CHILD_ID_TGS2602 11

void setup()  
{ 
//  delay(50*1000); //delay to allow serial to fully print before sleep
  Serial.print("    TGS2600:"); 
  Ro3 = MQCalibration(TGS2600_SENSOR,10,RL3,C2H5OH_terCurve);
  Serial.println(Ro3);

  Serial.print("    TGS2602:"); 
  Ro6 = MQCalibration(TGS2602_SENSOR,1,RL6,C7H8Curve);
  Serial.println(Ro6);

}

void loop()      
{      
  //TGS2600 H2 C2H5OH C4H10   
  //Serial.print("TGS2600:"); 
  Serial.print("H2: "); // H2 Gas
  Serial.print(MQGetGasPercentage(MQRead(TGS2600_SENSOR,RL3),Ro3,GAS_H2,TGS2600_SENSOR) );
  Serial.print( "ppm\t" );
   
  Serial.print("C2H5OH: "); // C2H5OH Gas
  Serial.print(MQGetGasPercentage(MQRead(TGS2600_SENSOR,RL3),Ro3,GAS_C2H5OH,TGS2600_SENSOR) );
  Serial.print( "ppm\t" );

  Serial.print("C4H10: "); // C4H1O Gas
  Serial.print(MQGetGasPercentage(MQRead(TGS2600_SENSOR,RL3),Ro3,GAS_C4H10,TGS2600_SENSOR) );
  Serial.print( "ppm\t" );
      
  //TGS2602 C7H8
  //Serial.print("TGS2602:"); 
  Serial.print("C7H8: "); // C7H8 Gas
  Serial.print(MQGetGasPercentage(MQRead(TGS2602_SENSOR,RL6), Ro6,GAS_C7H8, TGS2602_SENSOR) );
  Serial.print( "ppm\n" );
 
  delay(SLEEP_TIME * 1000); //delay to allow serial to fully print before sleep
}

/****************** MQResistanceCalculation ****************************************
Input:   raw_adc - raw value read from adc, which represents the voltage
Output:  the calculated sensor resistance
Remarks: The sensor and the load resistor forms a voltage divider. Given the voltage across the load resistor and its resistance, the resistance of the sensor could be derived.
************************************************************************************/ 
float MQResistanceCalculation(int raw_adc,float rl_value)
{
  return  (long)((long)(1024*1000*(long)rl_value)/raw_adc-(long)rl_value);
;
}
 
/***************************** MQCalibration ****************************************
Input:   mq_pin - analog channel
Output:  Ro of the sensor
Remarks: This function assumes that the sensor is in clean air. It use  
         MQResistanceCalculation to calculates the sensor resistance in clean air.        .
************************************************************************************/ 
float MQCalibration(int mq_pin, double ppm, double rl_value, float *pcurve )
{
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
/*****************************  MQRead *********************************************
Input:   mq_pin - analog channel
Output:  Rs of the sensor
Remarks: This function use MQResistanceCalculation to caculate the sensor resistenc (Rs).
         The Rs changes as the sensor is in the different consentration of the target
         gas. The sample times and the time interval between samples could be configured
         by changing the definition of the macros.
************************************************************************************/ 
float MQRead(int mq_pin,float rl_value)
{
  int i;
  float rs=0;
 
  for (i=0;i<READ_SAMPLE_TIMES;i++) {
    rs += MQResistanceCalculation(analogRead(mq_pin),rl_value);
    delay(READ_SAMPLE_INTERVAL);
  }
 
  rs = rs/READ_SAMPLE_TIMES;
 
  return rs;  
}
 
/*****************************  MQGetGasPercentage **********************************
Input:   rs_ro_ratio - Rs divided by Ro
         gas_id      - target gas type
Output:  ppm of the target gas
Remarks: This function passes different curves to the MQGetPercentage function which 
         calculates the ppm (parts per million) of the target gas.
************************************************************************************/ 
int MQGetGasPercentage(float rs_ro_ratio, float ro, int gas_id, int sensor_id)
{
	if (sensor_id == TGS2600_SENSOR ){
		if ( gas_id == GAS_C2H5OH ) {
		  return MQGetPercentage(rs_ro_ratio,ro,C2H5OH_terCurve);  //TGS2600
		} else if ( gas_id == GAS_C4H10 ) {
		   return MQGetPercentage(rs_ro_ratio,ro,C4H10Curve);   //TGS2600
		} else if ( gas_id == GAS_H2 ) {
		   return MQGetPercentage(rs_ro_ratio,ro,H2_terCurve);  //TGS2600
		}    
   }
   if (sensor_id == TGS2602_SENSOR ){
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
 
/*****************************  MQGetPercentage **********************************
Input:   rs_ro_ratio - Rs divided by Ro
         pcurve      - pointer to the curve of the target gas
Output:  ppm of the target gas
Remarks: By using the slope and a point of the line. The x(logarithmic value of ppm) 
         of the line could be derived if y(rs_ro_ratio) is provided. As it is a 
         logarithmic coordinate, power of 10 is used to convert the result to non-logarithmic 
         value.
************************************************************************************/ 
int  MQGetPercentage(float rs_ro_ratio, float ro, float *pcurve)
{
  return (double)(pcurve[0] * pow(((double)rs_ro_ratio/ro), pcurve[1]));
}
