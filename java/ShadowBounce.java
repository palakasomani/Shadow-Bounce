import bagel.*;
public class ShadowBounce extends AbstractGame{
    private GameBoard gameBoard;
    private boolean flagPowerup =false;
    private int num=0;
    private static final int maxBoards=5;


    /**
     * Initiates a new GameBoard and loads the first board.
     * @return   Nothing.
     */
    public ShadowBounce() {
        gameBoard=new GameBoard();
        gameBoard.createBoard("res/"+num+".csv");
    }

    /**
     * @param args   Not used.
     * @return   Nothing.
     */

    public static void main(String[] args) {
        ShadowBounce game = new ShadowBounce();
        game.run();
    }

    /**
     *
     * @param input Provides access to input devices (in this case the mouse).
     */

    @Override
    public void update(Input input) {
        //Exiting the game window if escape key is pressed.
        if (input.wasPressed(Keys.ESCAPE) || gameBoard.getCountShots()>gameBoard.getMax()) {
            Window.close();
        }

        gameBoard.playGame();

        if(input.wasPressed(MouseButtons.LEFT)){
                gameBoard.initialiseBall(input);
                flagPowerup = false;
                gameBoard.setGreenFlag(true);
        }
        /*Loading a new board if all red pegs are destroyed
        and if there are no more game boards the load, the game window is closed*/
       if(!gameBoard.checkForRed() && num<maxBoards){
           num+=1;
           if(num==maxBoards){
               Window.close();
           }
           else{
               gameBoard.setBallsFalse();
               gameBoard.setGreenFlag(true);
               gameBoard.clearBoard();
               gameBoard.createBoard("res/"+num+".csv");
           }
       }

       /*Checking if there are no balls on the screen
        and calling the method to generate a powerup only once every turn which is ensured by the flag. */
       if(!gameBoard.getBallsAlive() && !flagPowerup) {
           gameBoard.generatePowerup();
           flagPowerup = true;
       }
    }

}

