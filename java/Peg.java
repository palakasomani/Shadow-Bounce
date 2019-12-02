
public class Peg extends Piece {
    public enum TYPE {
        BLUE,GREY,RED,GREEN;
    }
    public enum ORIENTATION {
        NORMAL,HORIZONTAL,VERTICAL;
    }

    private TYPE type;
    private ORIENTATION orientation;

    /**
     * Constructor of the peg class.
     * @param imagePath   The location of the image of the Peg.
     * @param type   The type of the peg. It can either be BLUE,GREEN,RED or GREY.
     * @param orientation   The orientation of the pef. It can either be NORMAL,HORIZONTAL or VERTICAL.
     * @return   Nothing.
     */

    public Peg(String imagePath,TYPE type, ORIENTATION orientation) {
        super(imagePath);
        this.type=type;
        this.orientation=orientation;

    }

    /**
     * Returns the type of the peg
     * @return   This returns the type of the peg which can either be BLUE,RED,GREY OR GREEN.
     */

    public TYPE getType(){
        return type;
    }

    /**
     * Returns the orientation of the peg
     * @return   This returns the orientation of the peg which can either be NORMAL,HORIZONTAL OR VERTICAL
     */

    public ORIENTATION getOrientation(){
        return orientation;
    }


    /**
     * Gets the distance between two pegs.
     * @param peg   This is the peg from which we want to find the distance of this peg from.
     * @return   This returns the distance between the two pegs in pixels.
     */

    public double getDistance(Peg peg){
        // returning magnitude of the vector from one peg location to another.
        return this.getPosition().sub(peg.getPosition()).length();
    }
    /**
     * Sets the type of the peg
     * @param type   This is the peg type we want to set to.
     * @return   nothing
     */

    public void setType(TYPE type){
        this.type=type;
    }

}
