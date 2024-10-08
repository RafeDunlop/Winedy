package seng202.team3.repository;
import com.password4j.Hash;
import com.password4j.Password;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.exceptions.WineDrinkerAlreadyExistsException;
import seng202.team3.models.*;

import java.util.List;
import java.sql.*;


/**
 * Wine Drinker DAO class that handles all user related actions to the database
 *
 * @author Steven Leishman (sle159)
 */
public class WineDrinkerDAO implements DAOInterface<WineDrinker> {

    /**
     * Database manager instance to manage database connections
     */
    private final DatabaseManager database;

    /**
     * Logger for robust error logging
     */
    private static final Logger log = LogManager.getLogger(WineDrinkerDAO.class);

    /**
     *  Creates a WineDrinkerDAO object and gets a reference to the database singleton
     */
    public WineDrinkerDAO(String url){database = DatabaseManager.getInstance(url);}

    /**
     * TODO: implement for deliverable 3
     */
    @Override
    public List<WineDrinker> getAll() {
        throw new NotImplementedException("WineDrinkerDAO get all method not yet implemented");
    }



    /**
     * Gets a WineDrinker object from the database based on their username
     *
     * @param username unique username to identify a WineDrinker
     * @return the wine drinker that has been fetched from the database
     */

    public WineDrinker getWineDrinkerFromUsername(String username) {
        WineDrinker retrievedWineDrinker = null;
        String sqlQuery = "SELECT * FROM wineDrinker where username=?";
        try(Connection conn = database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, username);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    retrievedWineDrinker = new WineDrinker(
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("countryPreference"),
                            resultSet.getString("colourPreference"),
                            resultSet.getString("fullnessPreference"),
                            resultSet.getString("grapePreference"),
                            resultSet.getDouble("abvLimit"));
                }

            }
        } catch(SQLException e) {
            log.error("Error retrieving user from database", e);
        }

        return retrievedWineDrinker;
    }

    /**
     * Adds Wine Drinker to the database
     *
     * @param toAdd object of type T to add
     * @return the insert id associated with a wine drinker
     * @throws WineDrinkerAlreadyExistsException if method is called with a WineDrinker with a username that already exists
     */
    @Override
    public int add(WineDrinker toAdd) throws WineDrinkerAlreadyExistsException {
        String sqlQuery = "INSERT INTO wineDrinker(username, password, countryPreference, colourPreference, fullnessPreference, grapePreference, abvLimit) values (?,?,?,?,?,?,?);";
        Hash hash = Password.hash(toAdd.getPassword()).withBcrypt();
        String password = hash.getResult();
        try (Connection conn = database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, toAdd.getUsername());
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, toAdd.getCountryPreference());
            preparedStatement.setString(4, toAdd.getColourPreference());
            preparedStatement.setString(5, toAdd.getFullnessPreference());
            preparedStatement.setString(6, toAdd.getGrapePreference());
            preparedStatement.setDouble(7, toAdd.getAbvLimit());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            int insertId = -1;
            if(resultSet.next()){
                insertId = resultSet.getInt(1);
            }
            return insertId;
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                throw new WineDrinkerAlreadyExistsException("An account with this username already exists");
            }
            log.error("Error inserting user into database", e);
            return -1;
        }
    }


    /**
     * Delete object by ID
     *
     * @param toDelete WineDrinker object to be deleted
     */
    @Override
    public int delete(WineDrinker toDelete) {
        String sqlQuery = "DELETE from wineDrinker WHERE username=?";
        try(Connection conn = database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, toDelete.getUsername());
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            log.error("Error updating user in database", e);
            return 1;
        }

    }


    /**
     * Updates the Wine Drinker's details
     *
     * @param user User that has updated preferences and needs their data stored in the database to be documents
     */
    @Override
    public int update(WineDrinker user) {
        String sqlQuery = "UPDATE wineDrinker SET countryPreference=?, colourPreference=?, fullnessPreference=?, grapePreference=?, abvLimit=?  WHERE username=?";
        try(Connection conn = database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, user.getCountryPreference());
            preparedStatement.setString(2, user.getColourPreference());
            preparedStatement.setString(3, user.getFullnessPreference());
            preparedStatement.setString(4, user.getGrapePreference());
            preparedStatement.setDouble(5, user.getAbvLimit());
            preparedStatement.setString(6, user.getUsername());
            preparedStatement.executeUpdate();
            return 0;
        } catch (SQLException e) {
            log.error("Error updating user in database", e);
            return 1;
        }

    }
}
