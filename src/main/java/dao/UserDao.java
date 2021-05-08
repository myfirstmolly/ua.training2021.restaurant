package dao;

import entities.User;
import exceptions.DaoException;

import java.util.Optional;

public interface UserDao extends CrudDao<User> {

    Optional<User> findByUsername(String username) throws DaoException;

}
