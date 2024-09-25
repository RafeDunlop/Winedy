package seng202.team3.unittests.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;
import seng202.team3.models.WineList;

import static org.junit.jupiter.api.Assertions.*;

public class WineListTest {

    private static WineList toTest;

    private static Wine toAdd;

    @BeforeAll
    public static void setup() {
        toAdd = new Wine(
                0,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                null,
                0,
                0,
                0);
    }

    @BeforeEach
    public void resetList() {
        toTest = new SearchWineList(); // used to test concrete methods in abstract class
    }

    @Test
    public void testRemoveNull() {
        assertThrows(NullPointerException.class, () -> toTest.removeWineFromList(null));
    }

    @Test
    public void testAddNull() {
        assertThrows(NullPointerException.class, () -> toTest.addWineToList(null));
    }

    @Test
    public void testRemoveNotInList() {
        assertFalse(toTest.removeWineFromList(toAdd));
    }

    @Test
    public void testRemoveInList() {
        toTest.addWineToList(toAdd);
        assertTrue(toTest.removeWineFromList(toAdd));
    }

    @Test
    public void testAdd() {
        toTest.addWineToList(toAdd);
        assertEquals(toAdd, toTest.getWineList().getFirst());
    }
}
