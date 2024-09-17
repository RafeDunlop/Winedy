package seng202.team3.repository;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.exceptions.WineDrinkerAlreadyExistsException;
import seng202.team3.models.*;

import java.util.List;
import java.sql.*;


/**
 * Wine Drinker DAO class that handles all user related actions to the database
 */
public class WineDrinkerDAO implements DAOInterface<WineDrinker> {
    private final DatabaseManager database;
    private static final Logger log = LogManager.getLogger(WineDrinkerDAO.class);

    public WineDrinkerDAO(){database = DatabaseManager.getInstance();}

    /**
     * @return 
     */
    @Override
    public List<WineDrinker> getAll() {
        throw new NotImplementedException("WineDrinkerDAO get all method not yet implemented");
    }



    /**
     * Gets a WineDrinker object from the database based on their username
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
        } catch(SQLException sqlException) {
            log.error(sqlException);
            System.out.println("Exception here = " + sqlException);
        }

        return retrievedWineDrinker;
    }

    /**
     * @param toAdd object of type T to add 
     * @return
     * @throws WineDrinkerAlreadyExistsException
     */
    @Override
    public int add(WineDrinker toAdd) throws WineDrinkerAlreadyExistsException {
        String sqlQuery = "INSERT INTO wineDrinker(username, password, countryPreference, colourPreference, fullnessPreference, grapePreference) values (?,?,?,?,?,?);";
        try (Connection conn = database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, toAdd.getUsername());
            preparedStatement.setString(2, toAdd.getPassword());
            preparedStatement.setString(3, toAdd.getCountryPreference());
            preparedStatement.setString(4, toAdd.getColourPreference());
            preparedStatement.setString(5, toAdd.getFullnessPreference());
            preparedStatement.setString(6, toAdd.getGrapePreference());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            int insertId = -1;
            if(resultSet.next()){
                insertId = resultSet.getInt(1);
            }
            return insertId;
        } catch (SQLException sqlException) {
            System.out.println("Exception here = " + sqlException);
            if (sqlException.getErrorCode() == 19) {
                throw new WineDrinkerAlreadyExistsException("An account with this username already exists");
            }
            log.error(sqlException);
            return -1;
        }
    }

    /**
     * @param username username of record to delete
     */

    public void deleteByUsername(String username) {
        String sqlQuery = "DELETE FROM wineDrinker WHERE username=?";
        try(Connection conn = database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Exception here = " + sqlException);
        }
    }

    /**
     * Delete object by ID TODO this is redudant, but required by our DAO interface
     * @param id id of object to delete
     */
    @Override
    public void delete(int id ){}


    /**
     * @param user User that needs to be updated (this object must be able to identify itself and its previous self)
     */
    @Override
    public void update(WineDrinker user) {
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
        } catch (SQLException sqlException) {
            System.out.println("Exception here = " + sqlException);
        }

    }
}
