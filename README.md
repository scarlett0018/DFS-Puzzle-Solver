# Puzzle Solver with DFS Tree Search Algorithm

## Overview

This project implements a puzzle solver using Depth-First Search (DFS) tree search algorithm and various data structure optimizations. The project includes two versions with enhancements in data structure and algorithm efficiency.

## Features

- **Version 1**: Basic DFS algorithm design and state storage.
- **Version 2**: Advanced data structures and optimized search algorithms for improved performance.

## Data Structures and Algorithms

### Version 1

- **Data Structures**: State storage with a board and a Hashtable for complex tiles.
- **Move Methods**: Separate methods for moving 1x1 tiles and complex tiles.
- **Search Methods**: DFS search with ArrayList for storing searched states. Optimized with greedy pruning using Manhattan distance as a measure of board disorder. Every 50 steps, the Manhattan distance is checked for improvement, with backtracking if no improvement is found. This method can solve 3x3 (and smaller) puzzles.

### Version 2

#### Data Structure

1. **Tree**:
    - Contains board state and direction information.
    - Uses two 2D arrays to store board states, reducing memory usage.
    - Implements `hashCode` method to map board states to integers, allowing efficient duplicate detection with `HashSet`.

2. **Stack**:
    - Used for DFS to store node states in a LIFO manner.

3. **Queue**:
    - Used for BFS to store node states in a FIFO manner.

4. **HashSet**:
    - Stores all encountered states using `hashCode` to detect duplicates efficiently.

#### Algorithm

1. **Search Pool**:
    - Uses a data structure to store search results, preventing stack overflow from large search state trees.

2. **Pruning Strategies**:
    - Prune already encountered events.
    - Prune states based on Manhattan distance (sum of the absolute differences of x and y coordinates from their final positions).

3. **Optimized Search**:
    - Uses `queue` for memory-efficient state storage.
    - Uses `priorityQueue` to select more optimal states based on depth and Manhattan distance.

4. **Parallel Computation and Functional Programming**:
    - Leverages Java 8's functional programming framework.
    - Uses `stream()`, `parallel`, `map`, `filter`, and `collect` methods for efficient data operations.

## Running the Program

1. **GameFrame**:
    - Contains GUI code to create game objects and perform DFS to find solutions.
2. **Data Folder**:
    - Stores data files. Update paths in the main file's initial lines as needed.
3. **Gendata**:
    - Generates data by randomly shuffling a properly ordered board.

### Steps to Run

1. **Setup Data**:
    - Place required data files in the `Data` folder and update paths in the main file.
2. **Run Program**:
    - Set `search` to `True` to display the GUI and print steps in the console.
    - Click the "Go" button in the GUI to see tile movements. On completion, a dialog box saying "over" will appear.
    - If `search` is `False`, the console will display only "False" and the GUI components won't appear (though a window will open).

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/puzzle-solver.git
    ```
2. Navigate to the project directory:
    ```bash
    cd puzzle-solver
    ```
3. Compile and run the project:
    ```bash
    javac Main.java
    java Main
    ```

## License

This project is licensed under the MIT License. See the LICENSE file for details.
