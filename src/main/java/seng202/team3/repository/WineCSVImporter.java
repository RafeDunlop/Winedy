package seng202.team3.repository;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team3.models.Wine;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WineCSVImporter{

    private static final Logger log = LogManager.getLogger(WineCSVImporter.class);

    /**
     * Reads objects of type Wine from file
     *
     * @param file File to read from
     * @return List of objects type T that are read from the file
     */
    public static List<Wine> readFromFile(InputStream file) {
        ArrayList<Wine> wines = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file))) {
            reader.skip(1);
            String[] line;
            boolean toRead = true;
            while (toRead) {
                line = reader.readNext();
                if (line != null) {
                    wines.add(readWineFromLine(line));
                } else {
                    toRead = false;
                }
            }
            return wines;
        } catch (IOException | CsvValidationException e) {
            log.error(e);
            return Collections.emptyList();
        }
    }

    public static Wine readWineFromLine(String[] line) {
        try {
            String name = line[0];
            String country = line[1];
            String colour = line[2];
            String style = line[3];
            String[] grapes = line[4].split(", ");
            String fullness = line[5];

            String lDescription = line[6];
            float price = Float.parseFloat(line[7]);
            String[] awards = line[8].split(", ");
            float aBV = Float.parseFloat(line[9]);
            float volume = 10 * Float.parseFloat(line[10]);
            //following to be added in preprocessing
            int uniqueID = Integer.parseInt(line[11]);
            int year = line[12].equals("Unknown") ? 0 : Integer.parseInt(line[12]);
            return new Wine(
                    uniqueID,
                    name,
                    country,
                    colour,
                    style,
                    grapes,
                    fullness,
                    lDescription,
                    price,
                    awards,
                    aBV,
                    volume,
                    year);
        } catch (NumberFormatException e) {
            log.error(e);
            return null;
        }
    }
}
