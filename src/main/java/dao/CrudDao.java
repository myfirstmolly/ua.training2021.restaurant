package dao;

import exceptions.DatabaseException;

import java.util.List;

/**
 * general interface which all data access objects implement
 *
 * @param <T>
 */
public interface CrudDao<T> {

    List<T> findAll() throws DatabaseException;

    T findById(int id) throws DatabaseException;

    void save(T t) throws DatabaseException;

    void update(T t) throws DatabaseException;

    void deleteById(int id) throws DatabaseException;

    void delete(T t) throws DatabaseException;

}
