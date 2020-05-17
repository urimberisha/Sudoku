import java.awt.event.*;
import javax.swing.*;


// The Controller coordinates interactions
// between the View and Model

public class SudokuController {
	
	private SudokuView theView;
	private SudokuModel theModel;
	
	public SudokuController(SudokuView theView, SudokuModel theModel) {
		this.theView = theView;
		this.theModel = theModel;
		
      		// Creating a SudokuListener object and adding the view and model objects to it
      		SudokuListener listener = new SudokuListener();
      		listener.addModel(theModel);     
      		listener.addView(theView);

      		// The view needs model to show the Sudoku values
      		// as well as the mouse listener to make it playable
      		theView.addModel(theModel);       
		theView.addSudokuListener(listener);
      
      
	}
}

/* SudokuListener contains all the code for playing the game with mouse.
   It's basically all if & else-s for mouse coordinates and the code that
   should be executed when clicked in that specific position.          */

class SudokuListener implements MouseListener{

   int moX, moY;
   DIFFICULTY difficulty = DIFFICULTY.EASY;
   SudokuModel puzzle;
   SudokuView view;
   String inputString;
   int inputValue;


   // An enum to save the difficulty choosen by user 
   private enum DIFFICULTY {
   
      EASY,
      MEDIUM,
      HARD
   
   };   
   
   public void addModel(SudokuModel m) {
   
      puzzle = m;
   
   }
   
   public void addView(SudokuView v) {
   
      view = v;
   
   }   
   
   // Returns the difficulty as a String
   // since we use String parameters for the Model
   public String getDifficulty() {
      String d = "easy";
      switch (difficulty) {
      
         case EASY: d = "easy"; break;
         case MEDIUM: d = "medium"; break;
         case HARD: d = "hard"; break;                  
      
      }
      return d;
   }
   
   // Used for the SOLVE button, shows the solution and makes it unplayable
   public void solveGame(){
   
      view.gameSolved = true;
      puzzle.solveMatrix();
      view.repaint();
      JOptionPane.showMessageDialog(null,"This is the solution. Press BACK to start over.");   
   
   }   
   
   // Adds the input to the Matrix used for the Sudoku
   // if it's allowed a.k.a if it is 0-10
   public void addInput(String inputString) {
   
         if (inputString != null && !inputString.isEmpty()) {
            try { 
               inputValue = Integer.parseInt(inputString);
            }  catch (Exception e) {
                  inputValue = puzzle.getMatrix()[moY/50][moX/50]; 
                  JOptionPane.showMessageDialog(null, "Enter values 1-9 or 0 to leave it blank.", "Not allowed!", JOptionPane.ERROR_MESSAGE);
               }
         }
         
         else if (inputString == null || inputString.isEmpty()) {
            inputValue = puzzle.getMatrix()[moY/50][moX/50];
         }
      
         if (inputValue >= 0 && inputValue <= 9){
            puzzle.getMatrix()[moY/50][moX/50] = inputValue;   
         }
         else {
            JOptionPane.showMessageDialog(null, "Enter values 1-9 or 0 to leave it blank.", "Not allowed!", JOptionPane.ERROR_MESSAGE);
         }   
   
   }      

   // Showing and doing different stuff based on the coordinates clicked with Mouse
   public void mouseExited(MouseEvent me){}
   public void mousePressed(MouseEvent me){
      
      moX = (int) me.getPoint().getX();
      moY = (int) me.getPoint().getY();    
      
      
      // Code for the Main Menu
      if (view.state == STATE.MENU){
      
         boolean playCoords = (moY>=190 && moY <=211);
         boolean rulesCoords = (moY>=240 && moY <=263);
         boolean aboutCoords = (moY>=288 && moY <=311);
         boolean quitCoords = (moY>=336 && moY <=359);
         
         if(moX>=195 && moX<=270){
               
               //Play
               if(playCoords) {
                  view.state = STATE.DIFF;
               }
               
               // Rules
               if(rulesCoords){
                  view.state = STATE.RULES;
               }
               
               // About
               if(aboutCoords){
                  view.state = STATE.ABOUT;
                  
               }
               // Quit
               if(quitCoords){
                  System.exit(0);
            }
         }
      }
      
      // Showing the BACK button in the Difficulty, Rules and About screen
      if (view.state == STATE.DIFF || view.state == STATE.RULES || view.state == STATE.ABOUT) { 
         //back button
         if (moX>=195 && moX<=273 && moY>=387 && moY <=411){   
            view.state = STATE.MENU;
         }
         
         //difficulties
         if (moY>=262 && moY <=287 && view.state == STATE.DIFF) {
            if (moX>=66 && moX<=125) { difficulty = DIFFICULTY.EASY; puzzle = new SudokuModel(getDifficulty()); view.changeFrameGame(); view.state = STATE.GAME; view.addModel(puzzle);}
            else if (moX>=177 && moX<=287) { difficulty = DIFFICULTY.MEDIUM; puzzle = new SudokuModel(getDifficulty()); view.changeFrameGame(); view.state = STATE.GAME; view.addModel(puzzle);}
            else if (moX>=337 && moX<=398) { difficulty = DIFFICULTY.HARD; puzzle = new SudokuModel(getDifficulty()); view.changeFrameGame(); view.state = STATE.GAME; view.addModel(puzzle);}  
                
         }
      view.repaint(); //repaint once a difficulty is chosen or back "button" is clicked
      }
      
      // Code for clickable places when the Game screen is shown      
      else if (view.state == STATE.GAME) {
      
         if (moY>=32 && moY <=69) {
         // Solve Button
            if (moX>=397 && moX<= 436) {
               if (!view.gameSolved) {
                  solveGame();
               }
            }
            
            // Back button
            if (moX>=33 && moX<= 65) {
               view.changeFrameMenu();
               view.state = STATE.MENU;
               view.gameSolved = false;
            }
         }               
         
         // Making sure you can only click on places you're supposed to.
         if ((moX < 5 || moX > 465) || (moY < 105 || moY > 565) || (moX > 155 && moX < 160) || (moX > 310 && moX < 315) || (moY > 255 && moY < 260) || (moY > 410 && moY < 415)) {}
         else {            
            moX = moX - 5;
            moY = moY - 105; 
            if (moX > 5+150) { moX -= 5; }
            if (moX > 5+300) { moX -= 5; }
            if (moY > 100+150) { moY -= 5; }
            if (moY > 100+300) { moY -= 5; }
         
            // Handling the right-click mouse input.
            if (SwingUtilities.isRightMouseButton(me) && me.getClickCount() == 1) {
               if (puzzle.getMatrixBool()[moY/50][moX/50] == true && !view.gameSolved) {
                  String smallInput = JOptionPane.showInputDialog(null, "New Small Value?", "");
                  try {
                     puzzle.getSmallMatrix()[moY/50][moX/50] = Integer.parseInt(smallInput);
                  } catch (Exception e) {}                                  
               }
               
            }
            // Handling the left-click mouse input.
            else if (puzzle.getMatrixBool()[moY/50][moX/50] == true && !view.gameSolved) {
                         
               inputString = JOptionPane.showInputDialog(null, "New Value?", "");
               addInput(inputString);
                  
            }
         }  
      view.repaint();
      } 

   }
   
   public void mouseEntered(MouseEvent me){}
   public void mouseReleased(MouseEvent me){} 
   public void mouseClicked(MouseEvent me){}

}
