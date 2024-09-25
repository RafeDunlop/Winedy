package seng202.team3.repository;

import seng202.team3.exceptions.WineDrinkerAlreadyExistsException;

import java.util.List;

/**
 * Interface for Database Access Objects (DAOs) that provides common functionality for database access
 *
 * @author Morgan English
 */
public interface DAOInterface<T> {
    /**
     * Gets all of T from the database where T contains a foreign key of a Wine Drinker, only tuples which reference
     * the currently logged in Wine Drinker are returned.
     * @return List of all objects type T from the database
     */
    List<T> getAll();


    /**
     * Adds a single object of type T to database
     * @param toAdd object of type T to add
     * @return object insert id if inserted correctly
     * @throws WineDrinkerAlreadyExistsException if method is called with a WineDrinker with a username that already exists
     */
    int add(T toAdd) throws WineDrinkerAlreadyExistsException;

    /**
     * Deletes and object from database that matches id given
     * @param toDelete Object to be deleted
     */
    int delete(T toDelete);

    /**
     * Updates an object in the database
     * @param toUpdate Object that needs to be updated (this object must be able to identify itself and its previous self)
     */
    int update(T toUpdate);

}

