#include "BluetoothSerial.h"
BluetoothSerial serialBT;
const int rows = 4;
const int cols = 4;

// SAFE GPIO pins for ESP32 (avoid strapping pins: 0, 2, 4, 12, 15)
const int pirPins[rows][cols] = {  
  {13, 14, 16, 17},    // Row 1
  {18, 19, 21, 22},    // Row 2
  {23, 25, 26, 27},    // Row 3
  {32, 33, 34, 35}     // Row 4
};

void setup() {
  serialBT.begin("Yantra-Central");
  Serial.begin(115200);
  Serial.println("Initializing PIR sensors...");

  
  // Wait for PIR sensors to stabilize (30-60 seconds recommended)
  for(int i = 30; i > 0; i--) {
    Serial.printf("Stabilizing PIR sensors... %ds remaining\n", i);
    delay(1000);
  }

  // Configure pins with internal pull-down resistors
  for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
      pinMode(pirPins[i][j], INPUT_PULLDOWN);
    }
  }
  
  Serial.println("\nSystem Ready!");
}

void loop() {
  Serial.println("\nPIR Sensor Matrix:");
  Serial.println("----------------------------------------");

  for (int i = 0; i < rows; i++) {
    Serial.print("Row " + String(i+1) + ":\t");
    for (int j = 0; j < cols; j++) {
      int sensorState = digitalRead(pirPins[i][j]);
      
      // Add error checking for floating pins
      if(sensorState != HIGH && sensorState != LOW) {
        Serial.print("[ERROR] ");
        continue;
      }

      // Print with alignment for better readability
      Serial.print(sensorState ? "1   " : "0    ");
    }
    Serial.println();
  }
  
  Serial.println("----------------------------------------");
  delay(1000); // Update every 1 second
  
}
