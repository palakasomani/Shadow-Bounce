import bagel.Window;
import bagel.util.Point;
import java.util.Random;

public class PowerUp extends Piece {

    Random rand=new Random();

    //Creating a constant for the magnitude of the velocity of the powerup
    private static final int VEL_MAG=3;
    //Creating a constant for the pixel distance within which powerup should choose new random position.
    private static final int STRIKING_DIST=5;
    //A point which stores the random position to where the powerup is destined to move to.
    private Point point;

    /**
     * Constructor of the powerup.
     * @param imagePath   Loccation of where the image of the powerup is stored.
     * @return   Nothing.
     */
    public PowerUp(String imagePath){
        super(imagePath);
        this.setPosition();
        setAlive(false);
        point=new Point(rand.nextDouble()* Window.getWidth(),rand.nextDouble()* Window.getHeight());
    }
    public void setPosition(){
        setPosition(rand.nextDouble()* Window.getWidth(),rand.nextDouble()* Window.getHeight());
    }


    public void generateVelocity(){
    setVelocity(point.asVector().sub(getPosition()).normalised().mul(VEL_MAG));
    }

    public void check(){
        if(Math.pow((Math.pow((getPosition().asPoint().y-point.y),2)+Math.pow((getPosition().asPoint().x-point.x),2)),0.5)<=STRIKING_DIST){
            // Generating a random point within the game window.
            point=new Point(rand.nextDouble()* Window.getWidth(),rand.nextDouble()* Window.getHeight());
            generateVelocity(); }
    }

}
