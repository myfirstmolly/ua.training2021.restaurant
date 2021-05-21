package model.dao.impl;

import model.dao.UserDao;
import model.database.DBManager;
import model.entities.Role;
import model.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * data access object for user entity
 */
public final class UserDaoImpl implements UserDao {

    private static final String TABLE_NAME = "user";
    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);
    private final DaoUtils<User> daoUtils;

    public UserDaoImpl(DBManager dbManager) {
        daoUtils = new DaoUtils<>(dbManager, TABLE_NAME, rs -> {
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

    @Override
    public Optional<User> findByUsername(String username) {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setSelect().setWhere("username").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getOptional(sql, username);
    }

    @Override
    public List<User> findAll() {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setSelect().build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getList(sql);
    }

    @Override
    public Optional<User> findById(int id) {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setSelect().setWhere("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getOptional(sql, id);
    }

    @Override
    public void save(User user) {
        if (user == null) {
            logger.warn("cannot save null object");
            return;
        }
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setInsert(getParams()).build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        daoUtils.save(user, sql, user.getUsername(), user.getPassword(), user.getName(),
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
        String sql = b.setUpdate(getParams()).setWhere("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        daoUtils.update(sql, user.getUsername(), user.getPassword(), user.getName(),
                user.getPhoneNumber(), user.getEmail(),
                user.getRole() == null ? null : user.getRole().toInt(),
                user.getId());
    }

    @Override
    public void deleteById(int id) {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setDelete().setWhere("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        daoUtils.deleteById(id, sql);
    }

    @Override
    public void delete(User user) {
        if (user == null) {
            logger.warn("Cannot delete null object in from table " + TABLE_NAME);
            return;
        }
        deleteById(user.getId());
    }

    private String[] getParams() {
        return new String[]{"username", "password", "name", "phone_number", "email", "role_id"};
    }
}
