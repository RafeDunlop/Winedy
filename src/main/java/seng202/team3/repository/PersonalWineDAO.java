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

/**
 * PersonalWine DAO Class that handles all personal wine related actions to the database
 *
 * @author Hannah Botting (hbo51)
 * @author Krishna Sridhar (nsr36)
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
     * Variable to store WineDAO instance to access useful methods
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
     * @param url string which references the path to the database
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
        String sqlWine = "SELECT * FROM personalWine WHERE personalWine.wineDrinker=?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement ps = conn.prepareStatement(sqlWine)) {
            ps.setString(1, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    int wineId = resultSet.getInt("id");
                    Wine newWine = wineDAO.getWineByID(wineId);
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
        wineDAO.add(toAdd);
        String sqlPersonalWine = "INSERT OR IGNORE INTO personalWine (id, wineDrinker) values (?,?);";
        try (Connection conn = databaseManager.connect();
             PreparedStatement psPersonalWine = conn.prepareStatement(sqlPersonalWine)) {
            psPersonalWine.setInt(1, toAdd.getUniqueWineID());
            psPersonalWine.setString(2, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
            psPersonalWine.executeUpdate();
            ResultSet resultSet = psPersonalWine.getGeneratedKeys();
            return (resultSet.next()) ? resultSet.getInt(1) : -1;
        } catch (SQLException sqlException) {
            log.error(sqlException);
            return -1;
        }
    }

    /**
     * Deletes a Personal Wine object from database that matches id given
     *
     * @param toDelete Object to be deleted
     */
    @Override
    public int delete(Wine toDelete) {
        wineDAO.delete(toDelete);
        String sqlDelete = "DELETE FROM personalWine WHERE id = ? AND wineDrinker = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement psDelete = conn.prepareStatement(sqlDelete)) {
            psDelete.setInt(1, toDelete.getUniqueWineID());
            psDelete.setString(2, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
            return psDelete.executeUpdate();
        } catch (SQLException | NullPointerException e) {
            log.error(e);
            return -1;
        }
    }

    /**
     * Updates a Personal Wine object in the database
     *
     * @param toUpdate Object that needs to be updated (this object must be able to identify itself and its previous self)
     */
    @Override
    public int update(Wine toUpdate) {
        String sqlQuery = "UPDATE wineSuper SET name=?, country=?, colour=?, style=?, fullness=?, longDescription=?, pricePerBottle=?, alcoholByVolume=?, volumeInML=?, year=? WHERE id = ?";
        try (Connection conn = databaseManager.connect();
             PreparedStatement psQuery = conn.prepareStatement(sqlQuery)) {
            psQuery.setString(1, toUpdate.getName());
            psQuery.setString(2, toUpdate.getCountry());
            psQuery.setString(3, toUpdate.getColour());
            psQuery.setString(4, toUpdate.getStyle());
            psQuery.setString(5, toUpdate.getFullness());
            psQuery.setString(6, toUpdate.getLongDescription());
            psQuery.setFloat(7, toUpdate.getPricePerBottle());
            psQuery.setFloat(8, toUpdate.getAlcoholByVolume());
            psQuery.setFloat(9, toUpdate.getVolumeInMl());
            psQuery.setInt(10, toUpdate.getYear());
            psQuery.setInt(11, toUpdate.getUniqueWineID());
            psQuery.executeUpdate();
            return 0;
        } catch (SQLException | NullPointerException e) {
            log.error(e);
            return 1;
        }
    }
}
