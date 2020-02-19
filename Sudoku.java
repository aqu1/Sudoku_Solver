/**
 * Sudoku.java
 * 
 * Implementation of a class that represents a Sudoku puzzle and solves
 * it using recursive backtracking.
 *
 * Computer Science 112, Boston University
 *
 * Name: Allen Qu    aqu1@bu.edu
 * Partner Name: Kris Huang    kriskch@bu.edu
 */

import java.io.*;  
import java.util.*;

public class Sudoku {    
    // The current contents of the cells of the puzzle. 
    private int[][] grid;
    
    /*
     * Indicates whether the value in a given cell is fixed 
     * (i.e., part of the initial configuration).
     * valIsFixed[r][c] is true if the value in the cell 
     * at row r, column c is fixed, and false otherwise.
     */
    private boolean[][] valIsFixed;
    
    /*
     * This 3-D array allows us to determine if a given subgrid 
     * (i.e., a given 3x3 region of the puzzle) already contains a given value.
     * We use 2 indices to identify a given subgrid:
     *
     *    (0,0)   (0,1)   (0,2)
     *
     *    (1,0)   (1,1)   (1,2)
     * 
     *    (2,0)   (2,1)   (2,2)
     * 
     * For example, subgridHasVal[0][2][5] will be true if the subgrid in the 
     * upper right-hand corner already has a 5 in it, and false otherwise.
     */
    private boolean[][][] subgridHasVal;
    private boolean[][] rowHasVal;
    private boolean[][] colHasVal;
    
    /* 
     * Constructs a new Puzzle object, which initially
     * has all empty cells.
     */
    public Sudoku() {
        this.grid = new int[9][9];
        this.valIsFixed = new boolean[9][9];     
        
        /* 
         * Note that the third dimension of the following array is 10,
         * because we need to be able to use the possible values 
         * (1 through 9) as indices.
         */
        this.subgridHasVal = new boolean[9][9][10];        
        this.rowHasVal = new boolean[9][10];
        this.colHasVal = new boolean[9][10];
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                for (int val = 1; val <= 9; val++) {
                    if (this.grid[i][j] == val) {
                        this.rowHasVal[i][val] = true;
                        this.colHasVal[j][val] = true;
                        this.subgridHasVal[i/3][j/3][val] = true;
                    }
                }
            }
        }
    }

    /*
     * Place the specified value in the cell with the specified coordinates, 
     * and update the state of the puzzle accordingly.
     */
    public void placeVal(int val, int row, int col) {
        this.grid[row][col] = val;
        this.subgridHasVal[row/3][col/3][val] = true;
        this.rowHasVal[row][val] = true;
        this.colHasVal[col][val] = true;
    }

    /*
     * Remove the specified value from the cell with the specified coordinates, 
     * and update the state of the puzzle accordingly.
     */
    public void removeVal(int val, int row, int col) {
        this.grid[row][col] = 0;
        this.subgridHasVal[row/3][col/3][val] = false;
        this.rowHasVal[row][val] = false;
        this.colHasVal[col][val] = false;
        
    }  
    
    /*
     * Read in the initial configuration of the puzzle from the specified 
     * Scanner, and use that config to initialize the state of the puzzle.  
     * The configuration should consist of one line for each row, with the
     * values in the row specified as integers separated by spaces.
     * A value of 0 should be used to indicate an empty cell.
     */
    public void readConfig(Scanner input) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = input.nextInt();
                this.placeVal(val, r, c);
                if (val != 0) {
                    this.valIsFixed[r][c] = true;
                }
            }
            input.nextLine();
        }
    }
    
    /*
     * Displays the current state of the puzzle.
     */        
    public void printGrid() {
        for (int r = 0; r < 9; r++) {
            this.printRowSeparator();
            for (int c = 0; c < 9; c++) {
                System.out.print("|");
                if (this.grid[r][c] == 0)
                    System.out.print("   ");
                else
                    System.out.print(" " + this.grid[r][c] + " ");
            }
            System.out.println("|");
        }
        this.printRowSeparator();
    }
    
    /* 
     * A private helper method used by display() 
     * to print a line separating two rows of the puzzle.
     */
    private static void printRowSeparator() {
        for (int i = 0; i < 9; i++)
            System.out.print("----");
        System.out.println("-");
    }
    
    /*
     * A private method that returns true if it is safe to place
     * a value at the specified square, and false otherwise;
     */ 
    private boolean isSafe(int val, int row, int col) {
        return (!this.rowHasVal[row][val] &&
                !this.colHasVal[col][val] &&
                !this.subgridHasVal[row/3][col/3][val] &&
                !this.valIsFixed[row][col]);
    }
    
    /*
     * This is the key recursive-backtracking method.
     * Returns true if a solution has already been found, and false otherwise.
     */
    private boolean solveRB(int count) { 
        if (count == 81) { //first base case
            return true;
        }
        
        // Try each column in the current row.
        int row = count/9; // get the current row
        int col = count%9; // get the current column
        
        if (valIsFixed[row][col]) {
            return this.solveRB(count+1);
        }
        
        for (int val = 1; val <= 9; val ++) {
            if (this.isSafe(val, row, col)) {
                this.placeVal(val, row, col);
                // Move onto next row by making recursive call
                if(this.solveRB(count+1)){
                    return true;
                }
                this.removeVal(val, row, col);
            }
        }
        return false; // tried all columns in this row, so backtrack!
    }
    
    /*
     * Public wrapper method for solveRB().
     * Makes the initial call to solveRB, and returns whatever it returns.
     */
    public boolean solve() { 
        boolean foundSol = this.solveRB(0);
        return foundSol;
    }
    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Sudoku puzzle = new Sudoku();
        
        System.out.print("Enter the name of the puzzle file: ");
        String filename = scan.nextLine();
        
        try {
            Scanner input = new Scanner(new File(filename));
            puzzle.readConfig(input);
        } catch (IOException e) {
            System.out.println("error accessing file " + filename);
            System.out.println(e);
            System.exit(1);
        }
        
        System.out.println();
        System.out.println("Here is the initial puzzle: ");
        puzzle.printGrid();
        System.out.println();
        
        if (puzzle.solve()) {
            System.out.println("Here is the solution: ");
        } else {
            System.out.println("No solution could be found.");
            System.out.println("Here is the current state of the puzzle:");
        }
        puzzle.printGrid();  
    }    
}