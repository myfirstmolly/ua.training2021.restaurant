package dao.impl;

import dao.UserDao;
import database.DBManager;
import entities.Role;
import entities.User;
import exceptions.DaoException;
import exceptions.DataIntegrityViolationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * data access object for user entity
 */
public class UserDaoImpl implements UserDao {

    private static final String FIND_ALL =
            "select * from user";

    private static final String FIND_BY_ID =
            "select * from user where id=?";

    private static final String FIND_BY_USERNAME =
            "select * from user where username=?";

    private static final String SAVE =
            "insert into user (username, password, name, phone_number, " +
                    "email, role_id) values (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "update user set username=?, password=?, name=?, " +
                    "phone_number=?, email=?, role_id=? where id=?";

    private static final String DELETE =
            "delete from user where id=?";

    private final DBManager dbManager;

    public UserDaoImpl(DBManager dbManager) {
        this.dbManager = Objects.requireNonNull(dbManager);
    }

    /**
     * @inheritDoc
     * returns list of all users from database
     *
     * @return list of all users
     * @throws DaoException if select statement wasn't executed
     */
    @Override
    public List<User> findAll() throws DaoException {
        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(fetchResult(rs));
            }
            return users;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException("Users not found, cause: " + ex.getCause(), ex);
        }
    }

    /**
     * @inheritDoc
     * returns user with given id
     *
     * @param id user unique identifier
     * @return optional user object
     * @throws DaoException if select statement wasn't executed
     */
    @Override
    public Optional<User> findById(int id) throws DaoException {
        if (id <= 0) return Optional.empty();

        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.ofNullable(fetchResult(rs));
                return Optional.empty();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException(String.format("User with id={%s} not found, cause: %s",
                    id, ex.getCause()), ex);
        }
    }

    /**
     * @inheritDoc
     * returns user with given username
     *
     * @param username user's username
     * @return optional user object
     * @throws DaoException if select statement wasn't executed
     */
    @Override
    public Optional<User> findByUsername(String username) throws DaoException {
        if (username == null) return Optional.empty();

        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(FIND_BY_USERNAME)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.ofNullable(fetchResult(rs));
                return Optional.empty();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException(String.format("User with username='%s' not found, cause: %s",
                    username, ex.getCause()), ex);
        }
    }

    /**
     * @inheritDoc
     * saves user object to database and modifies its id
     *
     * @param user user entity to store to database
     * @throws DaoException if insert statement wasn't executed
     * @throws DataIntegrityViolationException if username, password, name,
     * phoneNumber or role are null
     */
    @Override
    public void save(User user) throws DaoException, DataIntegrityViolationException {
        if (user == null) return;

        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            setPreparedStatementParams(user, ps);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    user.setId(id);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException(String.format("%s not saved, cause: %s",
                    user.toString(), ex.getCause()), ex);
        }
    }

    /**
     * @inheritDoc
     * updates user
     *
     * @param user user entity to update
     * @throws DaoException if update statement wasn't executed
     * @throws DataIntegrityViolationException if username, password, name,
     * phoneNumber or role are null
     */
    @Override
    public void update(User user) throws DaoException, DataIntegrityViolationException {
        if (user == null) return;

        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(UPDATE)) {
            setPreparedStatementParams(user, ps);
            ps.setInt(7, user.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException(String.format("%s not updated, cause: %s",
                    user.toString(), ex.getCause()), ex);
        }
    }

    /**
     * @inheritDoc
     * delete user with given id
     *
     * @param id user unique identifier
     * @throws DaoException if delete statement wasn't executed
     */
    @Override
    public void deleteById(int id) throws DaoException {
        if (id <= 0) return;

        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DaoException(String.format("User with id={%s} not deleted, cause: %s",
                    id, ex.getCause()), ex);
        }
    }

    /**
     * @inheritDoc
     * deletes user entity
     *
     * @param user user entity
     * @throws DaoException if delete statement wasn't executed
     */
    @Override
    public void delete(User user) throws DaoException {
        if (user == null) return;
        deleteById(user.getId());
    }

    private User fetchResult(ResultSet rs) throws SQLException {
        if (rs == null) return null;

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

    private void setPreparedStatementParams(User user, PreparedStatement ps)
            throws SQLException, DataIntegrityViolationException {
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

}
