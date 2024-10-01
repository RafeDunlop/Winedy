package seng202.team3.services;

import seng202.team3.models.WineAttribute;
import seng202.team3.repository.SearchDAO;
import seng202.team3.repository.Table;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Service class for search screen
 * @author Yuvraj (yfa50)
 */
public class SearchScreenService {

    private final SearchDAO searchDAO;

    public SearchScreenService() {
        searchDAO = new SearchDAO();
    }

    /**
     * Gets all distinct values that occur in the given attribute (column) in the database
     * @param attribute the name of the attribute (column) to get values from
     * @return a list of strings of all distinct values in the given column in the wineSuper table
     */
    public ArrayList<String> getAttributeValues(WineAttribute attribute, Table table) {

        ArrayList<String> values = new ArrayList<>();
        values.add("");
        if (searchDAO.isValidAttribute(attribute.attributeName, table.tableName)) {
            values.addAll(searchDAO.getWineAttributeValues(attribute.attributeName, table.tableName));
        }
        Collections.sort(values);

        return values;
    }

}
