package seng202.team3.unittests.models;

import org.junit.jupiter.api.Test;
import seng202.team3.models.Wine;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Wine model class
 * @author Krishna Sridhar
 */

public class WineTest {
    private Wine wine = new Wine(
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
        assertEquals(wine.getUniqueWineID(), 1);
    }

    @Test
    public void testGetName() {
        assertEquals(wine.getName(), "Nero Oro Appassimento 2018, Sicily");
    }

    @Test
    public void testGetCountry() {
        assertEquals(wine.getCountry(), "Italy");
    }

    @Test
    public void testGetType() {
        assertEquals(wine.getType(), "Red");
    }

    @Test
    public void testGetStyle() {
        assertEquals(wine.getStyle(), "Big");
    }

    @Test
    public void testGetGrapes() {
        assertArrayEquals(wine.getGrapes(), new String[]{"Nero d'Avola"});
    }

    @Test
    public void testGetFullness() {
        assertEquals(wine.getFullness(), "FULL");
    }
    @Test
    public void testGetLongDescription() {
        assertEquals(wine.getLongDescription(), "Nero Oro is made by a winemaker who's scored a perfect 100 Parker Points.");
    }
    @Test
    public void testGetPricePerBottle() {
        assertEquals(wine.getPricePerBottle(), (float) 9.99);
    }
    @Test
    public void testGetAwards() {
        assertArrayEquals(wine.getAwards(), new String[]{"IWC 2019 - Commended Award", "Decanter 2019 - Bronze Award"});
    }
    @Test
    public void testGetAlcoholByVolume() {
        assertEquals(wine.getAlcoholByVolume(), (float) 14);
    }

    @Test
    public void testGetVolumeInML() {
        assertEquals(wine.getVolumeInMl(), 75);
    }

    @Test
    public void testGetYear() {
        assertEquals(wine.getYear(), 2018);
    }

    @Test
    public void testSetUniqueWineID() {
        wine.setUniqueWineID(4);
        assertEquals(wine.getUniqueWineID(), 4);
    }

    @Test
    public void testSetName() {
        wine.setName("Definition Zinfandel 2017, Lodi");
        assertEquals(wine.getName(), "Definition Zinfandel 2017, Lodi");
    }

    @Test
    public void testSetCountry() {
        wine.setCountry("USA");
        assertEquals(wine.getCountry(), "USA");
    }

    @Test
    public void testSetType() {
        wine.setType("White");
        assertEquals(wine.getType(), "White");
    }

    @Test
    public void testSetStyle() {
        wine.setStyle("Fruity");
        assertEquals(wine.getStyle(), "Fruity");
    }

    @Test
    public void testSetFullness() {
        wine.setFullness("DRY");
        assertEquals(wine.getFullness(), "DRY");
    }
    @Test
    public void testSetLongDescription() {
        wine.setLongDescription("The Definition range captures the quintessential qualities of the world's greatest wine styles.");
        assertEquals(wine.getLongDescription(), "The Definition range captures the quintessential qualities of the world's greatest wine styles.");
    }
    @Test
    public void testSetPricePerBottle() {
        wine.setPricePerBottle((float) 14.99);
        assertEquals(wine.getPricePerBottle(), (float) 14.99);
    }
    @Test
    public void testSetAwards() {
        wine.setAwards(new String[]{"IWC 2019 - Bronze Award", "IWC 2018 - Commended Award"});
        assertArrayEquals(wine.getAwards(), new String[]{"IWC 2019 - Bronze Award", "IWC 2018 - Commended Award"});
    }
    @Test
    public void testSetAlcoholByVolume() {
        wine.setAlcoholByVolume((float) 13);
        assertEquals(wine.getAlcoholByVolume(), (float) 13);
    }

    @Test
    public void testSetVolumeInML() {
        wine.setVolumeInMl(80);
        assertEquals(wine.getVolumeInMl(), 80);
    }

    @Test
    public void testSetYear() {
        wine.setYear(2017);
        assertEquals(wine.getYear(), 2017);
    }

}
