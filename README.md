# 🐾 Shinchi - A Pet Care App

Shinchi is an Android-based mobile application designed to simplify **pet management and care routines** for pet owners.  
It allows users to create accounts, register pets, track health routines, and set reminders for important events like vet visits and vaccinations.  

---

## ✨ Key Features

- 🔐 **Authentication**
  - Register and log in securely
  - Store user details in database
  - Session-based sign in

- 👤 **User Accounts**
  - Register and log in securely
  - Manage personal details and preferences

- 🎨 **Modern UI/UX**
  - Clean and minimal Android Material Design
  - Responsive layouts for different screen sizes
  - Dark/light mode support (future plan)

---

## 🛠️ Tech Stack

| Category        | Technology |
|-----------------|------------|
| Language        | Java (Android) |
| Framework       | Android SDK |
| Build Tool      | Gradle |
| Database        | SQLite / Firebase (to be finalized) |
| IDE             | Android Studio |
| Version Control | Git & GitHub |

---


## 📂 Project Structure
Shinchi_A_Pet_Care_App/
│── app/ # Main Android app module
│ ├── java/com/example/shinchi/
│ │ ├── activities/ # Activity classes (Login, Register, Home, etc.)
│ │ ├── adapters/ # RecyclerView adapters
│ │ ├── models/ # Data models (User, Pet, Reminder, etc.)
│ │ ├── database/ # Local DB handlers (SQLite/Firebase integration)
│ │ └── utils/ # Helper classes (validation, constants, etc.)
│ │
│ ├── res/ # Android resources
│ │ ├── layout/ # XML UI layouts (activity_main.xml, etc.)
│ │ ├── drawable/ # App icons, vector images, shapes
│ │ ├── mipmap/ # Launcher icons
│ │ ├── values/ # Strings, colors, styles, dimens
│ │ └── menu/ # Menu XML files
│ │
│ └── AndroidManifest.xml # App manifest file
│
│── gradle/ # Gradle wrapper
│── build.gradle.kts # Gradle build configuration (app-level)
│── settings.gradle.kts # Project settings
│── gradlew / gradlew.bat # Gradle wrapper scripts
│── README.md # Project documentation
