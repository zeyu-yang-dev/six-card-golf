# Six Card Golf

## Introduction

This project is a Kotlin implementation of the classic card game *Six Card Golf* (see [Game Rules](#game-rules)).

The UI is built using the [BoardGameWork (BGW)](https://tudo-aqua.github.io/bgw/), 
a lightweight framework for developing 2D board game applications.

This game supports local hotseat gameplay for 2–4 players and provides in-game guidance to help players understand available actions at each step.

It is designed with a clear separation between UI, game logic, and domain model, 
emphasizing maintainability and modular structure. 
The UI is structured into modular components, enabling clean composition and reuse.


## Screenshots

Below are some screenshots of the game in action:

### Main Menu

![Main Menu](screenshots/01_main_menu.png)

### Gameplay – Start of Turn

![Start of Turn](screenshots/02_turn_start.png)

### Gameplay – After Drawing a Card

![After Draw](screenshots/03_has_drawn.png)

### Result Screen

![Result](screenshots/04_result_menu.png)



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



### Game Start

- A standard deck of 52 cards is used as the initial draw stack.
- Each player receives **6 cards** from the draw stack, arranged in a **2×3 grid**.
- The top card of the draw stack goes to the **discard stack**.
- At the first round, each player **must reveal two of their cards**.

Below is an example of the initial game setup:

![Initial Setup](screenshots/initial_setup.png)



---
### Score Calculation

The score of each player is calculated based on their revealed cards.  
At the end of the game, all cards are revealed and scored as follows:

| Card Value   | Points     |
|--------------|------------|
| Ace          | 1          |
| 2            | -2         |
| 3–10         | Card value |
| Jack / Queen | 10         |
| King         | 0          |

#### Special Rules
- If two cards **in the same column** have the **same value**, they count as **0 points**.
- If all three cards **in a row** have the **same value**, they are removed and placed on the discard stack.



---
### Gameplay

At the start of a turn, the player must choose **one** of the following actions:

#### 1. Reveal a card
- The player flips one of their face-down cards face up.

#### 2. Draw from the draw stack
- The player draws the top card from the draw stack.
- Then the player chooses to:
  - **Swap** it with one of their cards (face up or face down), or  
  - **Discard** it  
- If the player discards the card, they must reveal one of their face-down cards.

#### 3. Draw from the discard stack
- The player takes the top card of the discard stack.
- The player must **swap** it with one of their cards.



---
### The Last Round

The game enters **the last round** when a player has:
- **revealed all their cards**, or
- **removed all their cards** (see [Special Rules](#special-rules)).

All other players then take one final turn.



---
### Winning

- After the last round, all cards are revealed automatically.
- The player with the **lowest total score** wins.
- Special case: If a player has removed **both rows** before the final reveal, they are declared the winner.



## How to Run

> Requires **Java 17 or higher**.

There are two ways to run the game:

### Option 1: Run via JAR (Recommended)

1. Go to the **Releases** page of this repository.
2. Download the latest `.jar` file.
3. Run the game by double-clicking the JAR file.

> If double-click does not work, run the downloaded JAR from the terminal:  
> `java -jar six-card-golf-<version>.jar`

---

### Option 2: Run from Source Code

1. Clone the repository, or download it as a ZIP.
2. Open the project in your IDE.
3. Run the main class:
```
io.github.zeyuyangdev.sixcardgolf.MainKt
```



















