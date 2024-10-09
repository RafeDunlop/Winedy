package seng202.team3.unittests.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng202.team3.models.SearchWineList;
import seng202.team3.models.Wine;
import seng202.team3.models.WineList;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WineList
 * @author Rafe Dunlop (rdu46)
 */

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
        toTest = new SearchWineList(Collections.emptyList(), 0, 0, 0f, 0f,
                null, null, null, null);
    }

    @Test
    public void testRemoveNullFromList() {
        assertThrows(NullPointerException.class, () -> toTest.removeWineFromList(null));
    }

    @Test
    public void testAddNullToList() {
        assertThrows(NullPointerException.class, () -> toTest.addWineToList(null));
    }

    @Test
    public void testRemoveNonExistingWineFromList() {
        assertFalse(toTest.removeWineFromList(toAdd));
    }

    @Test
    public void testRemoveExistingWineFromList() {
        toTest.addWineToList(toAdd);
        assertTrue(toTest.removeWineFromList(toAdd));
    }

    @Test
    public void testAddWineToList() {
        toTest.addWineToList(toAdd);
        assertEquals(toAdd, toTest.getWineList().getFirst());
    }
}
