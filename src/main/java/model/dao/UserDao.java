package model.dao;

import model.entities.User;

import java.util.Optional;

/**
 * data access object interface for user table
 */
public interface UserDao extends CrudDao<User> {

    /**
     * finds user by its unique username
     *
     * @param username user's username
     * @return optional User object
     */
    Optional<User> findByUsername(String username);

}
