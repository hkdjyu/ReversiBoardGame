/**
 * CSCI1130 Java Assignment 6 BoardGame Reversi
 * Aim: Practise subclassing, method overriding
 *      Learn from other subclass examples
 * 
 * I declare that the assignment here submitted is original
 * except for source material explicitly acknowledged,
 * and that the same or closely related material has not been
 * previously submitted for another course.
 * I also acknowledge that I am aware of University policy and
 * regulations on honesty in academic work, and of the disciplinary
 * guidelines and procedures applicable to breaches of such
 * policy and regulations, as contained in the website.
 *
 * University Guideline on Academic Honesty:
 *   http://www.cuhk.edu.hk/policy/academichonesty
 * Faculty of Engineering Guidelines to Academic Honesty:
 *   https://www.erg.cuhk.edu.hk/erg/AcademicHonesty
 *
 * Student Name:
 * Student ID  :
 * Date        :
 */

package boardgame;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Reversi is a TurnBasedGame
 */
public class Reversi extends TurnBasedGame /* TO-DO */ {
    
    public static final String BLANK = " ";
    String winner;


    /*** TO-DO: STUDENT'S WORK HERE ***/
    
    ////////////////////////////////////
    // CONSTRUCTOR
    ////////////////////////////////////
    
    public Reversi(){
        super(8, 8, "BLACK", "WHITE");
        this.setTitle("Reversi");
    }
    
    
    
    ////////////////////////////////////
    // METHODS
    ////////////////////////////////////
    
    @Override
    protected void initGame()
    {
        for (int y = 0; y < yCount; y++)
            for (int x = 0; x < xCount; x++){
                pieces[x][y].setText(" ");
                pieces[x][y].setBackground(Color.GRAY);
                pieces[x][y].setOpaque(true);
            }
        
        //set centre 4 pieces
        for (int y = 3; y < 5; y++)
            for (int x = 3; x < 5; x++){
                pieces[x][y].setEnabled(false);
                
                //set White pieces
                if (x-y != 0){
                    pieces[x][y].setText("BLACK");
                    pieces[x][y].setBackground(Color.BLACK);
                    pieces[x][y].setOpaque(true);     
                }
                
                //set Black pieces
                else{
                    pieces[x][y].setText("WHITE");
                    pieces[x][y].setBackground(Color.WHITE);
                    pieces[x][y].setOpaque(true);  
                }
            }
    }
    
    //---------------seperate line---------------
    
    @Override
    protected void gameAction(JButton triggeredButton, int x, int y)
    {
        
        //System.out.println("Debug1: Turn="+turn+"CP="+ currentPlayer + " gameEnded="+gameEnded);
        
        //if(mustPass(x, y))
        //    changeTurn();
        
        //check player's move is valid or not, true means perform Capture
        if(!validCheckAndCapture(x,y,true)){
            addLineToOutput("Invalid move!");
            return;
        }
        
        triggeredButton.setEnabled(false); //disable the button
        triggeredButton.setText(currentPlayer);
        
        //make it more clear
        Color animatedColor = currentPlayer.equals("BLACK") ? Color.BLACK : Color.WHITE;
        pieces[x][y].setBackground(animatedColor);
        pieces[x][y].setOpaque(true);
        
        addLineToOutput(currentPlayer + " piece at (" + x + ", " + y + ")");
        
        //move is done, change player
        changeTurn();
        
        //if no valid move can be made, change again
        if(mustPass(x, y))
            changeTurn();
        
        //if no more valid move, checkEndGame will end the game
        checkEndGame(x, y);
        //System.out.println("Debug2: Turn="+turn+"CP="+ currentPlayer + " gameEnded="+gameEnded);
        
        //when end game
        if (gameEnded)
        {   
            int countB = countPieces("BLACK"); //count black pieces
            int countW = countPieces("WHITE"); //count white pieces
            addLineToOutput("BLACK score: "+countB);
            addLineToOutput("WHITE score: "+countW);
            
            if (countB > countW){
                winner = "BLACK";
                addLineToOutput("Winner is " + winner);
                addLineToOutput("Game ended!");
            }
            else if (countW > countB){
                winner = "WHITE";
                addLineToOutput("Winner is " + winner);
                addLineToOutput("Game ended!");
            }
            else
                addLineToOutput("Draw game!");
            
            JOptionPane.showMessageDialog(null, "Game ended!");
        }
    }
    
