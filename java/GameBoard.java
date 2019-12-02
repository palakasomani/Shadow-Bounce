import bagel.Input;
import bagel.Window;
import bagel.util.Side;
import bagel.util.Vector2;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileReader;
import java.io.BufferedReader;

public class GameBoard {
    private static final int DIST = 70;
    // Creating a constant to store the maximum number of balls that can be on the game window at a given instant.
    private static final int MAXBALL = 3;
    private static final int MAX_SHOTS = 20;
    //Creating a constant of the maximum window height.
    public static final double MAX_Y = Window.getHeight();
    //Creating a constant of the maximum window width.
    public static final double MAX_X = Window.getWidth();

    public Random rand = new Random();


    private boolean ballsAlive = false;
    private boolean greenFlag = true;

    private Bucket bucket = new Bucket("res/bucket.png");
    private PowerUp powerup = new PowerUp("res/powerup.png");
    private Ball ball[] = new Ball[MAXBALL];
    private Side side;
    ArrayList<Peg> allPegs = new ArrayList<>();

    private int count_shots = 0;
    private int count_blue = 0;
    private int count_red = 0;


    /**
     * Constructor of the GameBoard. Initialises the maximum number of balls where each ball is stored in a list.
     * @return   Nothing.
     */
    public GameBoard() {
        for (int i = 0; i < MAXBALL; i++) {
            ball[i] = new Ball("res/ball.png");
        }
    }

