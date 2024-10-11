package seng202.team3.services;

import seng202.team3.models.WineAttribute;
import seng202.team3.repository.SearchDAO;
import seng202.team3.repository.Table;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Service class for search screen
 * @author Yuvraj Fagotra (yfa50)
 */
public final class SearchScreenService {

    private static final SearchDAO searchDAO = new SearchDAO();

    /**
     * Gets all distinct values that occur in the given attribute (column) in the database
     *
     * @param attribute the name of the attribute (column) to get values from
     * @param table the name of the table whose attribute values are being selected
     * @return a list of strings of all distinct values in the given column in the wineSuper table
     */
    public static ArrayList<String> getAttributeValues(WineAttribute attribute, Table table) {

        ArrayList<String> values = new ArrayList<>();
        if (searchDAO.isValidAttribute(attribute.attributeName, table.tableName)) {
            values.addAll(searchDAO.getWineAttributeValues(attribute.attributeName, table.tableName));
        }
        Collections.sort(values);
        values.addFirst("All");

        return values;
    }

    /**
     * Gets the value of the aggregate function applied on the win attribute from the given table
     *
     * @param attribute the wine attribute to get the minimum value from
     * @param table the table the given wine attribute is a column of
     * @return the minimum value of the attribute in the table
     */
    public static float getBoundaryAttributeValue(WineAttribute attribute, Table table, String aggregateBoundary) {
        return searchDAO.getAggregateFunctionValue(attribute.attributeName, table.tableName, aggregateBoundary);
    }
}