    //---------------seperate line---------------
    
    protected boolean checkEndGame(int moveX, int moveY)
    {
        //System.out.println("Debug: checkEndGame enter");
        gameEnded = false; //by default, game is not ended
        
        //before calling checkEndGame, a pass has made already and turn is changed
        //So if pass is also a must, game is ended.
        if (mustPass(moveX, moveY)){
            System.out.println("Debug: checkEndGame.mustPass enter");
            gameEnded = true;
            return gameEnded;            
        }     
        return gameEnded;   
    }
    
    //---------------seperate line---------------
    
    // Count the pieces of player(BLACK or WHITE)
    protected int countPieces(String player){
        int count = 0;
        
        for (int i = 0; i < xCount; i++)
            for (int j = 0; j < yCount; j++)
                if(pieces[i][j].getText().equals(player))
                    count++;
        
        return count;
    }
    
    //---------------seperate line---------------
    
    //boolean capture is used for do Capture move or not
    //true = do capture, false = check validity only
    private boolean validCheckAndCapture(int x, int y, boolean capture){
        
        boolean isValid = false; //by default every moves are invalid
        
        //if the location is disabled (used), return 0
        if(!pieces[x][y].isEnabled())
            return isValid;
        
        //In the below, isValid++ when capture can be made
        //i refers to the number of opponents' chess to be captured
        //10, 20, 30... is the mode for performing capturePieces
        
        // check East side
        for(int i = 1; x+i+1 < xCount && pieces[x+i][y].getText().equals(getOpponent()); i++)
            if(pieces[x+i+1][y].getText().equals(currentPlayer)){
                if(capture)
                    capturePieces (x,y,10+i);
                isValid = true;
            }
        
        // check West side
        for(int i = 1; x-i-1 >= 0 && pieces[x-i][y].getText().equals(getOpponent()); i++)
            if(pieces[x-i-1][y].getText().equals(currentPlayer)){
                if(capture)
                    capturePieces (x,y,20+i);
                isValid = true;
            }
        
        // check South side
        for(int i = 1; y+i+1 < yCount && pieces[x][y+i].getText().equals(getOpponent()); i++)
            if(pieces[x][y+i+1].getText().equals(currentPlayer)){
                if(capture)
                    capturePieces (x,y,30+i);
                isValid = true;
            }
        
        // check North side
        for(int i = 1; y-i-1 >= 0 && pieces[x][y-i].getText().equals(getOpponent()); i++)
            if(pieces[x][y-i-1].getText().equals(currentPlayer)){
                if(capture)
                    capturePieces (x,y,40+i);
                isValid = true;
            }
        
        // check Southeast side
        for(int i = 1; x+i+1 < xCount && y+i+1 < yCount && pieces[x+i][y+i].getText().equals(getOpponent()); i++)
            if(pieces[x+i+1][y+i+1].getText().equals(currentPlayer)){
                if(capture)
                    capturePieces (x,y,50+i);
                isValid = true;
            }
        
        // check Northwest side
        for(int i = 1; x-i-1 >= 0 && y-i-1 >= 0 && pieces[x-i][y-i].getText().equals(getOpponent()); i++)
            if(pieces[x-i-1][y-i-1].getText().equals(currentPlayer)){
                if(capture)
                    capturePieces (x,y,60+i);
                isValid = true;
            }
        
        // check Northeast side
        for(int i = 1; x+i+1 < xCount && y-i-1 >= 0 && pieces[x+i][y-i].getText().equals(getOpponent()); i++)
            if(pieces[x+i+1][y-i-1].getText().equals(currentPlayer)){
                if(capture)
                    capturePieces (x,y,70+i);
                isValid = true;
            }
        
        // check Southwest side
        for(int i = 1; x-i-1 >= 0 && y+i+1 < yCount && pieces[x-i][y+i].getText().equals(getOpponent()); i++)
            if(pieces[x-i-1][y+i+1].getText().equals(currentPlayer)){
                if(capture)
                    capturePieces (x,y,80+i);
                isValid = true;
            }
        
        //System.out.println("Debug: isValid=" + isValid);
        return isValid; //move is not valid if all 8 directions have no chess to be captured
    }
    
