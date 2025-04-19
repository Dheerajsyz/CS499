# Enhancement Three: Databases

**Author:** Dheeraj Kollapaneni  
**Date:** April 3, 2025

## Artifact Overview

- **Project:** Android Event Manager App (CS-360)  
- **Original Backend:** SQLite for local event storage  
- **Limitation:** Lacked scalability, multi-user sync, remote access

## Enhancement Summary

In this **Databases** enhancement, I migrated the app’s data layer from **SQLite** to **Google Firebase Firestore**, enabling:

- **Real-time data sync** across devices
- **Cloud-based storage** and remote access
- **Role-based security rules** (users vs. admins)
- **Offline support** with Firestore caching
<img width="372" alt="image" src="https://github.com/user-attachments/assets/0c721d05-f12c-4a4f-9cb8-2a34ef803017" />
<img width="374" alt="image" src="https://github.com/user-attachments/assets/0ca9e981-e233-4277-99fa-a3b187d97fa8" />



## Migration Highlights

1. **Refactored Data Access:**  
   - Replaced SQLite `SQLiteOpenHelper` logic with Firestore `CollectionReference` and snapshot listeners.
   - Converted synchronous queries into asynchronous listeners and callbacks.  

2. **Data Modeling:**  
   - Created `users` and `events` collections to mirror relational concepts in NoSQL.  
   - Stored each event as a document with fields: `eventId`, `userId`, `name`, `location`, `date`, `time`.
<img width="418" alt="image" src="https://github.com/user-attachments/assets/80483a50-3ace-4c5b-a9e0-7b83225ca986" />

3. **Security Rules:**  
   ```js
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /events/{eventId} {
         allow read, write: if request.auth.uid == resource.data.userId || isAdmin(request.auth.uid);
       }
       function isAdmin(uid) {
         return get(/databases/$(database)/documents/users/$(uid)).data.role == 'admin';
       }
     }
   }
   ```

4. **UI Integration:**  
   - Updated `EventListActivity` and `AddEditEventActivity` to use Firestore references.  
   - Implemented real-time listeners to auto-update RecyclerView on data changes.

## Testing & Validation

- Verified real-time updates by running multiple emulator instances simultaneously.  
- Confirmed data persistence and offline caching behavior.  
- Ensured role-based access by testing as normal user vs. admin account.

## Learning Outcomes

- Demonstrated **cloud database integration** in Android.  
- Gained expertise in **asynchronous data handling** with Firestore listeners.  
- Designed **security rules** to enforce user permissions.  
- Improved app **scalability** and **user experience** with real-time sync.

## Challenges & Solutions

- **Asynchronous Refactor:** Needed to rethink data flow without blocking UI.  
  - **Solution:** Used Firestore’s `addSnapshotListener` and updated UI on callback threads.

- **Schema Flexibility:** No fixed schema in Firestore introduced complexity.  
  - **Solution:** Defined a consistent document structure and validated inputs client-side.

- **Security Rules Debugging:** Initial rules blocked admin writes.  
  - **Solution:** Iteratively tested and refined the `isAdmin` helper function in rules.

