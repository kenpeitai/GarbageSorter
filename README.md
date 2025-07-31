# Android-Based Garbage Sorting Guidance App

A lightweight and intelligent Android application that assists users in sorting waste through keyword search and real-time image recognition. Designed to promote environmental awareness and support urban sustainability efforts.

## 📱 Overview

This application is designed to guide users in classifying domestic waste accurately and efficiently. It supports:

- 🗑️ Keyword-based garbage search  
- 📷 Real-time garbage image recognition (ResNet50 + TensorFlow Lite)  
- 🌐 Multilingual UI (Chinese & English)  
- ⭐ Favorite collection and management  
- 🔄 Share function for garbage details  
- 📊 Search history & usage analytics  
- 🎨 Built with Jetpack Compose for modern UI

## 🧠 Motivation

Improper waste classification leads to resource loss and environmental degradation. Public knowledge of sorting rules is often limited or inconsistent. This app leverages mobile computing and AI to make waste classification more accessible, accurate, and user-friendly.

## 🛠 Tech Stack

| Component             | Technology Used             |
|----------------------|-----------------------------|
| Language             | Kotlin                      |
| UI Framework         | Jetpack Compose             |
| Database             | Room (SQLite)               |
| AI/ML Model          | ResNet50 (TensorFlow Lite)  |
| Image Preprocessing  | Kotlin + Android API        |
| Multilingual Support | Android Resource System     |

## 🧩 Architecture

```
User Interface Layer
     ↓
Application Logic Layer
     ↓
Data Access Layer (Room + SQLite)
     ↓
External API Layer (TensorFlow Lite)
```

## 📦 Features

- **Garbage Search**: Search by name, description, or category.
- **Garbage Details**: View disposal methods and examples.
- **Image Recognition**: Upload a photo to auto-classify waste.
- **Collection System**: Save frequently used items.
- **Multilingual**: Instant switch between Chinese and English.
- **Offline-first**: Works without network once installed.
- **Modular**: Easy to maintain, test, and extend.

## 🚀 Getting Started

### Prerequisites

- Android Studio (Giraffe or later)
- Kotlin 1.8+
- Gradle 8.x
- Android device (API Level 26+, ARM64 recommended)

### Build & Run

1. Clone the repository:
   ```bash
   git clone https://github.com/your_username/garbage-sorting-app.git
   ```

2. Open in Android Studio and sync Gradle.

3. Connect your Android device or use an emulator.

4. Click ▶ to run the app.

## 🧪 Testing

- Unit tests: DAO, image classifier
- Functional tests: search, favorites, language switch
- Tested on Snapdragon 8+ Gen1 / Android 13
- Average classification accuracy: **~80%** (ResNet50 on 158 categories)

## 📁 Dataset & Model

- Dataset: 158-class labeled garbage dataset (~54,000 images)
- Trained using TensorFlow + Keras
- Converted to `.tflite` for mobile inference
- Optimized using GlobalAveragePooling and softmax classification

## 📈 Future Work

- Improve recognition in poor lighting
- Add iOS & web versions
- Enable user personalization / learning
- Expand dataset to support region-specific classifications

## 📜 License

This project is part of an undergraduate thesis at Chongqing University of Posts and Telecommunications.  
For academic use only. Contact the author for collaboration or reuse.

## 🙏 Acknowledgements

- Supervisor: 陈吕洋 工程师  
- Project conducted under the Software Engineering Department, Class of 2019  
- Thanks to all testers and peers who provided feedback
