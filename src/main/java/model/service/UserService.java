package model.service;

import model.entities.User;

import java.util.Optional;

public interface UserService {

    /**
     * finds User by their username
     *
     * @param username user's username
     * @return Optional User object
     */
    Optional<User> findByUsername(String username);

    /**
     * finds User by their id
     *
     * @param id user's unique identifier
     * @return Optional User object
     */
    Optional<User> findById(int id);

    /**
     * checks if credentials (i.e. username and password match those stored in model.database
     *
     * @param username user's username
     * @param password user's password
     * @return true if credentials are valid, false - if not
     */
    boolean hasValidCredentials(String username, String password);

    /**
     * saves new user to db
     *
     * @param user user to save
     */
    void save(User user);

    /**
     * updates user
     *
     * @param user user to update
     */
    void update(User user);

    /**
     * deletes user by their id
     *
     * @param id user's unique identifier
     */
    void deleteById(int id);

}
