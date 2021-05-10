package dao.impl;

import dao.UserDao;
import database.DBManager;
import entities.Role;
import entities.User;
import exceptions.DataIntegrityViolationException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * data access object for user entity
 */
public final class UserDaoImpl extends AbstractDao<User> implements UserDao {

    private static final String FIND_BY_USERNAME = "select * from user where username=?";

    public UserDaoImpl(DBManager dbManager) {
        super(dbManager, "user", new Mapper());
        super.saveStmt = "insert into user (username, password, name, phone_number, " +
                "email, role_id) values (?, ?, ?, ?, ?, ?)";
        super.updateStmt = "update user set username=?, password=?, name=?, " +
                "phone_number=?, email=?, role_id=? where id=?";
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

    private static class Mapper implements AbstractDao.Mapper<User> {
        @Override
        public void setSaveStatementParams(User user, PreparedStatement ps) throws SQLException {
            if (user.getUsername() == null || user.getPassword() == null ||
                    user.getName() == null || user.getPhoneNumber() == null ||
                    user.getRole() == null)
                throw new DataIntegrityViolationException();

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getPhoneNumber());
            ps.setString(5, user.getEmail());
            ps.setInt(6, user.getRole().toInt());
        }

        @Override
        public void setUpdateStatementParams(User user, PreparedStatement ps) throws SQLException {
            setSaveStatementParams(user, ps);
            ps.setInt(7, user.getId());
        }

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
