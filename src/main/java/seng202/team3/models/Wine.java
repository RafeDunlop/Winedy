package seng202.team3.models;

import java.util.Objects;

/**
 * Wine class for creating wine objects that will be stored in a database
 *
 * @author Sophia Copley (sco207)
 */
public class Wine {
    /**
     * unique ID for the wine
     */
    private int uniqueWineID;
    /**
     * Wine name
     */
    private String name;
    /**
     * Wine colour out of red, white and rose
     */
    private String colour;
    /**
     * Wine style
     */
    private String style;
    /**
     * Personal wine entry associated with the wine that has been entered by the WineDrinker
     */
    private String personalWineEntry;
    /**
     * Country that the wine is from
     */
    private String country;
    /**
     * Year that the wine was made
     */
    private int year;
    /**
     * List of grapes that the wines contain
     */
    private final String[] grapes;
    /**
     * Wine fullness
     */
    private String fullness;
    /**
     * Long description of the wine
     */
    private String longDescription;
    /**
     * Price of the wine
     */
    private float price;
    /**
     * List of awards
     */
    private String[] awards;
    /**
     * Percentage of alcohol per unit volume in the wine
     */
    private float alcoholByVolume;
    /**
     * Volume of the wine bottle
     */
    private float volumeInMl;

    /**
     * Constructor for the Wine object
     * @param uniqueWineID identifying value for the wine
     * @param name of the wine
     * @param colour of wine, either red, white or rose
     * @param country that the wine was made in
     * @param year the year that the wine was made
     * @param fullness 1-2 word description of the fullness of the wine, e.g. "dry"
     * @param longDescription longer description of the wine
     * @param awards List of awards the wine has won
     * @param price float of the price per bottle
     * @param alcoholByVolume percentage of alcohol content in the bottle
     * @param volumeInMl volume per bottle in ml
     */
    public Wine(int uniqueWineID,
                String name,
                String country,
                String colour,
                String style,
                String[] grapes,
                String fullness,
                String longDescription,
                float price,
                String[] awards,
                float alcoholByVolume,
                float volumeInMl,
                int year) {
        this.uniqueWineID = uniqueWineID;
        this.name = name;
        this.country = country;
        this.colour = colour;
        this.style = style;
        this.grapes = grapes;
        this.fullness = fullness;
        this.longDescription = longDescription;
        this.price = price;
        this.awards = awards;
        this.alcoholByVolume = alcoholByVolume;
        this.volumeInMl = volumeInMl;
        this.year = year;
    }

    /**
     * Compares unique wine ids to determine equality of two wines
     * @param other The object to compare
     * @return true if both objects refer to the same wine, false otherwise
     */
    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()){
            return false;
        }

        Wine wine = (Wine) other;

        return uniqueWineID == wine.getUniqueWineID();
    }

    /**
     * Computes hash code for wine based on the unique id
     * @return The computed hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(uniqueWineID);
    }

    /**
     * Gets the unique wine id
     * @return Unique wine id
     */
    public int getUniqueWineID() {
        return uniqueWineID;
    }

    /**
     * Get name of the wine
     * @return a String with the name of the wine
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the colour of the wine
     * @return Wine colour, either red, white or rose
     */
    public String getColour() { return colour;}

    /**
     * Gets the wine entry recorded by the wine drinker
     * @return Personal wine entry
     */
    public String getPersonalWineEntry() {
        return personalWineEntry;
    }


    /**
     * Gets the country the wine is made in
     * @return Country of the wine
     */
    public String getCountry() {
        return country;
    }

    /**
     * Gets the year that the wine is from
     * @return Year the wine was made
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the one-word description of the wine
     * @return Short description of the wine
     */
    public String getFullness() {
        return fullness;
    }

    /**
     * Gets the long description of the wine
     * @return Long description of the wine
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**
     * Gets the price of the bottle
     * @return price of the wine
     */
    public float getPricePerBottle() {
        return price;
    }

    /**
     * Gets the array of awards the wine has won
     * @return the array of the wine's awards
     */
    public String[] getAwards() {
        return awards;
    }

    /**
     * gets the array of grape types the wine is made from
     * @return the array of the wine's grapes
     */
    public String[] getGrapes() {
        return grapes;
    }

    /**
     * Gets the percentage of alcohol the wine contains
     * @return the alcohol by volume
     */
    public float getAlcoholByVolume() {
        return alcoholByVolume;
    }

    /**
     * Gets the volume of the bottle in mL
     * @return the volume of the bottle
     */
    public float getVolumeInMl() {
        return volumeInMl;
    }

    /**
     * Gets the style of the wine which classifies its colour (or colour), e.g. crisp, fruity
     * @return the style of the wine
     */
    public String getStyle() {
        return style;
    }

    /**
     * Sets the personal wine ID
     * @param uniqueWineID Integer identifier of the wine
     */
    public void setUniqueWineID(int uniqueWineID) {
        this.uniqueWineID = uniqueWineID;
    }

    /**
     * Set name of the
     * @param name a String with the name of the wine
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Sets the colour of wine
     * @param colour of wine, either red, white or rose
     */
    public void setColour(String colour) {
        this.colour = colour;
    }
    /**
     * Sets the personal wine entry for a wine drinker
     * @param personalWineEntry Wine note taken by the user
     */
    public void setPersonalWineEntry(String personalWineEntry) {
        this.personalWineEntry = personalWineEntry;
    }

    /**
     * Sets the country the wine is from
     * @param country String containing the country the wine is made
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Sets the style of the wine
     * @param style the style the wine to set to
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * Sets the year the wine is from
     * @param year the year the wine was made
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Sets the short description of the wine
     * @param fullness a String containing one or two words describing the wine
     */
    public void setFullness(String fullness) {
        this.fullness = fullness;
    }

    /**
     * Sets the long description of wine
     * @param longDescription a String containing a long description of the wine
     */
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    /**
     * Sets the price per bottle;
     * @param pricePerBottle a float with the price of the bottle
     */
    public void setPricePerBottle(float pricePerBottle) {
        this.price = pricePerBottle;
    }

    /**
     * Sets the list of awards the wine has won
     * @param awards A list of the awards
     */
    public void setAwards(String[] awards) { this.awards = awards; }

    /**
     * Sets the alcohol percentage of the wine
     * @param alcoholByVolume a float with the percentage of alcohol content in the wine
     */
    public void setAlcoholByVolume(float alcoholByVolume) {
        this.alcoholByVolume = alcoholByVolume;
    }

    /**
     * Sets the volume of the wine in ML
     * @param volumeInMl a float with the volume of the bottle of wine
     */
    public void setVolumeInMl(float volumeInMl) {
        this.volumeInMl = volumeInMl;
    }
}

