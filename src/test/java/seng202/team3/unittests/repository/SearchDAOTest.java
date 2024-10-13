package seng202.team3.unittests.repository;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team3.models.WineAttribute;
import seng202.team3.repository.DatabaseManager;
import seng202.team3.repository.SearchDAO;
import seng202.team3.repository.Table;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SearchDAO repository class
 * @author Krishna Sridhar (nsr36)
 */
public class SearchDAOTest {
    final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";
    private SearchDAO searchDAO;

    @BeforeAll
    public static void deleteTestDB() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

    @BeforeEach
    public void setup() {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager.getInstance(DATABASE_PATH);
        searchDAO = new SearchDAO(DATABASE_PATH);
    }

    @AfterEach
    public void cleanUp() {
        File file = new File(DATABASE_PATH);
        file.delete();
    }

    @Test
    public void testGetWineAttributeValues() {
        List<String> values = new ArrayList<>();
        values.addAll(searchDAO.getWineAttributeValues(WineAttribute.FULLNESS.attributeName, Table.WINESUPER.tableName));
        assertEquals(5, values.size());
    }

    @Test
    public void testGetWineAttributeValuesInvalidAttribute() {
        List<String> values = new ArrayList<>();
        values.addAll(searchDAO.getWineAttributeValues("cool attribute", Table.WINESUPER.tableName));
        assertEquals(0, values.size());
    }

    @Test
    public void testGetWineAttributeValuesInvalidTable() {
        List<String> values = new ArrayList<>();
        values.addAll(searchDAO.getWineAttributeValues(WineAttribute.FULLNESS.attributeName, "cool table"));
        assertEquals(0, values.size());
    }

    @Test
    public void testIsValidAttribute() {
        assertTrue(searchDAO.isValidAttribute(WineAttribute.COLOUR.attributeName, Table.WINESUPER.tableName));
    }

    @Test
    public void testIsValidAttributeInvalidAttribute() {
        assertFalse(searchDAO.isValidAttribute("even cooler attribute", Table.WINESUPER.tableName));
    }

    @Test
    public void testIsValidAttributeInvalidTable() {
        assertFalse(searchDAO.isValidAttribute(WineAttribute.ABV.attributeName, "super cool table"));
    }

    @Test
    public void testGetAggregateFunctionValue() {
        assertEquals(2019, (int) searchDAO.getAggregateFunctionValue(WineAttribute.YEAR.attributeName, Table.WINESUPER.tableName, "max"));
    }
}
