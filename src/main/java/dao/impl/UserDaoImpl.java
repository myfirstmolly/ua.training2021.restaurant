package dao.impl;

import dao.UserDao;
import database.DBManager;
import entities.Role;
import entities.User;
import exceptions.DataIntegrityViolationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * data access object for user entity
 */
public final class UserDaoImpl extends AbstractDao<User> implements UserDao {

    public UserDaoImpl(DBManager dbManager) {
        super(dbManager, "user", new Mapper());
    }

    /**
     * returns user with given username
     *
     * @param username user's username
     * @return optional user object
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return super.findOneByParameter(new Param(username, "username"));
    }

    @Override
    public void save(User user) {
        if (user == null) return;
        super.save(user, getParams(user));
    }

    @Override
    public void update(User user) {
        if (user == null) return;
        super.update(user.getId(), getParams(user));
    }

    private Param [] getParams(User user) {
        if (user.getUsername() == null || user.getPassword() == null ||
                user.getName() == null || user.getPhoneNumber() == null ||
                user.getRole() == null)
            throw new DataIntegrityViolationException();

        Param username = new Param(user.getUsername(), "username");
        Param password = new Param(user.getPassword(), "password");
        Param name = new Param(user.getName(), "name");
        Param phoneNumber = new Param(user.getPhoneNumber(), "phone_number");
        Param email = new Param(user.getEmail(), "email");
        Param roleId = new Param(user.getRole().toInt(), "role_id");
        return new Param[] {username, password, name, phoneNumber, email, roleId};
    }

    private static class Mapper implements AbstractDao.Mapper<User> {
        @Override
        public User map(ResultSet rs) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setName(rs.getString("name"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setEmail(rs.getString("email"));
            user.setRole(Role.values()[rs.getInt("role_id")]);
            return user;
        }
    }
}
