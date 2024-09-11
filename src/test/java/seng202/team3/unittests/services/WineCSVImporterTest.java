package seng202.team3.unittests.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import seng202.team3.models.Wine;
import seng202.team3.services.WineCSVImporter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WineCSVImporterTest {

    private static Wine wineFromMethod;
    private static Wine testWine;

    @BeforeAll
    public static void initTestData() {
        String[] wineString = new String[]{
                "Jacob's Creek Swill",
                "New Zealand",
                "Red",
                "Big",
                "Merlot, Cabernet Sauvignon",
                "DRY",
                "A terribly poor vintage, made up only by it's being exceedingly economical",
                "7.29",
                "NZ's most iconic wine for broke people, cheap+ Gold, Best wine to substitute vinegar",
                "13.9",
                "75",
                "111",
                "2023"
        };
        wineFromMethod = WineCSVImporter.readWineFromLine(wineString);
        testWine = new Wine(
                111,
                "Jacob's Creek Swill",
                "New Zealand",
                "Red",
                "Big",
                new String[]{"Merlot", "Cabernet Sauvignon"},
                "DRY",
                "A terribly poor vintage, made up only by it's being exceedingly economical",
                (float) 7.29,
                new String[]{"NZ's most iconic wine for broke people", "cheap+ Gold", "Best wine to substitute vinegar"},
                (float) 13.9,
                (float) 750.0,
                2023
        );
    }

    @Test
    public void testWineName() {
        assertEquals(testWine.getName(), wineFromMethod.getName());
    }

    @Test
    public void testWineCountry() {
        assertEquals(testWine.getCountry(), wineFromMethod.getCountry());
    }

/*    @Test
    public void testWineColour() {
        assertEquals(testWine.getColour(), wineFromMethod.getColour());
    }*/

    @Test
    public void testWineStyle() {
        assertEquals(testWine.getStyle(), wineFromMethod.getStyle());
    }

    @Test
    public void testWineGrapes() {
        assertArrayEquals(testWine.getGrapes(), wineFromMethod.getGrapes());
    }

    @Test
    public void testWineFullness() {
        assertEquals(testWine.getFullness(), wineFromMethod.getFullness());
    }

    @Test
    public void testWineLongDescription() {
        assertEquals(testWine.getLongDescription(), wineFromMethod.getLongDescription());
    }

    @Test
    public void testWinePrice() {
        assertEquals(testWine.getPricePerBottle(), wineFromMethod.getPricePerBottle());
    }

    @Test
    public void testWineAwards() {
        assertArrayEquals(testWine.getAwards(), wineFromMethod.getAwards());
    }

    @Test
    public void testWineABV() {
        assertEquals(testWine.getAlcoholByVolume(), wineFromMethod.getAlcoholByVolume());
    }

    @Test
    public void testWineVolume() {
        assertEquals(testWine.getVolumeInMl(), wineFromMethod.getVolumeInMl());
    }

    @Test
    public void testWineYear() {
        assertEquals(testWine.getYear(), wineFromMethod.getYear());
    }
}
