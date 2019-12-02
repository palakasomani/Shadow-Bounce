import bagel.*;
import bagel.util.Point;
import bagel.util.Vector2;
public class Ball extends Piece{

    private static final double VEL_G =0.15;
    //Creating a constant Point with the coordinates of the desired starting position when a new ball is made.
    private static final Point START_POINT = new Point(512, 32);

    private static final int VELOCITY_MAGNITUDE = 10;
    private static final String BALL_LOCATION="res/ball.png";
    private static final String FIREBALL_LOCATION = "res/fireball.png";

    public enum TYPE {
        NORMAL,FIRE;
    }
    private TYPE type;
    private static final Vector2 velocityY = Vector2.down.normalised().mul(VEL_G);

    /**
     * Constructor of the Ball Class.
     * @param imagePath   Location of the image of the ball.
     */

    Ball(String imagePath){
        super(imagePath);
        setPosition(START_POINT.x,START_POINT.y);
        setAlive(false);
    }


    /**
     * <p>
     *     Checking if a new ball can be generated or not.
     *     (i.e returns true if there is no ball on the screen/ if the position of the ball is
     *     below the bottom of the screen.
     * </p>
     * @return   Whether a new ball can be generated or not.
     */

    public boolean canGenerate(){
        if(getAlive()){
            if(this.getPosition().asPoint().y >= GameBoard.MAX_Y){
                setAlive(false);
                return true;
            }
            return false;
        }

        return true;
    }





    //Setting initial velocity of a new ball as desired.
    public void setInitialVelocity(Input input){
        // assigning velocity of the ball towards the mouse click from the desired start position with the desired magnitude.
        setVelocity(input.directionToMouse(START_POINT).mul(VELOCITY_MAGNITUDE));
    }

    public void setInitialPosition(){
        setPosition(START_POINT.x,START_POINT.y);
    }

    public void setInitialVelocity(Vector2 vector) {
        setVelocity(vector);
    }

    //Updating the velocity of the ball in each frame by adding a vector of magnitude 0.15 pointing downwards.
    public void updateVelocity(){
        setVelocity(getVelocity().add(velocityY));
        if(getPosition().x <= 0 || getPosition().x >= GameBoard.MAX_X){
            setVelX();
        }

    }

    //Updating the position of the ball in each frame by adding the velocity at that instant.
    public void updatePosition(){
        //setPosition(getPosition().add(velocity).x,getPosition().add(velocity).y);
        // Checking if the ball passes the bottom of the game window and if so set's it as dead.
        super.updatePosition();
        if(getPosition().asPoint().y>GameBoard.MAX_Y){
            setAlive(false);
        }
    }
    public void setVelY(){
        /*adds twice the y component of the velocity of the ball at that stage in the opposite direction
        so as to ensure the ball's y velocity component is reversed*/
        Vector2 velocityY = new Vector2(0,getVelocity().y*(-2));
        setVelocity(getVelocity().add(velocityY));
    }
    public void setVelX(){
        /*adds twice the x component of the velocity of the ball at that stage in the opposite direction
        so as to ensure the ball's x velocity component is reversed*/
        Vector2 velocityX = new Vector2((-2)*getVelocity().x,0);
        setVelocity(getVelocity().add(velocityX));
    }
    public void setType(TYPE type){
        this.type=type;
        if(type==TYPE.FIRE){
            setImage(FIREBALL_LOCATION);
        }
        else{
            setImage(BALL_LOCATION);
        }
    }

    public TYPE getType(){
        return type;
    }



}
