package seng202.team3.io;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import seng202.team3.models.Wine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WineCSVImporter implements Importable {



    /**
     * Reads objects of type Wine from file
     *
     * @param file File to read from
     * @return List of objects type T that are read from the file
     */
    @Override
    public List<Wine> readFromFile(File file) {
        ArrayList<Wine> wines = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
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
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private Wine readWineFromLine(String[] line) {
        try {
            String name = line[0];
            String country = line[1];
            String type = line[2];
            String sDescription = line[5];
            String lDescription = line[6];
            float price = Float.parseFloat(line[7]);
            String awards = line[9];
            float aBV = Float.parseFloat(line[10].substring(0, line[10].length() - 1));
            float volume = 10 * Float.parseFloat(line[11]);
            //following to be added in preprocessing
            int year = Integer.parseInt(line[12]);
            int uniqueID = Integer.parseInt(line[13]);
            return new Wine(
                    uniqueID,
                    name,
                    type,
                    country,
                    year,
                    sDescription,
                    lDescription,
                    awards,
                    price,
                    aBV,
                    volume);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
