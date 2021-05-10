package service;

import entities.User;

public interface UserService {

    User findByUsername(String username);

    User findById(int id);

    boolean hasValidCredentials(String username, String password);

    void create(User user);

    void update(User user);

    void deleteById(int id);

}
