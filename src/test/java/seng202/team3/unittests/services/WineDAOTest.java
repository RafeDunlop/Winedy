package seng202.team3.unittests.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team3.models.Wine;
import seng202.team3.services.DatabaseManager;
import seng202.team3.services.WineDAO;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WineDAOTest {
    private WineDAO wineDAO = new WineDAO();
    private DatabaseManager databaseManager;

    @BeforeEach
    public void setup() {
        String databasePath = "./build/classes/java/database.db";
        File databaseFile = new File(databasePath);
        if (databaseFile.exists()) {
            if (databaseFile.delete()) {
                System.out.println("Database file deleted successfully.");
            } else {
                System.out.println("Failed to delete the database file.");
            }
        }
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.getInstance();
        databaseManager.resetDB();
    }

    @AfterEach
    public void removeInstance() {
        DatabaseManager.REMOVE_INSTANCE();
    }

    @Test
    void testAdd() {
        Wine wine = new Wine(
                783,
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
        int insertId = wineDAO.add(wine);
        assertEquals(insertId, 783);
    }

}
