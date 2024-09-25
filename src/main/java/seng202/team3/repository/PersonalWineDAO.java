package seng202.team3.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.exceptions.WineDrinkerAlreadyExistsException;
import seng202.team3.models.Wine;
import seng202.team3.services.WineDrinkerManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * PersonalWine DAO Class that handles all personal wine related actions to the database
 *
 * @author Hannah Botting (hbo51)
 */
public class PersonalWineDAO implements DAOInterface<Wine> {

    /**
     * Logger for robust error logging
     */
    private static final Logger log = LogManager.getLogger(WineDAO.class);

    /**
     * Variable to store DatabaseManager instance to manage database connections
     */
    private final DatabaseManager databaseManager;

    /**
     * Variable to store WineDAO instance to ______________
     */
    private final WineDAO wineDAO;

    /**
     * Creates a new PersonalWineDAO object and gets a reference to the database singleton
     */
    public PersonalWineDAO() {
        wineDAO = new WineDAO();
        databaseManager = DatabaseManager.getInstance();
    }

    /**
     * PersonalWineDAO constructor with ability to pass in a database url
     */
    public PersonalWineDAO(String url) {
        wineDAO = new WineDAO(url);
        databaseManager = DatabaseManager.getInstance(url);
    }

    /**
     * Gets all Personal Wines from the database of the currently logged-in user
     *
     * @return List of all objects type Wine from the database
     */
    @Override
    public List<Wine> getAll() {
        List<Wine> wines = new ArrayList<>();
        String sqlWine = "SELECT * FROM personalWine WHERE wineDrinker=?";
        String currentWineDrinker = WineDrinkerManager.getInstance().getCurrentUser().getUsername();
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sqlWine)) {
            ps.setString(1, currentWineDrinker);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    Wine newWine = wineDAO.getWineByID(id);
                    wines.add(newWine);
                }
                return wines;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return new ArrayList<>();
        }
    }

    /**
     * Adds a single object of type Personal Wine to database
     *
     * @param toAdd object of type Wine to add
     * @return object insert id if inserted correctly
     * @throws WineDrinkerAlreadyExistsException if method is called with a WineDrinker with a username that already exists
     */
    @Override
    public int add(Wine toAdd) throws WineDrinkerAlreadyExistsException {
        return 0;
    }

    /**
     * Deletes a Personal Wine object from database that matches id given
     *
     * @param toDelete Object to be deleted
     */
    @Override
    public int delete(Wine toDelete) {
        return 0;
    }

    /**
     * Updates a Personal Wine object in the database
     *
     * @param toUpdate Object that needs to be updated (this object must be able to identify itself and its previous self)
     */
    @Override
    public int update(Wine toUpdate) {
        return 0;
    }
}
