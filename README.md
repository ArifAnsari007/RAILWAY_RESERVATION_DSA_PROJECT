# 🚆 Railway Reservation System (Java)

<p align="center">
  <b>A Data Structures and Algorithms Based Railway Reservation System</b>
  <br>
  Built using Java, OOP, Graphs, Queue, HashMap, Merge Sort and File Handling.
</p>

---

## 📌 1. Project Overview

The **Railway Reservation System** is a menu-driven Java application designed to simulate basic railway ticket reservation operations.

It allows users to view available trains, book and cancel tickets, search for tickets, manage waiting lists, find the shortest route between railway stations, and sort trains based on fare and departure time.

This project demonstrates the practical implementation of **Object-Oriented Programming (OOP), Data Structures, Algorithms, and File Handling** in Java.

## 🎯 2. Project Objectives

- To implement real-world reservation operations using Java.
- To understand and apply different data structures.
- To implement Graphs and Dijkstra's Algorithm for shortest-route calculation.
- To manage waiting lists using FIFO Queue.
- To implement Merge Sort for train sorting.
- To maintain records using File Handling.
- To understand how different DSA concepts work together in a single application.

## ✨ 3. Key Features

| Feature | Description |
|---|---|
| View Trains | Displays train details, fares, departure times and available seats. |
| Station & Route Management | Displays railway stations and their connections. |
| Shortest Route | Finds the shortest distance using Dijkstra's Algorithm. |
| Ticket Booking | Books confirmed tickets if seats are available. |
| Waiting List | Adds passengers to a FIFO waiting queue when the train is full. |
| Ticket Search | Searches confirmed and waiting-list tickets. |
| Ticket Cancellation | Cancels tickets and promotes the next waiting passenger. |
| Sort by Fare | Sorts trains based on ticket fare using Merge Sort. |
| Sort by Departure | Sorts trains according to departure time using Merge Sort. |
| File Handling | Saves and loads railway data automatically. |
| Built-in Testing | Executes five predefined test cases. |

## 🛠️ 4. Technologies Used

- **Programming Language:** Java
- **IDE:** IntelliJ IDEA / VS Code
- **JDK:** Java 8 or above
- **Data Storage:** Text File (railway.txt)
- **Core Concepts:** OOP, DSA, Java Collections Framework

## 📚 5. Data Structures and Algorithms Used

| Concept | Implementation |
|---|---|
| Classes & Objects | Train, Ticket, Edge, Graph, ReservationSystem |
| Graph | Represents railway stations and routes. |
| Dijkstra's Algorithm | Calculates the shortest route by distance. |
| Queue | Maintains the waiting list in FIFO order. |
| HashMap | Stores confirmed tickets for efficient searching. |
| LinkedHashMap | Maintains train and station records. |
| PriorityQueue | Selects the next station with minimum distance in Dijkstra's Algorithm. |
| ArrayList | Stores graph adjacency lists. |
| Merge Sort | Sorts trains by fare and departure time. |
| File Handling | Saves and retrieves application data. |
| Exception Handling | Handles file and input-related exceptions. |

## ⏱️ 6. Time and Space Complexity

The following table describes the main algorithmic operations.

| Operation | Time Complexity | Explanation |
|---|---|---|
| View Trains | O(T) | Displays all trains. |
| Add Station | O(1) average | Uses LinkedHashMap. |
| Add Route | O(V + E) worst case | Removes duplicate edges and adds a route. |
| Shortest Route (Dijkstra) | O((V + E) log V) | Uses adjacency lists and PriorityQueue. |
| Book Ticket | O(K + W) | Checks existing bookings and waiting entries. |
| Search Confirmed Ticket | O(1) average | Uses HashMap. |
| Search Waiting Ticket | O(W + T) | Searches waiting queues across trains. |
| Cancel Waiting Ticket | O(W + T) | Searches and removes the waiting entry. |
| Cancel Confirmed Ticket | O(1) average | HashMap removal, excluding waiting-list promotion. |
| Sort Trains | O(T log T) | Uses Merge Sort. |
| Save/Load Data | O(N) | Processes stored records. |

**Space Complexity:**

| Data Structure / Algorithm | Space Complexity |
|---|---|
| Graph | O(V + E) |
| Train and Ticket Records | O(T + K + W) |
| Merge Sort | O(T) auxiliary space |
| Dijkstra's Algorithm | O(V + E) |
| Waiting Queue | O(W) |

**Notation:**

- V = Number of railway stations (vertices).
- E = Number of railway routes (edges).
- T = Number of trains.
- K = Number of confirmed tickets.
- W = Number of waiting-list entries.
- N = Total number of records in the data file.

*Note: Complexities are theoretical estimates. Actual performance depends on the number of stations, trains, tickets, and waiting-list entries.*

## 🖥️ 7. Sample Output

### Main Menu

