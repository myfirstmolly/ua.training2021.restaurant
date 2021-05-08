package dao;

import exceptions.DaoException;
import exceptions.DataIntegrityViolationException;

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
     * @throws DaoException if select statement wasn't executed
     */
    List<T> findAll() throws DaoException;

    /**
     * returns nullable entity object by its unique identifier
     *
     * @param id unique entity identifier
     * @return nullable T object
     * @throws DaoException if select statement wasn't executed
     */
    Optional<T> findById(int id) throws DaoException;

    /**
     * saves object to database and updates its id
     *
     * @param t object to save
     * @throws DaoException if insert statement wasn't executed
     * @throws DataIntegrityViolationException if object has inappropriate fields
     */
    void save(T t) throws DaoException, DataIntegrityViolationException;

    /**
     * updates entity in database
     *
     * @param t object to update
     * @throws DaoException if update statement wasn't executed
     * @throws DataIntegrityViolationException if object has inappropriate fields
     */
    void update(T t) throws DaoException, DataIntegrityViolationException;

    /**
     * deletes entity from database by it's unique identifier
     *
     * @param id entity identifier
     * @throws DaoException if delete statement wasn't executed
     */
    void deleteById(int id) throws DaoException;

    /**
     * deletes entity from database
     *
     * @param t object to delete
     * @throws DaoException if delete statement wasn't executed
     */
    void delete(T t) throws DaoException;

}
