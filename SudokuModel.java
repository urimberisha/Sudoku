import java.util.*;
import java.io.*;

/* SudokuModel holds 3 Matrices, one for the actual values, one for the small numbers,
   and one boolean matrix to hold true/false values about which values can be changed. 
   
   The idea of  this Model is to select a random Matrix from the 20 provided ones in files for each 
   difficulty. This way, odds for showing the same puzzle twice are 1/20 for each difficulty.    */

public class SudokuModel {

   private int mat[][], smallMat[][];
   private boolean matEditable[][];
   private File values;              // we'll be getting values from a file
   private int rand;                 // holds a random number
   private String difficulty;        // holds the difficulty 
   ArrayList <String> puzzleStrings; // stores all the puzzles provided by the text file

   public SudokuModel(String diff){
      difficulty = diff;
      mat = new int[9][9];
      matEditable = new boolean[9][9];
      smallMat = new int[9][9];
      puzzleStrings = new ArrayList<>();
      
      // Get one of the Files based on the difficulty provided by the parameter
      try { 
         values = new File("values//" + difficulty + ".txt");         
      } catch (Exception e) {}

      fileToString(values, puzzleStrings);         // Turns each line of the file to a String and stores it in puzzleStrings
      rand = randomNumber(puzzleStrings.size()-1); // Gets a random number based on the size of the ArrayList
      textToMatrix(mat, puzzleStrings.get(rand));  // Converts a String to an int 2D Array
      matEditable = intToBoolean(mat);             // Creates the boolean matrix based on the main matrix
   
   }
   
   // Method getMatrix() returns the main matrix.
   public int[][] getMatrix() {
   
      return mat;
   
   }
  
   // Method getSmallMatrix() returns the small matrix.
   public int[][] getSmallMatrix() {
   
      return smallMat;
      
   }  
   
   // Method getMatrixBool() returns the boolean matrix.
   public boolean[][] getMatrixBool() {
   
      return matEditable;
      
   }
   

   // Function getSolvedMatrix() returns the solved version of the selected puzzle
   // which is stored in a different text file.
   public int[][] getSolvedMatrix() {
   
      int solvedMat[][] = new int[9][9];

      File solvedValues = new File("values//" + difficulty + "Solved.txt");
      //ArrayList <String> puzzleStringsSolved = new ArrayList<>();
      puzzleStrings = new ArrayList<>();
      fileToString(solvedValues, puzzleStrings);     
      textToMatrix(solvedMat, puzzleStrings.get(rand));         
      return solvedMat;
   
   }
   
   // Replaces the main matrix with the solved one 
   public void solveMatrix() {
   
      mat = getSolvedMatrix();
   
   }
   
   // Method randomNumber(int limit) returns a random int between 0 and limit.
   public int randomNumber(int limit) {
   
      return (int)(Math.random()*limit+1);    
   
   }
   
   // Method fileToString(...) gets every row from the provided File, removes all
   // the spaces, and stores it into the provided ArrayList <String>
   public void fileToString(File textFile, ArrayList <String> arrayList) {
      int lineNumber = 1;
      try { 
         Scanner s = new Scanner(textFile);
         while(s.hasNextLine()){
            arrayList.add((s.nextLine()).replaceAll("\\s+",""));
            lineNumber++;
         }
      } catch (Exception e) {}
   }   
   
   // Method textToMatrix(int[][] m, String s) turns String S into an actual int 2D Array
   public void textToMatrix(int[][] m, String s) {
      for (int i = 0; i<m.length; i++) {
         for (int j = 0; j<m[0].length; j++) {
           m[i][j] = Integer.parseInt("" + s.charAt(i*9+j));
         }
      }      
   }

   // Returns booleanMat which is a boolean matrix that has true for every value that
   // can be changed in the main matrix and false for the ones that can't be changed.
   public boolean[][] intToBoolean(int [][] m) {
      boolean booleanMat[][] = new boolean[9][9];   
      for (int i = 0; i<9; i++) {
         for (int j = 0; j<9; j++) {
            if(m[i][j] == 0) {
               booleanMat[i][j] = true;
            }
            else {
               booleanMat[i][j] = false;
            }
         }
      }   
      return booleanMat;
   }
   
   // The methods below are all for calculations with the int arrays
   // All the methods are only written to make isFinished() work. 
   
   // Method isFinished(int[][] array) checks if the array provided via argument
   // is a solved Sudoku puzzle.
   public boolean isFinished(int[][] array) {
  
      return (!containsZero(array) && !checkAllVertically(array) && !checkAllBoxes(array) && !checkAllHorizontally(array));
   
   } 
   
   // ContainsZero(int[][] arr) returns true if the array contains any 0 and false if it doesn't.
   public boolean containsZero(int[][] arr) {
      boolean contains = false;
      for (int i = 0; i<arr.length; i++) {
         contains |= Arrays.stream(arr[i]).anyMatch(x -> (x == 0)); 
      }     
      return contains;
   }
   
   // Checks if a row contains two same values
   public boolean checkHorizontally (int[][] array, int row) {
      for (int i = 0; i<array[row].length; i++) {
         for (int j = i+1; j<array[row].length; j++) {
            if ( array[row][i] == array[row][j] ) {
               return true;
            }
         }         
      }
   return false;
   }

   // Checks every row of an array for same values
   public boolean checkAllHorizontally (int[][] array) {
      boolean checkHor = false;
      for (int i = 0; i<array.length; i++) {
         checkHor |= checkHorizontally(array, i);
      }
      return checkHor;
   }
   
   // Checks if a column contains two same values
   public boolean checkVertically (int[][] array, int col) {  
      for (int i = 0; i<array.length; i++) {
         for (int j = i+1; j<array.length; j++) {
            if ( array[i][col] == array[j][col] ) {
            
               return true; 
            
            }
         }         
      }
      return false;
   } 
 
   // Checks if any of the columns has two same values
   public boolean checkAllVertically (int[][] array) {
     
      boolean checkVer = false;
      for (int i = 0; i<array[0].length; i++) {
      
         checkVer |= checkVertically(array, i);
       
       }   
      return checkVer;
   }
   
   // Basically turns the 9x9 matrix into nine 3x3 matrixes.
   // We need this for one of the rules of Sudoku.
   public int[][] subArray (int[][] array, int row, int col) {
   
      int[][] subarray = new int[3][3];
      for (int i = 0; i<3; i++) {
         for (int j = 0; j<3; j++) {
         
            subarray[i][j] = array[row*3+i][col*3+j];

         }
      }
      return subarray;
   }
      
   // Checks if the matrix has two same values.
   public boolean arrayHasDuplicates(int[][] array) {
      int c=0;
      boolean b = false;
      for (int i = 0; i < array.length; i++) {
         for (int j = 0; j < array[0].length; j++) {
            for (int k = 0; k < array.length; k++) {
               for (int l = 0; l < array[0].length; l++) {
                  if (array[i][j] == array[k][l]) {
                     c++;
                  }
                }
             }
             if (c > 1) {
               b = true;
             }
               c = 0;
          }
      }
      return b;
   }       
        
   // Checks if any of the "boxes" has two same values
   public boolean checkAllBoxes (int[][] array) {
   
      boolean checkBoxes = false;
   
      for (int i = 0; i<3; i++) {
         for (int j = 0 ; j<3; j++) {
         
            checkBoxes |= arrayHasDuplicates(subArray(array,i,j));
         
         }
      }   
      return checkBoxes;
   }
}   
