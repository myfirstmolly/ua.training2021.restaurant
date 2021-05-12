package dao.impl;

import dao.UserDao;
import database.DBManager;
import entities.Role;
import entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * data access object for user entity
 */
public final class UserDaoImpl extends AbstractDao<User> implements UserDao {

    private static final String TABLE_NAME = "user";

    public UserDaoImpl(DBManager dbManager) {
        super(dbManager, TABLE_NAME, new Mapper());
    }

    /**
     * returns user with given username
     *
     * @param username user's username
     * @return optional user object
     */
    @Override
    public Optional<User> findByUsername(String username) {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        b.select().where("username");
        return super.getOptional(b.build(), username);
    }

    @Override
    public void save(User user) {
        if (user == null) return;
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        b.insert(getParams());
        super.save(user, b.build(), user.getUsername(), user.getPassword(), user.getName(),
                user.getPhoneNumber(), user.getEmail(),
                user.getRole() == null ? null : user.getRole().toInt());
    }

    @Override
    public void update(User user) {
        if (user == null) return;
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        b.update(getParams()).where("id");
        super.update(user.getId(), b.build(), user.getUsername(), user.getPassword(), user.getName(),
                user.getPhoneNumber(), user.getEmail(),
                user.getRole() == null ? null : user.getRole().toInt());
    }

    private String [] getParams() {
        return new String[] {"username", "password", "name", "phone_number", "email", "role_id"};
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
