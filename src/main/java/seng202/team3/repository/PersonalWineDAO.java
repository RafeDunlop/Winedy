package seng202.team3.repository;

import seng202.team3.exceptions.WineDrinkerAlreadyExistsException;
import seng202.team3.models.Wine;

import java.util.List;

public class PersonalWineDAO implements DAOInterface<Wine> {
    /**
     * Gets all of Wine from the database
     *
     * @return List of all objects type Wine from the database
     */
    @Override
    public List<Wine> getAll() {
        return null;
    }

    /**
     * Adds a single object of type Wine to database
     *
     * @param toAdd object of type Wine to add
     * @return object insert id if inserted correctly
     * @throws WineDrinkerAlreadyExistsException if method is called with a WineDrinker with a username that already exists
     */
    @Override
    public int add(Wine toAdd) throws WineDrinkerAlreadyExistsException {
        return 0;
    }

    /**
     * Deletes and object from database that matches id given
     *
     * @param id id of object to delete
     */
    @Override
    public void delete(int id) {

    }

    /**
     * Updates an object in the database
     *
     * @param toUpdate Object that needs to be updated (this object must be able to identify itself and its previous self)
     */
    @Override
    public void update(Wine toUpdate) {

    }
}
