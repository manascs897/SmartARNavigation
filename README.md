#📌 Smart AR Navigation System for Visually Impaired

An AI-based mobile application that assists visually impaired users by detecting real-time obstacles using YOLOv8 and providing voice-based navigation alerts.

#🚀 Tech Stack

Backend

Python

Flask

YOLOv8 (Ultralytics)

OpenCV

PyTorch

Android App

Kotlin

Jetpack Compose

Retrofit

Text-to-Speech API

#🏗️ How It Works

Android app captures an image.

Image is sent to Flask backend.

YOLOv8 detects obstacles.

Backend returns obstacle + distance data.

App provides voice alerts to the user.

#⚙️ Setup
Backend
cd backend
pip install -r requirements.txt
python app.py

Android

Open project in Android Studio

Update server IP in ApiService.kt

Sync Gradle and run on physical device

#🎯 Features

Real-time obstacle detection

Distance estimation

Voice-based guidance

REST API communication
