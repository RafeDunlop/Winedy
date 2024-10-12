package seng202.team3.services;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import seng202.team3.models.WineAttribute;
import seng202.team3.repository.SearchDAO;
import seng202.team3.repository.Table;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Service class for search screen
 *
 * @author Yuvraj (yfa50)
 */
public final class SearchScreenService {

    /**
     * Instance of a search DAO for database related actions
     */
    private static final SearchDAO searchDAO = new SearchDAO();

    /**
     * A string converter for converting numbers to and from their string and number representation
     */
    public static final StringConverter<Number> converter = new StringConverter<>() {
        @Override
        public String toString(Number number) {
            return String.valueOf(number.intValue());
        }
        @Override
        public Number fromString(String s) {
            try {
                return Integer.parseInt(s);
            }
            catch (NumberFormatException e) {
                return null;
            }
        }
    };

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
     * Gets the value of the aggregate function applied on the wine attribute from the given table
     *
     * @param attribute the wine attribute to get the minimum value from
     * @param table the table the given wine attribute is a column of
     * @return the minimum value of the attribute in the table
     */
    public static float getBoundaryAttributeValue(WineAttribute attribute, Table table, String aggregateBoundary) {
        return searchDAO.getAggregateFunctionValue(attribute.attributeName, table.tableName, aggregateBoundary);
    }

    /**
     * Returns a text formatter for the minimum and maximum price value text fields on the search screen. This text
     * formatter prevents the input of the text fields from being non-numeric and from going outside the minimum and
     * maximum price values.
     *
     * @param minPrice the minimum price of the wines
     * @param maxPrice the maximum price of the wines
     * @return a String text formatter for validating the input of the price text fields on the search screen
     */
    public static TextFormatter<String> getMinMaxPriceTextFormatter(int minPrice, int maxPrice) {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (!newText.matches("\\d*")) return null;
            if (newText.isEmpty()) return change;
            double newValue = Double.parseDouble(newText);
            if (newValue < minPrice || newValue > maxPrice) return null;
            return change;
        });
    }
}
