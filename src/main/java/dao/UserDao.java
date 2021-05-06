package dao;

import entities.User;
import exceptions.DatabaseException;

public interface UserDao extends CrudDao<User> {

    User findByUsername(String username) throws DatabaseException;

}
