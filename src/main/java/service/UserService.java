package service;

import entities.User;

import java.util.Optional;

public interface UserService {

    User findByUsername(String username);

    Optional<User> findById(int id);

    boolean hasValidCredentials(String username, String password);

    void create(User user);

    void update(User user);

    void deleteById(int id);

}
