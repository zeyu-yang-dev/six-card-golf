# Six Card Golf

A Kotlin implementation of the classic card game *Six Card Golf*, built using the [BGW framework](https://tudo-aqua.github.io/bgw/).

---
## Project Introduction

This project supports local hotseat gameplay for 2–4 players and 
provides in-game guidance to help players understand available actions at each step.

It is designed with a clear separation between UI, game logic, and domain model, 
emphasizing maintainability and modular structure. 
The UI is organized using custom pane classes that encapsulate groups of UI components, 
enabling structured composition and reuse.

---
## Architecture Overview

This project follows a layered architecture that separates UI, game logic, and domain model:

- **View Layer (`view`)**  
  Responsible for rendering the user interface and handling user input.  
  It triggers player actions and reflects the current game state through visual updates.

- **Service Layer (`service`)**  
  It contains the core game logic and controls the game flow.
  Responsible for updating the game state in the entity layer. 
  The `RootService` acts as a central coordinator, providing access to all services and orchestrating interactions between layers.

- **Entity Layer (`entity`)**  
  Defines the domain model of the game.  
  The `SixCardGolf` class represents a single game instance and acts as the central state holder.
  It aggregates other domain objects such as `Player` and `Card`, which represent the fundamental elements of the game.



---
## Game Rules


---
## How to Run


---
## Screenshots