```text
===== RAILWAY RESERVATION SYSTEM =====

1. View trains
2. View stations & routes (graph)
3. Search route
4. Book ticket
5. Search ticket
6. Cancel ticket
7. Sort trains by fare
8. Sort trains by departure time
9. View waiting list of a train
10. Run test cases
0. Save & Exit

Choice:
```

### View Available Trains

```text
Choice: 1

ID     Name               From         To           Fare   Dep    Seats(free/total)   Waiting
T101   Rajdhani Exp       Delhi        Mumbai       1800   16:30  2/2                 0
T102   Shatabdi Exp       Delhi        Agra         700    06:15  3/3                 0
T103   Duronto Exp        Delhi        Bhopal       1200   22:00  3/3                 0
```

### Book a Ticket

```text
Choice: 4

Train ID: T101
Passenger name: Aman

Ticket booked: TKT101 | Aman | Train T101
```

### Waiting List

```text
Choice: 4

Train ID: T101
Passenger name: Riya

Train full. Added to waiting queue: WL1 | position 1
```

### Search Ticket

```text
Choice: 5

Ticket ID: TKT101

Ticket found: TKT101 | Aman | Train T101 | CONFIRMED
```

### Shortest Route Search

```text
Choice: 3

From: Delhi
To: Mumbai

Shortest route: Delhi -> Agra -> Bhopal -> Mumbai (1583 km)
No direct train between these stations.
```

### Ticket Cancellation

```text
Choice: 6

Ticket ID: TKT101

Ticket cancelled. Seat allocated to Riya from waiting queue (WL1).
New ticket: TKT103
```

*Note: Sample outputs illustrate the program's behavior with fresh default data. Ticket IDs may differ depending on previous bookings.*

## 📁 8. Project Structure

```text
Railway-Reservation-System/
│
├── RailwayReservationSystem.java
│
├── railway.txt
│
└── README.md
```

### File Description

- `RailwayReservationSystem.java` – Contains the complete Java application.
- `railway.txt` – Stores train, station, route, ticket, and waiting-list records.
- `README.md` – Contains project documentation.

**Note:** The railway.txt file is automatically created when the application saves data for the first time.

## 🚀 9. How to Run the Project

### Step 1: Clone the Repository

```bash
git clone YOUR_REPOSITORY_URL
```

### Step 2: Open Project Directory

```bash
cd Railway-Reservation-System
```

### Step 3: Compile Java Code

```bash
javac RailwayReservationSystem.java
```

### Step 4: Execute the Application

```bash
java RailwayReservationSystem
```

### Requirements

- Java JDK 8 or above.
- Command Prompt / Terminal.
- IntelliJ IDEA or VS Code (optional).

## 🧪 10. Testing

The application includes five built-in test cases.

| Test | Scenario | Expected Result |
|---|---|---|
| 1 | Normal Booking | Ticket successfully booked. |
| 2 | Full Train | Passenger added to waiting list. |
| 3 | Invalid Ticket | Ticket not found message. |
| 4 | Ticket Cancellation | Waiting passenger gets the released seat. |
| 5 | Route Search | Shortest route is displayed. |

Select option `10` from the main menu to execute all five test cases.

## ⚠️ 11. Limitations

- The application currently uses a console-based interface.
- Data is stored in a text file instead of a database.
- The system does not support multiple users accessing the application simultaneously.
- Train schedules and routes are based on predefined data.
- It does not include real-time train tracking.
- Online payment and refund facilities are not implemented.
- Passenger authentication and authorization are not available.
- Route searching calculates the shortest distance, not the fastest journey time.
- The current train model supports direct source-to-destination train information rather than complete multi-stop journey scheduling.

## 🔮 12. Future Scope

The project can be enhanced with the following features:

1. **GUI Development:** Build a user-friendly interface using Java Swing or JavaFX.
2. **Database Integration:** Replace text-file storage with MySQL or PostgreSQL.
3. **User Authentication:** Add secure passenger and administrator login.
4. **Real-Time Train Tracking:** Integrate railway tracking APIs.
5. **Seat Selection:** Allow passengers to select specific seats.
6. **Online Payment:** Add a secure payment gateway.
7. **Multi-Stop Routes:** Support intermediate stations and complete train journeys.
8. **PNR System:** Introduce PNR generation and detailed ticket status.
9. **Admin Dashboard:** Allow administrators to add, update, and remove trains and routes.
10. **Web Application:** Develop a web-based reservation platform using Java Spring Boot.

## 🎓 13. Learning Outcomes

Through this project, I gained practical experience in:

- Applying Object-Oriented Programming principles.
- Implementing Graphs and shortest-path algorithms.
- Understanding FIFO Queue operations.
- Using HashMap for efficient data retrieval.
- Implementing Merge Sort using Java Generics.
- Performing file-based data storage and retrieval.
- Integrating multiple Data Structures and Algorithms into one application.

## 👨‍💻 14. Author

**Arif Ansari**

B.Tech – Computer Science and Engineering (AIML)

Rungta International Skills University, Bhilai

**Skills & Technologies:** Java | OOP | DSA | Graphs | Algorithms | File Handling




