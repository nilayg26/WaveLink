# 🚀 WaveLink  
**Yantra Central Hackathon – VIT Vellore 2025**

<div align="center">
  <img src="https://nilayg26.github.io/Animation/waveLinkLogo.png" alt="WaveLink Logo" width="300"/>
</div>

<div align="center">
  <img src="https://nilayg26.github.io/Animation/Certificates/YantraCentralHackathon.jpeg" alt="Yantra Central Hackathon Certificate" width="500"/>
</div>

<div align="center">
  <img width="500" height="350" alt="Yantra Central Hackathon" src="https://github.com/user-attachments/assets/49a583d7-1306-4340-8289-653ef717db79" />
</div>

[🎥 Watch Project Demo](https://drive.google.com/file/d/1JDl03vZDYtb3RqyTyTADU7hbFrmrDZ6h/view?usp=share_link)

> **WaveLink** is a cutting-edge (concept) Android application that enables **gesture-based device control** using an IR-based input grid and **Bluetooth communication**, powered by a custom **Center of Mass algorithm** and **Gemini 2.0 Flash** for real-time recognition.

---

## 🔍 Overview

WaveLink transforms how users interact with technology by introducing a **touchless gesture input system** — particularly beneficial for the visually impaired.

- 📡 **Input System**: IR-based sensor grid detects hand gestures  
- 🔄 **Connectivity**: Bluetooth enables seamless interaction with external devices  
- 🧠 **Smart Algorithm**: Custom Center of Mass logic for interpreting gestures  
- 🌟 **AI-Powered**: Integrated with **Gemini 2.0 Flash** for enhanced recognition  

---

## 🌍 Impact

This system aims to:

-  Blind Empower millions with **touch-free accessibility**  
- 🔐 Enhance **privacy and security**  
- ⚙️ Enable device control in **challenging environments** (e.g., gloves, darkness, disabilities)  

---

## 🛠️ Tech Stack

### Android Application
- **Kotlin** & **Jetpack Compose** – Modern Android development  
- **Firebase** – Real-time database and authentication  
- **Bluetooth Integration** – For hardware communication  
- **Gemini 2.0 Flash** – AI-driven gesture recognition  

### Hardware (Yantra-Central)
- **ESP32** – Main controller  
- **4×4 PIR / IR Sensor Grid** – Spatial motion detection  
- **Bluetooth Serial** – Real-time data streaming to the Android app  

---

## 📐 Hardware Details (ESP32 Firmware)

The firmware (`yantra_hack.ino`) runs on an ESP32 and exposes a 4×4 sensor matrix over Bluetooth under the name **`Yantra-Central`**.

### Safe GPIO Mapping


Row 1:  13, 14, 16, 17

Row 2:  18, 19, 21, 22

Row 3:  23, 25, 26, 27

Row 4:  32, 33, 34, 35


- Uses only non-strapping pins  
- 30-second sensor stabilization on boot  
- Internal pull-downs for noise reduction  
- Prints a live 0/1 occupancy matrix every second over Serial & Bluetooth  

---

## 🚀 Getting Started

### Android App
1. Clone the repository  
2. Open the project in Android Studio  
3. Connect a Firebase project (optional)  
4. Pair with the ESP32 device named **Yantra-Central**  
5. Run the app and start gesturing!

### ESP32 Firmware
1. Open `yantra_hack.ino` in Arduino IDE / PlatformIO  
2. Select your ESP32 board  
3. Upload the sketch  
4. Open Serial Monitor at 115200 baud  
5. Wait for the stabilization countdown  

---

## 📊 Sample Sensor Output
PIR Sensor Matrix:


Row 1:  0    1    0    0

Row 2:  0    1    1    0

Row 3:  0    0    1    0

Row 4:  0    0    0    0


---

## 🙌 Acknowledgements

Built during **Yantra Central Hackathon 2025** at VIT Vellore.

---

## 📄 License

This project is open for educational and non-commercial use. Feel free to fork, improve, and build upon it.
