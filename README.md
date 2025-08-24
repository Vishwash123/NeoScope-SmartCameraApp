## Neoscope – Smart Camera & AI Assistant 📸🤖

Neoscope is an advanced Android application that combines computer vision, AI-powered assistants, and smart camera functionalities in a single, seamless experience. Built with Kotlin and Jetpack Compose, it integrates multiple AI services for real-time detection, recognition, and interactive tasks.

## 🎯 Features
# Smart Camera & Vision

Capture photos directly in-app with a smart camera interface.

Object Detection using TensorFlow models.

Text Recognition & Translation with Google ML Kit.

QR Code Scanning for quick data retrieval.

# AI Assistance

Conversational AI chatbot powered by LLaMA.cpp.

Custom Hugging Face models for:

Classification

Image generation

Image editing

Background removal

Unified AI services via a Flask backend (tunneled via ngrok) for smooth client-server communication.

# Authentication

Firebase Authentication

Google Sign-In

##🎥 Demo

## 📸 ScreenShots

<!-- Authentication -->
<h3>Authentication</h3>
<img src="ScreenShots/Authentication.jpg" width="200">

<!-- Main Interface -->
<h3>Main Interface</h3>
<img src="ScreenShots/MainScreen.jpg" width="200">
<img src="ScreenShots/VoiceInput.jpg" width="200">

<!-- Chatbot -->
<h3>Chatbot</h3>
<img src="ScreenShots/Chatbot1.jpg" width="200">
<img src="ScreenShots/Chatbot2.jpg" width="200">
<img src="ScreenShots/Chatbot3.jpg" width="200"><br>
<img src="ScreenShots/Chatbot4.jpg" width="200">
<img src="ScreenShots/Chatbot5.jpg" width="200">
<img src="ScreenShots/Chatbot6.jpg" width="200"><br>

<!-- Object Detection & QR -->
<h3>Object Detection & QR</h3>
<img src="ScreenShots/ObjectDetection.jpg" width="200">
<img src="ScreenShots/QRCodeScanner.jpg" width="200"><br>

<!-- Text Recognition & Translation -->
<h3>Text Recognition & Translation</h3>
<img src="ScreenShots/TextRecognition1.jpg" width="200">
<img src="ScreenShots/Text Recognition2.jpg" width="200">
<img src="ScreenShots/TranslationScreen1.jpg" width="200"><br>
<img src="ScreenShots/TranslationScreen2.jpg" width="200">




## Video Demo

[Watch Full Demo Video](https://github.com/Vishwash123/NeoScope-SmartCameraApp/releases/download/v1.0/neoscope-demo.mp4)


## ⚙️ Tech Stack

Frontend: Kotlin, Jetpack Compose, Android SDK

Backend: Flask (Python)

AI Models: LLaMA.cpp, Hugging Face models, TensorFlow

Services: Firebase, Google ML Kit, ngrok


## 🖥️ Backend

The AI services are powered by a separate **Flask backend** which handles:

- AI model inference (LLaMA.cpp, Hugging Face models)
- Photo and text processing
- Serving AI features to the Android client

You can find the backend repository here: [Neoscope Backend](https://github.com/Vishwash123/neoscope-backend)


