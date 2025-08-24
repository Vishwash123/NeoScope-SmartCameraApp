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

### Authentication
<table>
  <tr>
    <td><img src="ScreenShots/Authentication.jpg" width="200"></td>
  </tr>
</table>




### Main Interface
<table>
  <tr>
    <td><img src="ScreenShots/MainScreen.jpg" width="200"></td>
    <td><img src="ScreenShots/VoiceInput.jpg" width="200"></td>
  </tr>
</table>




### Chatbot
<table>
  <tr>
    <td><img src="ScreenShots/Chatbot1.jpg" width="200"></td>
    <td><img src="ScreenShots/Chatbot2.jpg" width="200"></td>
    <td><img src="ScreenShots/Chatbot3.jpg" width="200"></td>
  </tr>
  <tr>
    <td><img src="ScreenShots/Chatbot4.jpg" width="200"></td>
    <td><img src="ScreenShots/Chatbot5.jpg" width="200"></td>
    <td><img src="ScreenShots/Chatbot6.jpg" width="200"></td>
  </tr>
</table>




### Object Detection & QR
<table>
  <tr>
    <td><img src="ScreenShots/ObjectDetection.jpg" width="200"></td>
    <td><img src="ScreenShots/QRCodeScanner.jpg" width="200"></td>
  </tr>
</table>




### Text Recognition & Translation
<table>
  <tr>
    <td><img src="ScreenShots/TextRecognition1.jpg" width="200"></td>
    <td><img src="ScreenShots/Text Recognition2.jpg" width="200"></td>
    <td><img src="ScreenShots/TranslationScreen1.jpg" width="200"></td>
  </tr>
  <tr>
    <td><img src="ScreenShots/TranslationScreen2.jpg" width="200"></td>
  </tr>
</table>




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

You can find the backend repository here: [Neoscope Backend](https://github.com/Vishwash123/NeoScopeChatbotBackend.git)


