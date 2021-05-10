package dao;

import java.util.List;
import java.util.Optional;

/**
 * general interface which all data access objects implement
 *
 * @param <T>
 */
public interface CrudDao<T> {

    /**
     * returns list of entity objects in database
     *
     * @return list of T objects
     */
    List<T> findAll();

    /**
     * returns nullable entity object by its unique identifier
     *
     * @param id unique entity identifier
     * @return nullable T object
     */
    Optional<T> findById(int id);

    /**
     * saves object to database and updates its id
     *
     * @param t object to save
     */
    void save(T t);

    /**
     * updates entity in database
     *
     * @param t object to update
     */
    void update(T t);

    /**
     * deletes entity from database by it's unique identifier
     *
     * @param id entity identifier
     */
    void deleteById(int id);

    /**
     * deletes entity from database
     *
     * @param t object to delete
     */
    void delete(T t);

}