    //---------------seperate line---------------
    
    private void capturePieces(int x, int y, int type){
        
        //int type are in the format AB
        //A is for direction
        //B is for number of pieces to be fliped
        //e.g. 54, 5 = southeast, 4 = 4 pieces
        
        int category = type/10; //obtain A
        int number = type % 10; //obtain B
        
        switch(category){
            case 1:
                for(int i = 1; i<=number; i++){
                    pieces[x+i][y].setText(currentPlayer);
                    Color animatedColor = currentPlayer.equals("BLACK") ? Color.BLACK : Color.WHITE;
                    pieces[x+i][y].setBackground(animatedColor);
                }
                break;
                
            case 2:
                for(int i = 1; i<=number; i++){
                    pieces[x-i][y].setText(currentPlayer);
                    Color animatedColor = currentPlayer.equals("BLACK") ? Color.BLACK : Color.WHITE;
                    pieces[x-i][y].setBackground(animatedColor);
                }
                break;
            
            case 3:
                for(int i = 1; i<=number; i++){
                    pieces[x][y+i].setText(currentPlayer);
                    Color animatedColor = currentPlayer.equals("BLACK") ? Color.BLACK : Color.WHITE;
                    pieces[x][y+i].setBackground(animatedColor);
                }
                break;
            
            case 4:
                for(int i = 1; i<=number; i++){
                    pieces[x][y-i].setText(currentPlayer);
                    Color animatedColor = currentPlayer.equals("BLACK") ? Color.BLACK : Color.WHITE;
                    pieces[x][y-i].setBackground(animatedColor);
                }
                break;
                
            case 5:
                for(int i = 1; i<=number; i++){
                    pieces[x+i][y+i].setText(currentPlayer);
                    Color animatedColor = currentPlayer.equals("BLACK") ? Color.BLACK : Color.WHITE;
                    pieces[x+i][y+i].setBackground(animatedColor);
                }
                break;
            
            case 6:
                for(int i = 1; i<=number; i++){
                    pieces[x-i][y-i].setText(currentPlayer);
                    Color animatedColor = currentPlayer.equals("BLACK") ? Color.BLACK : Color.WHITE;
                    pieces[x-i][y-i].setBackground(animatedColor);
                }
                break;
                
            case 7:
                for(int i = 1; i<=number; i++){
                    pieces[x+i][y-i].setText(currentPlayer);
                    Color animatedColor = currentPlayer.equals("BLACK") ? Color.BLACK : Color.WHITE;
                    pieces[x+i][y-i].setBackground(animatedColor);
                }
                break;
                
            case 8:
                for(int i = 1; i<=number; i++){
                    pieces[x-i][y+i].setText(currentPlayer);
                    Color animatedColor = currentPlayer.equals("BLACK") ? Color.BLACK : Color.WHITE;
                    pieces[x-i][y+i].setBackground(animatedColor);
                }
                break;
        }
    }    
    
    //---------------seperate line---------------
    
    //return true when no valid move can be made
    private boolean mustPass(int x, int y){
        //System.out.println("Debug: mustPass enter");
        for(int i = 0; i<xCount; i++)
            for(int j = 0; j<yCount; j++)
                if(validCheckAndCapture(i, j, false))
                    return false;
                
            
        addLineToOutput("Pass!");
        return true;
    }
    
    
    
    ////////////////////////////////////
    // MAIN
    ////////////////////////////////////
    
    public static void main(String[] args)
    {
        Reversi reversi;
        reversi = new Reversi();
        
        // TO-DO: run other classes, one by one
        System.out.println("You are running class Reversi");
        
        // TO-DO: study comment and code in other given classes
        
        // TO-DO: uncomment these two lines when your work is ready
        reversi.setLocation(400, 20);
        reversi.verbose = false;

        // the game has started and GUI thread will take over here
    }
}
