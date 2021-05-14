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
     * @return list of T objects
     */
    List<T> findAll();

    /**
     * @param id unique entity identifier
     * @return nullable T object
     */
    Optional<T> findById(int id);

    /**
     * @param t object to save
     */
    void save(T t);

    /**
     * @param t object to update
     */
    void update(T t);

    /**
     * @param id entity identifier
     */
    void deleteById(int id);

    /**
     * @param t object to delete
     */
    void delete(T t);

}
