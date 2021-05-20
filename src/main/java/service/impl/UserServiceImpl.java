package service.impl;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import database.DBManager;
import entities.User;
import service.UserService;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(DBManager dbManager) {
        userDao = new UserDaoImpl(dbManager);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Optional<User> findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public boolean hasValidCredentials(String username, String password) {
        if (username == null || password == null)
            return false;

        if (!userDao.findByUsername(username).isPresent())
            return false;

        return userDao.findByUsername(username).get().getPassword().equals(password);
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void deleteById(int id) {
        userDao.deleteById(id);
    }

}
