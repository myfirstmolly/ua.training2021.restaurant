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
