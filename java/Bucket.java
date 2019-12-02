import bagel.util.Point;
import bagel.util.Vector2;

public class Bucket extends Piece {
    //Creating a constant Point with the coordinates of the desired starting position when a new bucket is made.
    private static final Point START_POINT = new Point(512, 744);

    private static final int vel_mag=4;
    private static final int reverse=-2;

    /**
     * Constructor of the Bucket class.
     * @param imagePath   Location of where the Bucket image is stored.
     * @return   Nothing.
     */
    Bucket(String imagePath){
        super(imagePath);
        setPosition(START_POINT.x,START_POINT.y);
        setVelocity(Vector2.left.mul(vel_mag));
    }

    /**
     * Reverses the direction of the bucket when it reaches the end of the screen.
     * @return   Nothing.
     */
    public void updateVelocity(){
        if (getPosition().x <= 0 || getPosition().x >= GameBoard.MAX_X) {
            setVelocity(getVelocity().add(getVelocity().mul(reverse)));
        }
    }


}
