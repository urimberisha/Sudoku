public class MVCSudoku {
    
    public static void main(String[] args) {

    	SudokuView theView = new SudokuView();
      SudokuModel tempModel = new SudokuModel("easy");
      SudokuController theController = new SudokuController(theView, tempModel);      
      
      theView.setVisible(true);
        
    }
}