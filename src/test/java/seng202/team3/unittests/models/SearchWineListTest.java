package seng202.team3.unittests.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.Wine;

public class SearchWineListTest {
    SearchWineList testSearchWineList;

    Wine testWine;

    @BeforeEach
    public void setup() {
        testSearchWineList = new SearchWineList();

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

        if (testSearchWineList.getWineList().contains(testWine)) {
            testSearchWineList.removeWineFromList(testWine);
            assertTrue(testSearchWineList.getWineList().isEmpty());
        } else {
            fail("Wine was not added to list and can not be removed");
        }
    }

    @Test
    public void removeNonExistentWineFromList() {
        assertFalse(testSearchWineList.getWineList().remove(testWine));
    }

    @Test
    public void addWineRemoveWineRemoveWine() {
        testSearchWineList.addWineToList(testWine);

        if (testSearchWineList.getWineList().contains(testWine)) {  // If addWine does not work, this test is redundant
            testSearchWineList.removeWineFromList(testWine);
        } else {
            fail("Wine was not added to list and can not be removed");
        }

        if (testSearchWineList.getWineList().contains(testWine)) {  // If remove wine does not work. this test is redundant
            fail("Test wine was not removed from testSearchWineList");
        } else {
            assertFalse(testSearchWineList.getWineList().remove(testWine));
        }
    }
}
