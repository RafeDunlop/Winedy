package seng202.team3.unittests.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng202.team3.models.DrinkerPreferenceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DrinkerPreferenceModelTest {

    private static DrinkerPreferenceModel drinkerPreferenceModel = new DrinkerPreferenceModel();
    private static ArrayList<String> prefNames = new ArrayList<>(Arrays.asList("Red", "DRY", "Shiraz", "ABV"));
    private static ArrayList<Float> prefValues = new ArrayList<>(Arrays.asList((float) 5.0, (float) 7.0,(float) 6.0, (float)3.0));
    private static String username = "testing";
    private static HashMap mockModel = new HashMap<String, Float>();
    @BeforeAll
    public static void setupPrefModel(){
        drinkerPreferenceModel.setHashMapValues(prefNames,prefValues);
        drinkerPreferenceModel.setUsername(username);

        for(int i = 0; i < prefNames.size(); i++){
            mockModel.put(prefNames.get(i).toLowerCase(),prefValues.get(i));
        }
    }
    @Test
    public void testSetUsername(){
        assertEquals(username, drinkerPreferenceModel.getUsername());
    }
    @Test
    public void testGetABV(){
        assertEquals(3.0, drinkerPreferenceModel.getABV());
    }
    @Test
    public void testGetPreferenceHashMap(){
        assertEquals(mockModel, drinkerPreferenceModel.getPreferencesHashMap());
    }

    @Test
    public void testGetPrefByAttr(){
        assertEquals(5.0, drinkerPreferenceModel.getPrefValByAttr("Red"));
    }
}
