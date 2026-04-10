# Six Card Golf

## Introduction

This project is a Kotlin implementation of the classic card game *Six Card Golf* (see [Game Rules](#game-rules)).

The UI is built using the [BoardGameWork (BGW)](https://tudo-aqua.github.io/bgw/), 
a framework for developing 2D board game applications.

This game supports local hotseat gameplay for 2–4 players and provides in-game guidance to help players understand available actions at each step.

It is designed with a clear separation between UI, game logic, and domain model, 
emphasizing maintainability and modular structure. 
The UI is structured into modular components, enabling clean composition and reuse.


## Screenshots



## Architecture

This project follows a layered architecture that separates UI, game logic, and domain model:

- **View Layer (`view` package)**   
  Responsible for rendering the user interface and handling user input.
  It forwards player actions and reflects the current game state through visual updates.

- **Service Layer (`service` package)**  
  Responsible for updating the game state in the entity layer. 
  It contains the core game logic and controls the game flow.
  The `RootService` acts as a central coordinator, providing access to all services and orchestrating interactions between layers.

- **Entity Layer (`entity` package)**  
  Defines the domain model of the game.  
  The `SixCardGolf` class represents a single game instance and acts as the central state holder.
  It aggregates other domain objects such as `Player` and `Card`, which represent the fundamental elements of the game.




## Game Rules



## How to Run















