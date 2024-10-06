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
    private String username;
    private HashMap<String, Float> preferencesHashMap = new HashMap<>();

    public DrinkerPreferenceModel(){}

    /**
     * sets the arraylist of the
     * @param usernameToSet
     */
    public void setUsername(String usernameToSet){
        this.username = usernameToSet;
    }

    /**
     * Takes two Arraylists of strings and floats of equal length representing the users preference model
     * and stores it in the preferences hash map
     * @param prefNames Arraylist of Strings representing preference types
     * @param prefNums Arraylist of Floats representing preference value for each string
     */
    public void setHashMapValues(ArrayList<String> prefNames, ArrayList<Float> prefNums){
        for (int i = 0; i < prefNames.size(); i++) {
            if (preferencesHashMap.get(prefNames.get(i)) == null) {
                preferencesHashMap.put(prefNames.get(i).toLowerCase(), prefNums.get(i));
            }
        }
    }

    /**
     * Returns the float preference value of a given string attribute
     * @param attributeToGet String representation of attribute to retrieve
     * @return the float score value of the preference requested
     */
    public float getPrefValByAttr(String attributeToGet) {
        System.out.println(preferencesHashMap.get(attributeToGet.toLowerCase()));
        return preferencesHashMap.get(attributeToGet.toLowerCase());
    }

    /**
     * Returns the Attribute score pair for all attributes in the form of a hashmap
     * @return preferenceHashMap - the hash map of String attributes mapped to float scores
     */
    public HashMap<String, Float> getPreferencesHashMap(){
        return preferencesHashMap;
    }

    public String getUsername() {
        return this.username;
    }
}
