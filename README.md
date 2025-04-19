# Enhancement Two: Algorithms & Data Structures

**Author:** Dheeraj Kollapaneni  
**Date:** March 28, 2025

## Artifact Overview

- **Project:** Android Event Manager App (CS-360)
- **Pre‑Enhancement:** User authentication and CRUD operations via Firebase Firestore

## Enhancement Summary

For **Milestone Three**, I implemented an advanced **search & filtering** feature to demonstrate algorithmic and data structure proficiency:
<img width="150" alt="image" src="https://github.com/user-attachments/assets/a392972c-bc99-41d1-9e39-04c0b58b9166" />
<img width="150" alt="image" src="https://github.com/user-attachments/assets/560d9834-4f95-4294-ab00-ab6fb0f280d8" />
<img width="150" alt="image" src="https://github.com/user-attachments/assets/f4a2c46b-a71d-45bf-9d78-634ee42e37f5" />
<img width="150" alt="image" src="https://github.com/user-attachments/assets/e2713aa6-792c-4ffb-8c07-3fee29eace46" />


- Filter events by **Date** and/or **Location**
- Utilize `HashMap<String, List<Event>>` for **constant‑time** lookups
- Merge results when both filters are active
- Gracefully handle edge cases (no filters, no matches)

## Core Implementation

```java
// Build date‑indexed map
Map<String, List<Event>> dateMap = new HashMap<>();
for (Event e : allEvents) {
    dateMap.computeIfAbsent(e.getDate(), k -> new ArrayList<>()).add(e);
}

// Filter logic example
List<Event> dateMatches = dateMap.getOrDefault(selectedDate, Collections.emptyList());
List<Event> locationMatches = locationMap.getOrDefault(selectedLocation, Collections.emptyList());
List<Event> results;
if (!dateMatches.isEmpty() && !locationMatches.isEmpty()) {
    results = dateMatches.stream()
        .filter(locationMatches::contains)
        .collect(Collectors.toList());
} else {
    results = !dateMatches.isEmpty() ? dateMatches : locationMatches;
}
```

### Data Structures

- `HashMap<String, List<Event>>` for date and location indexes

### Filtering Algorithm

1. Retrieve lists from `dateMap` and/or `locationMap`  
2. If both filters set, compute their intersection  
3. Display `results` or show a **No events matched** message

## Testing & Validation

- **Node.js Script:** `generate_test_data.js`  
  - Generates **500 unique** events using Firebase Admin SDK  
  - Validates performance under realistic load and edge conditions
<img width="350" alt="image" src="https://github.com/user-attachments/assets/91e605c1-810a-478c-918d-fe5769295b6f" />



## Learning Outcomes

- Applied **algorithmic principles** for efficient event filtering  
- Integrated Android UI (`DatePickerDialog`) with backend maps  
- Ensured **UI responsiveness** by performing filtering off the main thread

## Challenges & Solutions

- **Date Format Inconsistency:** Enforced `DatePickerDialog` to standardize input  
- **Single‑Field Searches:** Independently handled date‑only and location‑only queries



