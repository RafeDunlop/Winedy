package seng202.team3.io;

import java.io.File;
import java.util.List;

/**
 * Simple interface for reading objects from file
 */
public interface Importable<T> {

    /**
     * Reads objects of type T from file
     *
     * @param file File to read from
     * @return List of objects type T that are read from the file
     */
    List<T> readFromFile(File file);
}
