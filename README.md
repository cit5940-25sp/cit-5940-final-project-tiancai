[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/nK589Lr0)
[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=18841709&assignment_repo_type=AssignmentRepo)
# Othello Demo

An interactive Othello (Reversi) game implemented in Java with GUI and multiple AI strategies.

---

## 🎯 Project Overview

`Othello Demo` allows two players to compete on an 8×8 board. Players can be human or computer-controlled using different AI strategies:

* **Minimax** with α–β pruning
* **Monte Carlo Tree Search (MCTS)**
* **Neural Network Strategy** (via ONNX model)

Additionally, the game supports:

* **Save/Load** game state using the Memento pattern and file I/O
* **Strategy** pattern to swap AI behaviors
* **GUI** built with JavaFX

---

## 📂 Project Structure

```
cit-5940-final-project-tiancai/
├── src/
│   ├── main/
│   │   ├── java/othello/
│   │   │   ├── ai/               # Minimax, MCTS, NeuralNetworkStrategy, etc.
│   │   │   ├── gamelogic/        # BoardSpace, Player, OthelloGame, GameMemento, etc.
│   │   │   ├── gui/              # GameController, GUISpace
│   │   │   └── io/               # GameIO
│   │   └── resources/othello/
│   │       └── game-view.fxml    # JavaFX layout
│   └── test/java/
│       ├── AITest/               # MCTS, Minimax, NN strategy tests
│       ├── gamelogicTest/        # BoardSpace, OthelloGame, Player, GameMemento tests
│       └── IOTest/               # GameIO save/load tests
├── README.md                     # ← this file
├── pom.xml (or build.gradle)     # build definition
└── .gitignore
```

---


## 📥 Dependencies

* Java 17 or later
* JavaFX 17
* ONNX Runtime Java API (for neural network strategy)
* JUnit 5 (for testing)

If using Maven, dependencies are declared in `pom.xml`. For Gradle, see `build.gradle`.

---

## 🚀 Getting Started

1. **Clone the repository**:

   ```bash
   git clone https://github.com/your-org/othello-demo.git
   cd othello-demo
   ```

2. **Build the project**:

   * **Maven**:

     ```bash
     mvn clean package
     ```
   * **Gradle**:

     ```bash
     gradle build
     ```

3. **Run the game**:

   ```bash
   mvn javafx:run -Dexec.args="human mcts"
   # or
   java --module-path /path/to/javafx-sdk/lib \
     --add-modules javafx.controls,javafx.fxml \
     -jar target/othello-demo.jar human minimax
   ```

   Replace `human` and `minimax` with any combination of `human`, `minimax`, `mcts`, or `cnn`.

---

## 🔍 Features

* **Human vs Human** or **Human vs AI** matches
* **AI vs AI** exhibition mode
* **Minimax** AI with α–β pruning to search to fixed depth
* **MCTS** AI balancing exploration & exploitation
* **Neural Network** AI (CNN) via ONNX model inference
* **Save/Load** game state (`.otsav` files)
* **Strategy** pattern for pluggable AI
* **Memento** pattern for snapshotting game state
* **JavaFX UI** with clickable board and status panel

---

## 🛠️ Design Patterns

* **Strategy**: `AIStrategy` interface implemented by `Minimax`, `MCTS`, `NeuralNetworkStrategy`, and custom strategies.
* **Memento**: `GameMemento` captures board & ownership state; `GameIO` reads/writes snapshots.
* **MVC** (Model-View-Controller):

  * Model: `OthelloGame`, `BoardSpace`, `Player`
  * View: `game-view.fxml`, `GUISpace` components
  * Controller: `GameController`, `App` launcher

---

## ✅ Testing

Unit tests are written with JUnit 5 under `src/test/java`:

* **AI Tests**: ensure each strategy returns valid moves and handles edge cases.
* **Game Logic Tests**: board initialization, move generation, flipping logic, game over conditions.
* **I/O Tests**: saving/loading game snapshots, error on invalid paths.

Run tests with:

```bash
mvn test
# or
gradle test
```

Coverage should exceed 80% for core modules (`ai` and `gamelogic`).

---

## 👥 Team Members

* **Zihan (Hans) Wu (Testing Lead)**: Wrote and validated comprehensive unit tests covering `ai` and `gamelogic` modules.
* **Junxiang Li (Design Lead)**: Defined class diagrams, applied Strategy and Memento patterns, and ensured clean MVC separation.
* **Tinghao Ruan (Team Lead)**: Coordinated GitHub tasks, led integration of JavaFX UI, and managed project documentation.


---

✨ Enjoy playing Othello with advanced AI! 
