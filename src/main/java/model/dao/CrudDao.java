package model.dao;

import java.util.List;
import java.util.Optional;

/**
 * general interface which all data access objects implement
 *
 * @param <T>
 */
public interface CrudDao<T> {

    /**
     * finds all objects stored in the table and returns
     * them as List
     *
     * @return List of T objects
     */
    List<T> findAll();

    /**
     * finds object by its unique identifier
     *
     * @param id unique entity identifier
     * @return nullable T object
     */
    Optional<T> findById(int id);

    /**
     * saves object to the table and updates its id
     *
     * @param t object to save
     */
    void save(T t);

    /**
     * updates object in the table
     *
     * @param t object to update
     */
    void update(T t);

    /**
     * deletes object from table by its unique identifier
     *
     * @param id entity identifier
     */
    void deleteById(int id);

    /**
     * deletes object from table
     *
     * @param t object to delete
     */
    void delete(T t);

}
