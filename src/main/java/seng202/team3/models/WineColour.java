package seng202.team3.models;

/**
 * Colour options for wines
 *
 * @author Yuvraj Fagotra (yfa50)
 */
public enum WineColour {

    RED("Red"),
    WHITE("White"),
    ROSE("Rose");

    /**
     * The string representation of Colour
     */
    public final String colour;
    WineColour(String colour){
        this.colour = colour;
    }
}
