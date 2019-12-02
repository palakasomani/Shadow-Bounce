import bagel.Image;
import bagel.util.Rectangle;
import bagel.util.Vector2;

public abstract class Piece {
    private Vector2 position;
    private Image image;
    private boolean isAlive;

    private Vector2 velocity = new Vector2();


    /**
     * <p>
     *     Constructor of the piece class which sets the status of the piece as alive.
     *     The imagePath is a string that contains the location of the image of the piece.
     * </p>
     * @param imagePath   Location of the image for the piece.
     */

    public Piece(String imagePath) {
        image = new Image(imagePath);
        isAlive = true;

    }

    /**
     * Draws the piece onto the screen window at its position vector.
     * @return   Nothing.
     */
    public void drawPiece(){
        image.draw(position.x,position.y);
    }

    /**
     * Returns the position vector of the piece.
     * @return   Returns the position vector.
     */

    public Vector2  getPosition(){
        return position;
    }

    /**
     * Sets the position vector of the piece to the desired co-ordinates.
     * @param x   The x co-ordinate of the position of the piece.
     * @param y   The y co-ordinate of the position of the piece.
     * @returm   nothing.
     */
    public void setPosition(double x, double y){
        this.position=new Vector2(x, y);
    }

    /**
     * Sets the position vector of the piece to the desired position.
     * @param position   The position vector of the piece.
     * @return    nothing.
     */
    public void setPosition(Vector2 position){
        this.position=position;
    }
    /**
     * Sets the alive status of the piece to the desired boolean value.
     * @param isAlive   A boolean whether the piece is alive or dead.
     * @return nothing.
     */
    public void setAlive(boolean isAlive){
        this.isAlive=isAlive;
    }

    /**
     * Returns whether the given piece is alive or not.
     * @return   Returns the boolean.
     */
    public boolean getAlive(){
        return isAlive;
    }

    /**
     * Checks whether another piece object intersects with this piece object or not.
     * @param piece   The piece object whom we want to check if it intersects with this piece or not.
     * @return   Whether the given piece intersects with the other piece or not.
     */
    public boolean checkIntersect(Piece piece){
        return this.image.getBoundingBoxAt(this.position.asPoint()).intersects(piece.image.getBoundingBoxAt(piece.position.asPoint()));
    }

    /**
     * Returns a rectangle around the centre of the piece.
     * @return   returns the rectangle.
     */
    public Rectangle rectanglePiece(){
        return image.getBoundingBoxAt(position.asPoint());
    }

    /**
     * Sets the image with the desired picture.
     * @param imagepath   The location of the piece image.
     * @return   Nothing.
     */

    public void setImage(String imagepath){
        image = new Image(imagepath);
    }

    /**
     * Used to access the image of the piece.
     * @return   Returns the image of the piece.
     */
    public Image getImage(){
        return this.image;
    }

    /**
     * Updates the position of the piece each frame by adding the velocity.
     * @return   Nothing.
     */
    public void updatePosition(){
        position = position.add(velocity);

    }

    /**
     * Used to access the velocity of the piece.
     * @return   Returns the velocity of the piece.
     */
    public Vector2 getVelocity(){
        return velocity;
    }

    /**
     * Used to set the velocity of the piece to a given velocity.
     * @param velocity   The velocity that the piece is assigned.
     * @return   Nothing.
     */
    public void setVelocity(Vector2 velocity){
        this.velocity=velocity;
    }
}
