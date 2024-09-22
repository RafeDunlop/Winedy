package seng202.team3.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import seng202.team3.models.UserWineList;
import seng202.team3.models.Wine;
import seng202.team3.services.WineDrinkerManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Hannah Botting (hbo51)
 */
public class UserWineListDAO implements DAOInterface<UserWineList> {

    /**
     * Logger for robust error logging
     */
    private static final Logger log = LogManager.getLogger(UserWineListDAO.class);

    /**
     * Retrieves and returns all the wine lists of the current logged-in Wine Drinker
     *
     * @return A list of all the current logged-in Wine Drinker's wine lists
     */
    @Override
    public List<UserWineList> getAll() {
        ArrayList<UserWineList> userWineLists = new ArrayList<>();
        String sql = "SELECT * FROM wineList WHERE wineList.username = ?";
        try (Connection conn = DatabaseManager.getInstance().connect();
             PreparedStatement psList = conn.prepareStatement(sql)) {
            psList.setString(1, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
            ResultSet resultSet = psList.executeQuery();
            while (resultSet.next()) {
                String description = resultSet.getString("description");
                String name = resultSet.getString("name");
                UserWineList userWineList = new UserWineList(name, description);
                getContainedWines(userWineList);
                userWineLists.add(userWineList);
            }
            return userWineLists;
        } catch (SQLException | NullPointerException e) {
            log.error(e);
        }
        return Collections.emptyList();
    }

    /**
     * Adds a new UserWineList to the database
     *
     * @param toAdd object of type UserWineList to add to the database
     * @return an int representing the success of the INSERT statements
     */
    @Override
    public int add(UserWineList toAdd) {
        String sqlList = "INSERT INTO wineList (name, username, description) VALUES (?, ?, ?)";
        String sqlContains = "INSERT INTO contains (wineID, listName, wineDrinker) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().connect();
             PreparedStatement psList = conn.prepareStatement(sqlList);
             PreparedStatement psContains = conn.prepareStatement(sqlContains)) {
            setListParams(psList, toAdd);
            psList.executeUpdate();
            for (Wine wine : toAdd.getWineList()) {
                setContainsParams(psContains, wine, toAdd.getWineListName());
                psContains.executeUpdate();
            }
            return 0;
        } catch (SQLException e) {
            log.error(e);
            return 1;
        }
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public void update(UserWineList toUpdate) {
        String sql = "UPDATE wineList SET description = ? WHERE name = ? AND username = ?";
        try (Connection conn = DatabaseManager.getInstance().connect();
             PreparedStatement psUpdate = conn.prepareStatement(sql)) {
            setUpdateParams(psUpdate, toUpdate);
        } catch (SQLException e) {
            log.error(e);
        }
    }

    public void rename(UserWineList toRename, String newName) {
        String sql = "INSERT INTO wineList (name, username, description) VALUES (?, ?, ?)";
    }

    private void setListParams(PreparedStatement ps, UserWineList toAdd) throws SQLException {
        ps.setString(1, toAdd.getWineListName());
        ps.setString(2, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
        ps.setString(3, toAdd.getDescription());
    }

    private void setUpdateParams(PreparedStatement ps, UserWineList toUpdate) throws SQLException {
        ps.setString(1, toUpdate.getDescription());
        ps.setString(2, toUpdate.getWineListName());
        ps.setString(3, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
    }

    private void getContainedWines(UserWineList userWineList) {
        String sqlContains = "SELECT * FROM contains JOIN wineSuper ON contains.wineId = wineSuper.id WHERE contains.wineDrinker = ? AND contains.listName = ?";
        try (Connection conn = DatabaseManager.getInstance().connect();
             PreparedStatement psContains = conn.prepareStatement(sqlContains)) {
            psContains.setString(1, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
            psContains.setString(2, userWineList.getWineListName());
            ResultSet resultSet = psContains.executeQuery();
            WineDAO wineDAO = new WineDAO();
            Wine containedWine;
            while (resultSet.next()) {
                String[] grapeList = wineDAO.getGrapesByID(resultSet.getInt("wineId"));
                String[] awardList = wineDAO.getAwardsByID(resultSet.getInt("wineId"));
                containedWine = wineDAO.getWineFromResultSet(resultSet, grapeList, awardList);
                userWineList.addWineToList(containedWine);
            }
        } catch (SQLException e) {
            log.error(e);
        }
    }

    private void setContainsParams(PreparedStatement ps, Wine wine, String wineListName) throws SQLException {
        ps.setInt(1, wine.getUniqueWineID());
        ps.setString(2, wineListName);
        ps.setString(3, WineDrinkerManager.getInstance().getCurrentUser().getUsername());
    }
}
