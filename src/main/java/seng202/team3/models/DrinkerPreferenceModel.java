package seng202.team3.models;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Drinker preference model to work with Recommendation DAO to provide the preference model
 * to the recommendation algorithm
 *
 * @author Steven Leishman (sle159)
 */
public class DrinkerPreferenceModel {

    /**
     * The username of the Wine Drinker whose preference model this is
     */
    private String username;

    /**
     * A hashmap that maps the preference types to their preference values
     */
    private HashMap<String, Float> preferencesHashMap = new HashMap<>();

    /**
     * Empty default constructor
     * ToDo remove if unused
     */
    public DrinkerPreferenceModel() {

    }

    /**
     * sets the arraylist of the
     *
     * @param usernameToSet username for current drinker preference model
     */
    public void setUsername(String usernameToSet){
        this.username = usernameToSet;
    }

    /**
     * Takes two Arraylists of strings and floats of equal length representing the users preference model
     * and stores it in the preferences hash map
     *
     * @param prefNames Arraylist of Strings representing preference types
     * @param prefNums Arraylist of Floats representing preference value for each string
     */
    public void setHashMapValues(ArrayList<String> prefNames, ArrayList<Float> prefNums) {
        for (int i = 0; i < prefNames.size(); i++) {
            if (preferencesHashMap.get(prefNames.get(i)) == null) {
                preferencesHashMap.put(prefNames.get(i).toLowerCase(), prefNums.get(i));
            }
        }
    }

    /**
     * Returns the float preference value of a given string attribute
     *
     * @param attributeToGet String representation of attribute to retrieve
     * @return the float score value of the preference requested
     */
    public float getPrefValByAttr(String attributeToGet) {
        return preferencesHashMap.get(attributeToGet.toLowerCase());
    }

    /**
     * Returns the Attribute score pair for all attributes in the form of a hashmap
     *
     * @return preferenceHashMap - the hash map of String attributes mapped to float scores
     */
    public HashMap<String, Float> getPreferencesHashMap(){
        return preferencesHashMap;
    }

    /**
     * Get the username stored with this preference mode
     *
     * @return String of username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns the current abv preference score
     * ABV works differently and is always based of the users current preferences
     *
     * @return current abv preference score
     */
    public float getABV(){
        return preferencesHashMap.get("abv");
    }

    /**
     * Returns the number of columns/attributes stored in the hash map by iterating over the key set and incrementing a
     * counter
     *
     * @return the number of attributes in the hash map
     */
    public int getNumColumns() {
        int i = 0;
        for(String key : preferencesHashMap.keySet()){
            i++;
        }
        return i;
    }
}
