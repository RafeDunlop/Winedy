package seng202.team3.unittests;


import org.junit.jupiter.api.*;
import seng202.team3.repository.DatabaseManager;
import java.io.File;
import java.lang.reflect.Field;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DatabaseManager service class
 * @author Krishna Sridhar
 */

public class DatabaseManagerTest {
    private DatabaseManager databaseManager;
    private final String DATABASE_PATH = "jdbc:sqlite:./src/test/resources/test_database.db";

    @BeforeAll
    public static void deleteTestDB() {
        File file = new File("./src/test/resources/test_database.db");
        file.delete();
    }

    @BeforeEach
    public void setup() {
        DatabaseManager.REMOVE_INSTANCE();
        databaseManager = DatabaseManager.getInstance(DATABASE_PATH);
    }

    @AfterEach
    public void cleanup() {
        File file = new File(DATABASE_PATH.substring(12));
        file.delete();
    }

    @Test
    public void testDatabaseInstanceWithUrl() {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager dbManager = DatabaseManager.getInstance(DATABASE_PATH);
        Assertions.assertEquals(dbManager, DatabaseManager.getInstance());
    }

    @Test
    public void testDatabaseInstanceWithoutUrl() {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager dbManager = DatabaseManager.getInstance();
        Assertions.assertEquals(dbManager, DatabaseManager.getInstance());
    }

    @Test
    public void testSingletonInstance() {
        DatabaseManager instance = DatabaseManager.getInstance();
        assertSame(instance, databaseManager);
    }

    @Test
    public void testConnect() throws SQLException {
        Connection conn = databaseManager.connect();
        Assertions.assertNotNull(conn);
        Assertions.assertEquals(conn.getMetaData().getURL(), DATABASE_PATH);
        conn.close();
    }

    @Test
    public void testRemoveInstance() throws NoSuchFieldException, IllegalAccessException {
        DatabaseManager.REMOVE_INSTANCE();
        Field instance = databaseManager.getClass().getDeclaredField("instance");
        instance.setAccessible(true);
        Assertions.assertNull(instance.get(databaseManager));
    }

    @Test
    public void testConnectionWithInvalidPath() {
        DatabaseManager.REMOVE_INSTANCE();
        DatabaseManager dbManager = DatabaseManager.getInstance("jdbc:sqlite:InvalidFolder/Invalid.db");
        Connection conn = dbManager.connect();
        Assertions.assertNull(conn);
    }
}
