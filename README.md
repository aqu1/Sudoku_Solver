## Sudoku_Solver
A Sudoku solver implemented in Java. 

Given a Sudoku puzzle with no solutions, one solution, or multiple solutions, the respective solver method in the class solves the puzzle in place using recursive backtracking.

To run the sudoku solver, clone the repo and makes sure that you have JDK 8 installed. 
In the terminal, type the following: 
### javac Sudoku.java
### java Sudoku

When prompted to enter the name of the puzzle file, enter one of the following:
• puzzle1.txt
• puzzle2.txt
• multi_sol.txt
• no_solution.txt

When solving a puzzle file with multiple solutions, the Sudoku solver will return the first solution that is found. 
If no solution is present, the current state of the puzzle will be returned. 
