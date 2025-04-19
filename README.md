# EventManager Android App (Base Version – SQLite)

A simple Android-based event management application using a local SQLite database.  
Provides user sign‑up/login and basic event creation, viewing, and editing — all stored on the device.

---

## Table of Contents

- [Features](#features)  
- [Prerequisites](#prerequisites)  
- [Installation](#installation)  
- [Database (SQLite) Setup](#database-sqlite-setup)  
- [Build & Run](#build--run)  
- [Usage](#usage)  
- [Project Structure](#project-structure)  

---

## Features

- **User Authentication**  
  - Sign up with email & password  
  - Log in to your account  
- **Local Data Storage** (SQLite)  
  - `users` table: stores `id`, `email`, `password` (hashed or plaintext)  
  - `events` table: stores `id`, `user_id`, `name`, `location`, `date`, `time`  
- **Event Management**  
  - View a list of your events  
  - Add new events (name, location, date & time)  
  - Edit existing events  
- **Floating Action Button (FAB)** to add events  
- **SMS Permission Handling** (request on first launch)

---

## Prerequisites

- Android Studio (Arctic Fox or later)  
- JDK 11 or newer  
- Android SDK (API Level 21+)  
- A physical device or emulator (Android 5.0+)

---

## Installation

1. **Clone the repository**  


2. **Open in Android Studio**  
   - Launch Android Studio → **Open an existing project** → select the cloned folder.

3. **Sync Gradle**  
   - Click **Sync Now** when prompted, or go to **File → Sync Project with Gradle Files**.

---

## Database (SQLite) Setup

The app uses an internal SQLite database via a `SQLiteOpenHelper` subclass - `DatabaseHelper.java`. No manual setup is needed:

```java
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "event_manager.db";
    private static final int DB_VERSION = 1;

    // Users table
    private static final String CREATE_USERS_TABLE =
        "CREATE TABLE users (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "email TEXT UNIQUE NOT NULL," +
        "password TEXT NOT NULL" +
        ");";

    // Events table
    private static final String CREATE_EVENTS_TABLE =
        "CREATE TABLE events (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
        "user_id INTEGER NOT NULL," +
        "name TEXT NOT NULL," +
        "location TEXT," +
        "date TEXT," +
        "time TEXT," +
        "FOREIGN KEY(user_id) REFERENCES users(id)" +
        ");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    // ... onUpgrade(), helper methods for CRUD operations ...
}
```

When the app is first launched, the database and tables are created automatically.

---

## Build & Run

1. Connect an Android device or start an emulator (API 21+).  
2. In Android Studio, click the **Run** ▶️ button.  
3. The app will install and launch on your device.

---

## Usage

1. **Sign Up**  
   - Tap **Sign Up**, enter email & password, then **Register**.  
2. **Log In**  
   - Enter your credentials and tap **Login**.  
3. **View Events**  
   - After login, see a list of all events you’ve created.  
4. **Add Event**  
   - Tap the **+** FAB → fill in **Name**, **Location**, **Date**, **Time** → **Save**.  
5. **Edit Event**  
   - Tap an existing event → modify fields → **Save**.  
6. **SMS Permission**  
   - On first launch, grant SMS permission when prompted (for future notification features).

---

## Project Structure

```plaintext
app/
├─ src/main/
│  ├─ java/com/example/eventmanager/
│  │  ├─ activities/
│  │  │  ├─ LoginActivity.java
│  │  │  ├─ SignUpActivity.java
│  │  │  ├─ MainActivity.java
│  │  │  ├─ EventListActivity.java
│  │  │  └─ AddEditEventActivity.java
│  │  ├─ data/
│  │  │  └─ DatabaseHelper.java
│  │  └─ models/
│  │     ├─ User.java
│  │     └─ Event.java
│  ├─ res/
│  │  ├─ layout/
│  │  │  ├─ activity_login.xml
│  │  │  ├─ activity_signup.xml
│  │  │  ├─ activity_main.xml
│  │  │  ├─ activity_event_list.xml
│  │  │  └─ activity_add_event.xml
│  │  └─ menu/
│  │     └─ navi.xml
└─ build.gradle
```

---


