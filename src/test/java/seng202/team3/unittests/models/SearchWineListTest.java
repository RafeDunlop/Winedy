package seng202.team3.unittests.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.Wine;

import java.util.Collections;

public class SearchWineListTest {
    SearchWineList testSearchWineList;

    Wine testWine;

    @BeforeEach
    public void setup() {
        testSearchWineList = new SearchWineList(Collections.emptyList(), 0, 0, 0f, 0f,
                null, null, null, null);

        testWine =  new Wine(
                0,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0, null,
                0,
                0,
                0);
    }

    @Test
    public void addWineToListTest() {
        testSearchWineList.addWineToList(testWine);
        assertTrue(testSearchWineList.getWineList().contains(testWine));
    }

    @Test
    public void removeWineFromList() {
        testSearchWineList.addWineToList(testWine);
        testSearchWineList.removeWineFromList(testWine);
        assertTrue(testSearchWineList.getWineList().isEmpty());
    }

    @Test
    public void removeNonExistentWineFromList() {
        assertFalse(testSearchWineList.getWineList().remove(testWine));
    }

    @Test
    public void addWineRemoveWineRemoveWine() {
        testSearchWineList.addWineToList(testWine);
        testSearchWineList.removeWineFromList(testWine);
        assertFalse(testSearchWineList.getWineList().remove(testWine));
    }
}
