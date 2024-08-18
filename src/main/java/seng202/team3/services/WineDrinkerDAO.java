package seng202.team3.services;
import seng202.team3.models.*;

import java.util.List;
import java.sql.*;

public class WineDrinkerDAO implements DAOInterface<WineDrinker> {
    private final DatabaseManager database;

    public WineDrinkerDAO(){database = DatabaseManager.getInstance();}
    /**
     * @return 
     */
    @Override
    public List<WineDrinker> getAll() {
        return null;
    }



    public WineDrinker getUserByLoginDetails(String username, String password) {
        WineDrinker retrievedWineDrinker = null;
        String sqlQuery = "SELECT * FROM wineDrinker where username=?, password=?";
        try(Connection conn = database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try(ResultSet resultset = preparedStatement.executeQuery()){
                while (resultset.next()){
                    retrievedWineDrinker = new WineDrinker(
                            resultset.getInt("id"),
                            resultset.getString("username"),
                            resultset.getString("password"),
                            resultset.getString("wineColourPreference"),
                            resultset.getString("wineFullnessPreference"));
                }
                return retrievedWineDrinker;
            }
        } catch(SQLException sqlException) {
            System.out.println("Exception here = " + sqlException);
            return retrievedWineDrinker;
        }
    }

    /**
     * @param id id of object to get
     * @return
     */
    @Override
    public WineDrinker getOne(int id) {
        WineDrinker retrievedWineDrinker = null;
        String sqlQuery = "SELECT * FROM wineDrinker where id=?";
        try(Connection conn = database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery(sqlQuery)){
                while(resultSet.next()) {
                    retrievedWineDrinker = new WineDrinker(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("wineColourPreference"),
                            resultSet.getString("wineFullnessPreference"));

                }
                return retrievedWineDrinker;
            }
        } catch (SQLException sqlException) {
            System.out.println("Exception here = " + sqlException);
            return null;
        }
    }

    /**
     * @param toAdd object of type T to add 
     * @return
     * @throws DuplicateEntryException
     */
    @Override
    public int add(WineDrinker toAdd) throws DuplicateEntryException {
        String sqlQuery = "INSERT INTO wineDrinker(id, username, password, wineColourPreference, wineFullnessPreference) values (?,?,?,?,?,?);";
        try (Connection conn = database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, toAdd.getDatabaseID());
            preparedStatement.setString(2, toAdd.getUsername());
            preparedStatement.setString(3, toAdd.getPassword());
            preparedStatement.setString(4, toAdd.getWineColourPreference());
            preparedStatement.setString(5, toAdd.getWineFullnessPreference());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            int insertId = -1;
            if(resultSet.next()){
                insertId = resultSet.getInt(1);
            }
            return insertId;
        } catch (SQLException sqlException) {
            System.out.println("Exception here = " + sqlException);
            return -1;
        }
    }

    /**
     * @param id id of object to delete 
     */
    @Override
    public void delete(int id) {
        String sqlQuery = "DELETE FROM wineDrinker WHERE id=?";
        try(Connection conn = database.connect();
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println("Exception here = " + sqlException);
        }
    }

    /**
     * @param toUpdate Object that needs to be updated (this object must be able to identify itself and its previous self) 
     */
    @Override
    public void update(WineDrinker toUpdate) {
//        String sqlQuery = "UPDATE wineDrinker SET wineColourPreference = ?  WHERE id=?";
//        try(Connection conn = database.connect();
//            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
//            preparedStatement.setInt(1, id);
//            preparedStatement.executeUpdate();
//        } catch (SQLException sqlException) {
//            System.out.println("Exception here = " + sqlException);
//        }
//
    }
}
