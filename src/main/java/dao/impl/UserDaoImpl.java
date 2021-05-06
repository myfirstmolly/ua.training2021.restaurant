package dao.impl;

import dao.UserDao;
import database.DBManager;
import entities.Role;
import entities.User;
import exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        this.dbManager = dbManager;
    }

    /**
     * @return list of all users
     * @throws DatabaseException if nothing is found
     */
    @Override
    public List<User> findAll() throws DatabaseException {
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
            throw new DatabaseException("Users not found", ex);
        }
    }

    /**
     * @param id user unique identifier
     * @return user entity
     * @throws DatabaseException if user not found
     */
    @Override
    public User findById(int id) throws DatabaseException {
        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return fetchResult(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException(String.format("User with id={%s} not found", id), ex);
        }
    }

    /**
     * @param username user's username
     * @return user entity
     * @throws DatabaseException if user not found
     */
    @Override
    public User findByUsername(String username) throws DatabaseException {
        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(FIND_BY_USERNAME)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return fetchResult(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException(String.format("User with username={%s} not found", username), ex);
        }
    }

    /**
     * @param user user entity to store to database
     * @throws DatabaseException if user wasn't saved
     */
    @Override
    public void save(User user) throws DatabaseException {
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            setPreparedStatementValues(user, ps);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                int id = keys.getInt(1);
                user.setId(id);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException(String.format("%s not deleted", user.toString()), ex);
        }
    }

    /**
     * @param user user entity to update
     * @throws DatabaseException if user wasn't updated
     */
    @Override
    public void update(User user) throws DatabaseException {
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            setPreparedStatementValues(user, ps);
            ps.setInt(7, user.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException(String.format("%s not updated", user.toString()), ex);
        }
    }

    /**
     * @param id user unique identifier
     * @throws DatabaseException if user wasn't deleted
     */
    @Override
    public void deleteById(int id) throws DatabaseException {
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseException(String.format("User with id={%s} not deleted", id), ex);
        }
    }

    /**
     * @param user user entity
     * @throws DatabaseException if user wasn't deleted
     */
    @Override
    public void delete(User user) throws DatabaseException {
        deleteById(user.getId());
    }

    private User fetchResult(ResultSet rs) throws SQLException {
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

    private void setPreparedStatementValues(User user, PreparedStatement ps) throws SQLException {
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getName());
        ps.setString(4, user.getPhoneNumber());
        ps.setString(5, user.getEmail());
        ps.setInt(6, user.getRole().toInt());
    }

}
