package seng202.team3.unittests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seng202.team3.services.DatabaseManager;
import java.io.File;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DatabaseManager service class
 * @author Krishna Sridhar
 */

public class DatabaseManagerTest {
    private DatabaseManager databaseManager;
    private final String DATABASE_PATH = "./test/resources/database.db";

    @BeforeEach
    public void setup() {
        File databaseFile = new File(DATABASE_PATH);
        if (databaseFile.exists()) {
            if (databaseFile.delete()) {
                System.out.println("Existing database file deleted successfully.");
            } else {
                System.out.println("Failed to delete the existing database file.");
            }
        }
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.getInstance();
    }

    @AfterEach
    public void removeInstance() {
        DatabaseManager.REMOVE_INSTANCE();
    }

    @Test
    public void testSingletonInstance() {
        DatabaseManager instance1 = DatabaseManager.getInstance();
        DatabaseManager instance2 = DatabaseManager.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testConnect() {
        Connection conn = databaseManager.connect();
        try {
            assertFalse(conn.isClosed());
            conn.close();
        }
        catch (SQLException e) {
            fail("SQLException thrown while making connection: " + e.getMessage());
        }
    }
}