    /**
     * Reads each line of the csv and adds the pegs of the required type and orientation to an array list accordingly.
     * @param location   The location of the board file in the format of a csv file.
     * @return   Nothing
     */
    public void createBoard(String location) {
        count_blue = 0;
        count_red = 0;
        try (BufferedReader br =
                     new BufferedReader(new FileReader(location))) {
            String text;
            while ((text = br.readLine()) != null) {
                String cells[] = text.split(",");
                double x = Double.parseDouble(cells[1]);
                double y = Double.parseDouble(cells[2]);
                switch (cells[0]) {
                    case "blue_peg":
                        count_blue += 1;
                        allPegs.add(new Peg("res/peg.png", Peg.TYPE.BLUE, Peg.ORIENTATION.NORMAL));
                        break;
                    case "blue_peg_vertical":
                        count_blue += 1;
                        allPegs.add(new Peg("res/vertical-peg.png", Peg.TYPE.BLUE, Peg.ORIENTATION.VERTICAL));
                        break;
                    case "blue_peg_horizontal":
                        count_blue += 1;
                        allPegs.add(new Peg("res/horizontal-peg.png", Peg.TYPE.BLUE, Peg.ORIENTATION.HORIZONTAL));
                        break;
                    case "grey_peg":
                        allPegs.add(new Peg("res/grey-peg.png", Peg.TYPE.GREY, Peg.ORIENTATION.NORMAL));
                        break;
                    case "grey_peg_vertical":
                        allPegs.add(new Peg("res/grey-vertical-peg.png", Peg.TYPE.GREY, Peg.ORIENTATION.VERTICAL));
                        break;
                    case "grey_peg_horizontal":
                        allPegs.add(new Peg("res/grey-horizontal-peg.png", Peg.TYPE.GREY, Peg.ORIENTATION.HORIZONTAL));
                        break;

                }
                allPegs.get(allPegs.size() - 1).setPosition(x, y);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Generating the required number of red pegs from randomly chosen blue pegs.
        int RED_P = (int) java.lang.Math.floor(count_blue /5);
        while (count_red != RED_P) {
            int index_red = rand.nextInt(allPegs.size() - 1);
            if (allPegs.get(index_red).getType() == Peg.TYPE.BLUE) {
                allPegs.set(index_red,changePegType((allPegs.get(index_red)),"red"));
                count_red+=1;
                }
            }
        setGreenPeg();
    }

    /**
     * Changes the peg type from one to another.
     * @param peg   The peg whose type is to be changed.
     * @param color   The desired color of the new peg.
     * @return   Returns the new peg of the required type.
     */

    public Peg changePegType(Peg peg, String color){
        Peg newPeg;
        if(peg.getOrientation()== Peg.ORIENTATION.NORMAL){
            newPeg= new Peg("res/"+color+"-peg.png", Peg.TYPE.valueOf(color.toUpperCase()),peg.getOrientation());
        }
        else{
        newPeg = new Peg("res/"+color+"-"+peg.getOrientation().toString().toLowerCase()+"-peg.png",Peg.TYPE.valueOf(color.toUpperCase()), peg.getOrientation());}
        newPeg.setPosition(peg.getPosition());
        return newPeg;
    }

    /**
     * Clears the arraylist of pegs.
     * @return   Nothing.
     */

    public void clearBoard() {
        allPegs.clear();
    }

    /**
     * Checks if any of the balls which are alive intersects with any of the pegs.
     * @return   Nothing.
     */
    public void checkContact() {
        for (int i = 0; i < MAXBALL; i++) {
            if (ball[i].getAlive()) {
                contactPeg(ball[i]);
            }
        }

    }

    /**
     * <p>
     *  Checks if the ball intersects with any of the pegs stored in the pegs array
     *  and if so, sets the status of the respective peg as false. (i.e. the peg is dead).
     * </p>
     * @param ball   The ball whose contact is to be checked with the pegs.
     * @return   Nothing.
     */
    public void contactPeg(Ball ball) {
        for (int i = 0; i < allPegs.size(); i++) {
            if (allPegs.get(i).getAlive()) {
                if (allPegs.get(i).rectanglePiece().intersects(ball.getPosition().asPoint()) ){
                    // Checking side of intersection between ball and the given peg.
                    side= allPegs.get(i).rectanglePiece().intersectedAt(ball.getPosition().asPoint(), ball.getVelocity());

                    if (side.equals(side.LEFT) || side.equals(side.RIGHT)) {
                        ball.setVelX();
                    }
                    else {
                        ball.setVelY();
                    }

                    // setting status of the peg as dead if it is not grey.
                    if (allPegs.get(i).getType() != Peg.TYPE.GREY)
                        allPegs.get(i).setAlive(false);
                    // checking if the peg contacted was green, then calls the desired function.
                    if (allPegs.get(i).getType() == Peg.TYPE.GREEN) {
                        greenContact();
                    }
                    // Checking if the ball was a fireball and if so destroying all the pegs within a given distance.
                    if (ball.getType() == Ball.TYPE.FIRE) {
                        for (int j = 0; j < allPegs.size(); j++) {
                            if (allPegs.get(j).getAlive()) {
                                if (allPegs.get(i).getDistance(allPegs.get(j)) <= DIST) {
                                    if (allPegs.get(j).getType() != Peg.TYPE.GREY)
                                        allPegs.get(j).setAlive(false);
                                    if (allPegs.get(j).getType() == Peg.TYPE.GREEN) {
                                        greenContact();
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    /**
     * Checks if any of the balls are alive.
     * @return   Nothing.
     */

    public void checkBallsAlive() {
        for (int i = 0; i < MAXBALL; i++) {
            if (ball[i].getAlive()) {
                ballsAlive = true;
                return;
            }
        }
        ballsAlive = false;
    }

    /**
     * <p>
     *          Sets the velocities of the two new balls generated on contact with the green peg as desired.
     *          Also sets their desired type and position.
     * </p>
     * @return   Nothing.
     */
    public void greenContact() {
        ball[1].setInitialVelocity(Vector2.left.add(Vector2.up).normalised().mul(10));
        ball[2].setInitialVelocity(Vector2.right.add(Vector2.up).normalised().mul(10));
        for (int i = 1; i < MAXBALL; i++) {
            ball[i].setPosition(ball[0].getPosition().x, ball[0].getPosition().y);
            ball[i].setAlive(true);
            ball[i].setType(ball[0].getType());
            ball[i].drawPiece();
        }
    }

    /**
     * Draws all of the pegs stored in the pegs arraylist if they are alive.
     * @return   Nothing
     */
    public void drawPegs() {
        for (int i = 0; i < allPegs.size(); i++) {
            if (allPegs.get(i).getAlive()) {
                allPegs.get(i).drawPiece();
            }
        }
    }


    /**
     * Generates the balls if they can be generated.
     * @param input   The input from the mouse
     * @return   Nothing
     */
    public void initialiseBall(Input input) {
        for (int i = 0; i < MAXBALL; i++) {
            if (!ball[i].canGenerate()) {
                return;
            }
        }
        genBall(input);
    }

    /**
     * Generates the ball and sets the desired initial position and velocity.
     * @param input   input from the mouse
     */
    public void genBall(Input input) {
        ball[0].setType(Ball.TYPE.NORMAL);
        IncCountShots();
        ball[0].setInitialVelocity(input);
        ball[0].setInitialPosition();
        ball[0].setAlive(true);
        ball[0].drawPiece();

    }

    /**
     * Updates any of the ball if alive as desired.
     * @return   Nothing
     */
    public void updateBall() {
        for (int i = 0; i < MAXBALL; i++) {
            if (ball[i].getAlive()) {
                Vector2 prevPosition=ball[i].getPosition();
                ball[i].updateVelocity();
                ball[i].updatePosition();
                Vector2 newPosition = ball[i].getPosition();

                //Checking if the ball contacts the bucket by checking if in one frame the ball intersects with the bucket and in the very next frame it doesn't
                if(bucket.getImage().getBoundingBoxAt(bucket.getPosition().asPoint()).intersects(prevPosition.asPoint()) && !(bucket.getImage().getBoundingBoxAt(bucket.getPosition().asPoint()).intersects(newPosition.asPoint()))){
                    // Increasing the shots by 1 by reducing total shots used till now
                    count_shots-=1;
                }

                ball[i].drawPiece();
                if (ball[i].getPosition().y > MAX_Y) {
                    ball[i].setAlive(false);
                }
            }
        }
    }

    /**
     * Checking if powerup is alive or dead.
     * @return   Returns if the powerup is alive
     */
    public boolean isPowerupAlive() {
        return powerup.getAlive();
    }

    /**
     * Updates the powerup.
     * @return   Nothing.
     */
    public void updatePowerup() {
        powerup.drawPiece();
        powerup.check();
        powerup.updatePosition();
    }


    public void generatePowerup() {
        Random rand = new Random();
        if (rand.nextDouble() <= 0.1) {
            powerup.setPosition();
            powerup.setAlive(true);
            powerup.generateVelocity();
            powerup.drawPiece();
        } else {
            powerup.setAlive(false);
        }
    }


    public void updateBucket() {
        bucket.drawPiece();
        bucket.updatePosition();
        bucket.updateVelocity();
    }

    public void IncCountShots() {
        count_shots += 1;
    }

    public int getCountShots() {
        return count_shots; }

    public int getMax() {
        return MAX_SHOTS;
    }

    public void checkPowerup() {
        for (int i = 0; i < MAXBALL; i++) {
            if (ball[i].getAlive()) {
                if (ball[i].checkIntersect(powerup)) {
                    ball[i].setType(Ball.TYPE.FIRE);
                }
            }
        }
    }

    public boolean checkForRed() {
        for (int i = 0; i < allPegs.size(); i++) {
            if (allPegs.get(i).getType() == Peg.TYPE.RED) {
                if (allPegs.get(i).getAlive()) {
                    return true;
                }
            }
        }
        return false;
    }


    public void setGreenPeg() {
        if (!ballsAlive) {
            if (greenFlag) {
                greenFlag = false;

                for (int i = 0; i < allPegs.size(); i++) {
                    if (allPegs.get(i).getType() == Peg.TYPE.GREEN) {
                        allPegs.get(i).setType(Peg.TYPE.BLUE);
                        if (allPegs.get(i).getOrientation() == Peg.ORIENTATION.NORMAL) {
                            allPegs.get(i).setImage("res/peg.png");
                        } else {
                            allPegs.get(i).setImage("res/" + allPegs.get(i).getOrientation().toString().toLowerCase() + "-peg.png");
                        }
                    }
                }
                int index_green = rand.nextInt(allPegs.size() - 1);
                while (!allPegs.get(index_green).getAlive() || !(allPegs.get(index_green).getType() == Peg.TYPE.BLUE)) {
                    index_green = rand.nextInt(allPegs.size() - 1);
                }
                allPegs.set(index_green,changePegType((allPegs.get(index_green)),"green"));

            }
        }
    }

    public void setBallsFalse() {
        for (int i = 0; i < MAXBALL; i++) {
            ball[i].setAlive(false);
        }
    }

    public void setGreenFlag(boolean greenFlag) {
        this.greenFlag = greenFlag;
    }

    public boolean getBallsAlive() {
        return this.ballsAlive;
    }

    /**
     * Calls several functions which need to be called on every update cycle.
     * @return   Nothing.
     */
    public void playGame(){
        //Checking if any of the active ball is in contact with any of the pegs.
        drawPegs();
        checkBallsAlive();
        // Setting a random blue peg to green after every turn.
        setGreenPeg();

        //Checking if any of the active ball is in contact with any of the pegs.
        checkContact();
        updateBall();
        checkBallsAlive();

        //Updating the powerup if it is alive and checking if any ball intersects it

        if(isPowerupAlive()){
            updatePowerup();
            checkPowerup();
        }
        updateBucket();
    }

}