package seng202.team3.services;
import seng202.team3.models.*;
import java.util.*;
public class DBService {

    public List<String>  readDrinkerDetails(String username, String password) throws WineDrinkerDoesNotExistException{
        boolean mockFlag = false;
        if (mockFlag) {
            throw new WineDrinkerDoesNotExistException("Wine Drinker does not exist");
        }

        return new ArrayList<>();
    }

    public void writeDrinkerDetails(WineDrinker wineDrinker){
        //to implement
    }
    public void writeNewDrinkerRecord(String username, String password){
        System.out.println("Wine Drinker record created");
    }
    public void writeWineList(List<Wine> wineList){
        //to implement
    }
}
