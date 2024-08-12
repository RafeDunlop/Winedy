package seng202.team3.services;
import seng202.team3.models.*;
import java.util.*;

/**
 *This class represents the service which requests data from the database
 * and supplies it back to the models
 *
 * @author Steven Leishman
 */
public class DBService {


    /**
     * searches DB for users record under username
     * returns list of strings of users saved data
     * or if no data exists throw exception
     *
     * @param username - users username to search
     * @param password
     * @return List of Strings of user data
     * @throws WineDrinkerDoesNotExistException
     */
    public List<String>  readDrinkerDetails(String username, String password) throws WineDrinkerDoesNotExistException{
        boolean mockFlag = false;
        if (mockFlag) {
            throw new WineDrinkerDoesNotExistException("Wine Drinker does not exist");
        }

        return new ArrayList<>();
    }

    /**
     * writes a wineDrinker record to the database from input
     * wineDrinker instance
     * @param wineDrinker
     */
    public void writeDrinkerDetails(WineDrinker wineDrinker){
        //to implement when SQL learned
    }

    /**
     * Creates a new wineDrinker record with input username and password
     * utilises writeDrinkerDetails
     * @param username
     * @param password
     */
    public void writeNewDrinkerRecord(String username, String password){
        System.out.println("Wine Drinker record created");
        //writeDrinkerDetails();
    }

    /**
     * Adds inputted wineList to db
     * @param wineList
     */
    public void writeWineList(List<Wine> wineList){
        //to implement when SQL learned
    }
}
