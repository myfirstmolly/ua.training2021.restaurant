package model.service.impl;

import model.dao.UserDao;
import model.entities.User;
import model.service.UserService;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null)
            return Optional.empty();
        return userDao.findByUsername(username);
    }

    @Override
    public Optional<User> findById(int id) {
        return userDao.findById(id);
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
