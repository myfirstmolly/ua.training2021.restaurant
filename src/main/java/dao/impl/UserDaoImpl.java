package dao.impl;

import dao.UserDao;
import database.DBManager;
import entities.Role;
import entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/**
 * data access object for user entity
 */
public final class UserDaoImpl extends DaoUtils<User> implements UserDao {

    private static final String TABLE_NAME = "user";
    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);

    public UserDaoImpl(DBManager dbManager) {
        super(dbManager, TABLE_NAME, rs -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setName(rs.getString("name"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setEmail(rs.getString("email"));
            user.setRole(Role.values()[rs.getInt("role_id")]);
            return user;
        });
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
        String sql = b.select().where("username").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return super.getOptional(sql, username);
    }

    @Override
    public void save(User user) {
        if (user == null) {
            logger.warn("cannot save null object");
            return;
        }
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.insert(getParams()).build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        super.save(user, sql, user.getUsername(), user.getPassword(), user.getName(),
                user.getPhoneNumber(), user.getEmail(),
                user.getRole() == null ? null : user.getRole().toInt());
    }

    @Override
    public void update(User user) {
        if (user == null) {
            logger.warn("cannot update null object");
            return;
        }
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.update(getParams()).where("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        super.update(sql, user.getUsername(), user.getPassword(), user.getName(),
                user.getPhoneNumber(), user.getEmail(),
                user.getRole() == null ? null : user.getRole().toInt(),
                user.getId());
    }

    private String[] getParams() {
        return new String[]{"username", "password", "name", "phone_number", "email", "role_id"};
    }
}
