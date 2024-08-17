package seng202.team3.models;

import java.util.List;

/**
 * Wine class for creating wine objects that will be stored in a database
 */
public class Wine {
    private String name;
    private String type;
    private String personalWineEntry;
    private int uniqueWineID;
    private String country;
    private int year;
    private String shortDescription;
    private String longDescription;
    private float pricePerBottle;
    private List<String> awards;
    private float alcoholByVolume;
    private float volumeInMl;

    /**
     * Constructor for the Wine object
     * @param name of the wine
     * @param type of wine, either red, white or rose
     * @param uniqueWineID identifying value for the wine
     * @param country that the wine was made in
     * @param shortDescription 1-2 word description of the wine
     * @param longDescription longer description of the wine
     * @param awards List of awards the wine has won
     * @param pricePerBottle float of the price per bottle
     * @param alcoholByVolume percentage of alcohol content in the bottle
     * @param volumeInMl volume per bottle in ml
     */
    public Wine(String name, String type, int uniqueWineID, String country, String shortDescription, String longDescription, List<String> awards, float pricePerBottle, float alcoholByVolume, float volumeInMl) {
        this.name = name;
        this.type = type;
        this.uniqueWineID = uniqueWineID;
        this.country = country;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.awards = awards;
        this.pricePerBottle = pricePerBottle;
        this.alcoholByVolume = alcoholByVolume;
        this.volumeInMl = volumeInMl;
    }

    /**
     * Get name of the wine
     * @return a String with the name of the wine
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of the
     * @param name a String with the name of the wine
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the type of the wine
     * @return Wine type, either red, white or rose
     */
    public String getType() { return type;}

    /**
     * Sets the type of wine
     * @param type of wine, either red, white or rose
     */
    public void setType(String type) {
        this.type = type;
    }


    /**
     * Gets the wine entry recorded by the wine drinker
     * @return Personal wine entry
     */
    public String getPersonalWineEntry() {
        return personalWineEntry;
    }

    /**
     * Gets the unique wine id
     * @return Unique wine id
     */
    public int getUniqueWineID() {
        return uniqueWineID;
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
    public String getShortDescription() {
        return shortDescription;
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
        return pricePerBottle;
    }

    /**
     * Gets the list of awards the wine has won
     * @return the list of the wine's awards
     */
    public List<String> getAwards() {
        return awards;
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
     * Sets the personal wine entry for a wine drinker
     * @param personalWineEntry Wine note taken by the user
     */
    public void setPersonalWineEntry(String personalWineEntry) {
        this.personalWineEntry = personalWineEntry;
    }

    /**
     * Sets the personal wine ID
     * @param uniqueWineID Integer identifier of the wine
     */
    public void setUniqueWineID(int uniqueWineID) {
        this.uniqueWineID = uniqueWineID;
    }

    /**
     * Sets the country the wine is from
     * @param country String containing the country the wine is made
     */
    public void setCountry(String country) {
        this.country = country;
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
     * @param shortDescription a String containing one or two words describing the wine
     */
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
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
        this.pricePerBottle = pricePerBottle;
    }

    /**
     * Sets the list of awards the wine has won
     * @param awards A list of the awards
     */
    public void setAwards(List<String> awards) {
        this.awards = awards;
    }

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

