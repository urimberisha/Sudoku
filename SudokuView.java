import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;

/* SudokuView handles all the code for what's shown to the user.
   It doesn't save any values for the actual game, only values
   used in this class for drawing/showing stuff.            */

public class SudokuView extends JPanel {

   private SudokuModel puzzle;
   public STATE state;   
   private JFrame f;    
   private Color titleColor, bgColor; 
   private Font titleFont;
   private final int SIZE = 460, BOXSIZE = 50;   
   private int moX, moY, count, inputValue;     
   public boolean gameSolved;  

   public SudokuView() {
   
      state = STATE.MENU;
      titleColor = new Color(236,239,244);
      titleFont = new Font("Century Gothic", Font.PLAIN, 28);       
      bgColor = new Color(76,86,106);
      initializeFrame();
      
   }
   
   // Painting different stuff based on the current selected state
   public void paintComponent(Graphics g) {
  
      if (state != STATE.GAME) {
         drawBackground(0,0,g);
         drawHeader(g);         
      }
                     
      if (state == STATE.MENU){
         drawMainMenu(g);
      }
            
      if (state == STATE.DIFF) {
         drawHeader(g);      
         drawDiff(g);               
      }
      
      if (state == STATE.RULES) {
         drawRules(g);
      }
      
      if (state == STATE.ABOUT) {
         drawAbout(g);
      }
      
      if (state == STATE.GAME) {
         drawSudokuGame(puzzle, g);
      }
      
   }
  
   // Initializes and changes the settings of our frame
   public void initializeFrame() {
   
      f = new JFrame("Sudoku");
      f.setSize(SIZE+16, SIZE+39);
      f.getContentPane().add(this);
      f.setLocationRelativeTo(null);
      f.setResizable(false);
   
   }
   
   // Draws everything we want on the Main Menu 
   public void drawMainMenu(Graphics g) {
   
      g.setFont(titleFont);    
      g.setColor(titleColor);           
      g.drawString("PLAY",196,210);
      g.drawString("RULES",194,260);
      g.drawString("ABOUT",187,310);            
      g.drawString("QUIT",202,360);   
   
   }   
 
   // Draws the background
   public void drawBackground(int x, int y, Graphics g) {

      g.setColor(bgColor);
      g.fillRect(x,y,SIZE,SIZE);
   
   }
   
   // Draws the SUDOKU "logo" on top
   public void drawHeader(Graphics g) {
      
      g.setColor(titleColor);
      g.setFont(new Font("Century Gothic", Font.BOLD, 60));
      g.drawString("SUDOKU",117,100);   
   
   }
   
   // Draws the Difficulty screen (the one shown after you click PLAY)
   public void drawDiff(Graphics g) {
   
      String[] text = {"DIFFICULTY", "BACK", "Easy", "Medium", "Hard"};
      drawBackground(150,185,g);         
      g.setFont(titleFont.deriveFont(30f));       
      g.setColor(titleColor);
      g.drawString(text[0],151,196);
      g.drawString(text[1],192,410);       
      g.setFont(titleFont);            
      g.drawString(text[2],61,280);
      g.drawString(text[3],175,280);
      g.drawString(text[4],333,280);          

   }
 
   // Draws the Rules page
   public void drawRules(Graphics g) {

      int xPos = 112;
      int yPos = 170;
      Font rulesFont = titleFont.deriveFont(20f);
      String[] text = new String[] {"EACH NUMBER ONLY ONCE","EVERY ROW MUST CONTAIN","EVERY COLUMN MUST CONTAIN","EVERY BLOCK MUST CONTAIN"};
      
      // numbers
      g.setFont(rulesFont.deriveFont(46f));   
      g.drawString("1",xPos-29,yPos+20);      
      g.drawString("2",xPos-30,yPos+80);  
      g.drawString("3",xPos-30,yPos+140);  
                
      // 1st rule             
      g.setFont(customFont(rulesFont, 0.005));
      g.drawString(text[1],xPos,yPos);
      g.setFont(rulesFont);     
      g.drawString(text[0],xPos,yPos+20);      
      
      // 2nd rule
      g.setFont(customFont(rulesFont, -0.082));          
      g.drawString(text[2],xPos,yPos+60);
      g.setFont(rulesFont);      
      g.drawString(text[0],xPos,yPos+80); 
      
      // 3rd rule      
      g.setFont(customFont(rulesFont, -0.038)); 
      g.drawString(text[3],xPos,yPos+120);
      g.setFont(rulesFont);          
      g.drawString(text[0],xPos,yPos+140);               
      
      // back button      
      g.setFont(rulesFont.deriveFont(30f));        
      g.drawString("BACK",192,410);
         
   }
   
   // Draws the About page
   public void drawAbout(Graphics g) {
   
      int xPos = 67;
      int yPos = 200;
      Font aboutFont = titleFont.deriveFont(21f);
      g.setFont(aboutFont);
      g.drawString("A Sudoku game coded in Java.", xPos, 200);
      g.setFont(customFont(aboutFont, 0.031));
      g.drawString("Made by Urim Berisha w/ love.", xPos, 200+25);
      g.setFont(customFont(aboutFont, 0.004));
      g.drawString("May, 2020, University of Prishtina.", xPos, 200+2*25);            

      g.setFont(aboutFont.deriveFont(30f));        
      g.drawString("BACK",192,410);      
   
   }  
   
