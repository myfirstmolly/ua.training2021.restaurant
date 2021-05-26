package model.dao.impl;

import model.dao.UserDao;
import model.dao.mapper.UserMapper;
import model.database.DBManager;
import model.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * data access object for user entity
 */
public final class UserDaoImpl implements UserDao {

    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);
    private final DBManager dbManager;

    public UserDaoImpl(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "select * from user where username=?";
        logger.info("sql: " + sql);
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(getMapper().map(rs));
            }
        } catch (SQLException ex) {
            logger.error("cannot get object from table user", ex);
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from user";
        logger.info("sql: " + sql);
        List<User> entries = new ArrayList<>();

        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    entries.add(getMapper().map(rs));
            }
        } catch (SQLException ex) {
            logger.error("cannot obtain objects from table user", ex);
        }
        return entries;
    }

    @Override
    public Optional<User> findById(int id) {
        String sql = "select * from user where id=?";
        logger.info("sql: " + sql);
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(getMapper().map(rs));
            }
        } catch (SQLException ex) {
            logger.error("cannot get object from table user", ex);
        }

        return Optional.empty();
    }

    @Override
    public void save(User user) {
        if (user == null) {
            logger.warn("cannot save null object");
            return;
        }
        String sql = "insert into user (username, password, name, phone_number, email, role_id " +
                "values(?, ?, ?, ?, ?, ?)";
        logger.info("sql: " + sql);
        Connection con = null;
        try {
            con = dbManager.getConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            con.setAutoCommit(false);
            setParams(user, ps);

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }
            }
        } catch (SQLException ex) {
            logger.error("exception occurred during statement execution", ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

    @Override
    public void update(User user) {
        if (user == null) {
            logger.warn("cannot update null object");
            return;
        }
        String sql = "update user set username=?, password=?, name=?, phone_number=?," +
                "email=?, role_id=? where id=?";
        logger.info("sql: " + sql);
        Connection con = null;
        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql);
            setParams(user, ps);
            ps.setInt(7, user.getId());
        } catch (SQLException ex) {
            logger.error("exception occurred during statement execution", ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "delete from user where id=?";
        logger.info("sql: " + sql);
        Connection con = null;

        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.error(String.format("cannot delete row{id=%s} from table user", id), ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

    private void setParams(User user, PreparedStatement ps) throws SQLException {
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getName());
        ps.setString(4, user.getPhoneNumber());
        ps.setString(5, user.getEmail());
        ps.setInt(6, user.getRole().getId());
        ps.executeUpdate();
    }

    private static UserMapper getMapper() {
        return new UserMapper("id", "username", "password", "name",
                "phone_number", "email", "role_id");
    }

}
