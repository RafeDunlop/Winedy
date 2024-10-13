package seng202.team3.unittests.models;

import org.junit.jupiter.api.Test;
import seng202.team3.models.Wine;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Wine model class
 * @author Krishna Sridhar (nsr36)
 */

public class WineTest {
    private final Wine wine = new Wine(
            1,
            "Nero Oro Appassimento 2018, Sicily",
            "Italy",
            "Red",
            "Big",
            new String[]{"Nero d'Avola"},
            "FULL",
            "Nero Oro is made by a winemaker who's scored a perfect 100 Parker Points.",
            (float) 9.99,
            new String[]{"IWC 2019 - Commended Award", "Decanter 2019 - Bronze Award"},
            (float) 14,
            75,
            2018);

    @Test
    public void testGetUniqueWineID() {
        assertEquals(1, wine.getUniqueWineID());
    }

    @Test
    public void testGetName() {
        assertEquals("Nero Oro Appassimento 2018, Sicily", wine.getName());
    }

    @Test
    public void testGetCountry() {
        assertEquals("Italy", wine.getCountry());
    }

    @Test
    public void testGetStyle() {
        assertEquals("Big", wine.getStyle());
    }

    @Test
    public void testGetGrapes() {
        assertArrayEquals(new String[]{"Nero d'Avola"}, wine.getGrapes());
    }

    @Test
    public void testGetFullness() {
        assertEquals("FULL", wine.getFullness());
    }
    @Test
    public void testGetLongDescription() {
        assertEquals("Nero Oro is made by a winemaker who's scored a perfect 100 Parker Points.", wine.getLongDescription());
    }
    @Test
    public void testGetPricePerBottle() {
        assertEquals(9.99f, wine.getPricePerBottle());
    }
    @Test
    public void testGetAwards() {
        assertArrayEquals(new String[]{"IWC 2019 - Commended Award", "Decanter 2019 - Bronze Award"}, wine.getAwards());
    }
    @Test
    public void testGetAlcoholByVolume() {
        assertEquals(14f, wine.getAlcoholByVolume());
    }

    @Test
    public void testGetVolumeInML() {
        assertEquals(75, wine.getVolumeInMl());
    }

    @Test
    public void testGetYear() {
        assertEquals(2018, wine.getYear());
    }

    @Test
    public void testSetUniqueWineID() {
        wine.setUniqueWineID(4);
        assertEquals(4, wine.getUniqueWineID());
    }

    @Test
    public void testSetName() {
        wine.setName("Definition Zinfandel 2017, Lodi");
        assertEquals("Definition Zinfandel 2017, Lodi", wine.getName());
    }

    @Test
    public void testSetCountry() {
        wine.setCountry("USA");
        assertEquals("USA", wine.getCountry());
    }

    @Test
    public void testSetStyle() {
        wine.setStyle("Fruity");
        assertEquals("Fruity", wine.getStyle());
    }

    @Test
    public void testSetFullness() {
        wine.setFullness("DRY");
        assertEquals("DRY", wine.getFullness());
    }
    @Test
    public void testSetLongDescription() {
        wine.setLongDescription("The Definition range captures the quintessential qualities of the world's greatest wine styles.");
        assertEquals("The Definition range captures the quintessential qualities of the world's greatest wine styles.", wine.getLongDescription());
    }
    @Test
    public void testSetPricePerBottle() {
        wine.setPricePerBottle(14.99f);
        assertEquals(14.99f, wine.getPricePerBottle());
    }
    @Test
    public void testSetAwards() {
        wine.setAwards(new String[]{"IWC 2019 - Bronze Award", "IWC 2018 - Commended Award"});
        assertArrayEquals(new String[]{"IWC 2019 - Bronze Award", "IWC 2018 - Commended Award"}, wine.getAwards());
    }
    @Test
    public void testSetAlcoholByVolume() {
        wine.setAlcoholByVolume(13f);
        assertEquals(13f, wine.getAlcoholByVolume());
    }

    @Test
    public void testSetVolumeInML() {
        wine.setVolumeInMl(80);
        assertEquals(80, wine.getVolumeInMl());
    }

    @Test
    public void testSetYear() {
        wine.setYear(2017);
        assertEquals(2017, wine.getYear());
    }

}