   // Method customFont(Font f, double x) returns a customized form of the 
   // Font f (in argument) which is wider/narrower based on the second parameter.
   public Font customFont(Font f, double x) {
   
      Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
      attributes.put(TextAttribute.TRACKING, x);
      Font font2 = f.deriveFont(attributes);
      return font2;
      
   } 
   
   // Adds the MouseListener provided via the parameter.
   public void addSudokuListener(MouseListener ml) {
   
      addMouseListener(ml);
   
   }
   
   // Adds model based on the argument/parameter.
   public void addModel(SudokuModel m) {
   
      puzzle = m;
   
   }
   
   // Makes Frame f visible.
   public void setVisible(boolean b) {
   
      f.setVisible(b);
   
   }      
   
   // Most of the methods below are for the GameScreen,
   // a.k.a the one where the Sudoku puzzle is shown.
   
   // Changes frame's size and moves it to the center
   public void changeFrameGame() {
   
      f.setSize((50*9+5*2)+16+10,(50*9+5*2)+39+10+100);
      f.setLocationRelativeTo(null); 
   
   }
   
   // Same as the last method except it's used in the Main Menu
   public void changeFrameMenu() {
   
      f.setSize(SIZE+16, SIZE+39);
      f.setLocationRelativeTo(null);  
   
   }   
   
   // Draws all the actual game elements (such as boxes and numbers) and
   // also checks if the game is over.
   public void drawSudokuGame(SudokuModel puzzle, Graphics g) {
   
      if (puzzle.isFinished(puzzle.getMatrix()) && gameSolved == false) {
         count++;
         if (count<=1) { repaint();}
         if (count > 1) { 
            try { Thread.sleep(500); } 
            catch (Exception e){}             
            JOptionPane.showMessageDialog(null, "You did it!! Click BACK to start over.");
            gameSolved = true;            
         }
      }
      
      // Drawing background, the number boxes and assigning the values of the model
      drawBackground(g);          
      drawSquares(g);        
      assignValues(puzzle.getMatrix(), puzzle.getSmallMatrix(), g);
      
      // Drawing the Sudoku Logo
      g.setColor(new Color(76,86,106));
      g.setFont(new Font("Century Gothic", Font.BOLD, 60));      
      g.drawString("SUDOKU", 117, 72);
      
      // Drawing the BACK and SOLVE "buttons"/
      g.setColor(new Color(76,86,106,200));
      Font optionsFont = new Font("Century Gothic", Font.BOLD, 25);
      g.setFont(customFont(optionsFont, 0.08));      
      g.drawString("BA", 30,50);
      g.setFont(optionsFont);         
      g.drawString("CK", 30,70);  
      g.setFont(customFont(optionsFont, 0.37));            
      g.drawString("SO", 400,50);
      g.setFont(optionsFont);      
      g.drawString("LVE", 400,70);       
   
   }
   
   // Draws the background (with different colors)
   public void drawBackground(Graphics g) {
      g.setColor(new Color(236,239,244));
      g.fillRect(0,0,9*BOXSIZE+5*2+10,9*BOXSIZE+5*2+110);
      g.setColor(new Color(216,222,233));
      g.fillRect(160,105,150,150);
      g.fillRect(5,260,150,150);
      g.fillRect(315,260,150,150);
      g.fillRect(160,415,150,150);  
   }
   
   // Draws the boxes where the numbers will show
   public void drawSquares(Graphics g) { 
      for (int i = 0; i < 9; i++) {
         for (int j = 0; j < 9; j++) {
            g.setColor(Color.black);
            g.drawRect(5+BOXSIZE*i + i/3*5, 100 + 5+BOXSIZE*j+j/3*5, BOXSIZE, BOXSIZE);           
         }
      }
   }   
   
   // Draws the numbers provided by the matrix v parameter for the main numbers
   // and matrix u for the small/secondary numbers.
   public void assignValues(int[][] v, int[][] u, Graphics g) {
      g.setColor(Color.black);          
      for (int i = 0; i < 9; i++) {
         for (int j = 0; j < 9; j++) {
            if ( v[j][i] != 0) {
               if (puzzle.getMatrixBool()[j][i] == false) {
                  g.setColor(new Color(46,52,64,200));
                  g.setFont(new Font("Arial", Font.BOLD, 17));
               }
               else {                                 
                  g.setFont(new Font("Arial", Font.PLAIN, 17));          
                  g.setColor(new Color(000,000,000));
               }
               g.drawString("" + v[j][i], 5 + 22 + BOXSIZE*i + i/3*5, 5 + 100 + 30 + BOXSIZE*j + j/3*5);

            }
            if (u[j][i] != 0) {
               g.setColor(new Color(000,000,000));
               g.setFont(new Font("Arial", Font.PLAIN, 12)); 
               g.drawString("" + u[j][i], 45 + BOXSIZE*i + i/3*5, 5 + 100 + 45 + BOXSIZE*j + j/3*5);
            }
         }
      }   
   }   

}
