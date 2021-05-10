package dao;

import dao.impl.UserDaoImpl;
import database.DBManager;
import entities.Role;
import entities.User;
import exceptions.DataIntegrityViolationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDaoTest {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/restaurant_test?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private DBManager dbManager;
    private UserDao userDao;
    private User user;

    @Before
    public void before() {
        dbManager = mock(DBManager.class);
        userDao = new UserDaoImpl(dbManager);
        user = new User("test", "test", "test",
                "test", "test", Role.CUSTOMER);
    }

    @Test
    public void whenFindAllCalled_thenReturnListOfUsers() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        List<User> users = userDao.findAll();
        Assert.assertNotNull(users);
    }

    @Test
    public void whenFindByIdCalled_thenReturnUser() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        userDao.save(user);
        int id = user.getId();
        User user = userDao.findById(id).get();
        Assert.assertNotNull(user);
        Assert.assertEquals(id, user.getId());
    }

    @Test
    public void givenUserNotExists_whenFindByIdCalled_thenReturnNull() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        Assert.assertFalse(userDao.findById(-10).isPresent());
    }

    @Test
    public void whenFindByUsernameCalled_thenReturnUser() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        userDao.save(user);
        User userFromDb = userDao.findByUsername(user.getUsername()).get();
        Assert.assertNotNull(userFromDb);
        Assert.assertEquals(user.getUsername(), userFromDb.getUsername());
    }

    @Test
    public void givenUserNotExists_whenFindByUsernameCalled_thenReturnNull() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        Assert.assertFalse(userDao.findByUsername("not existing user").isPresent());
    }

    @Test
    public void givenUsernameIsNull_whenFindByUsernameCalled_thenReturnNull() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        Assert.assertFalse(userDao.findByUsername(null).isPresent());
    }

    @Test
    public void whenSaveCalled_thenUpdateUserId() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int id = user.getId();
        userDao.save(user);
        Assert.assertNotEquals(id, user.getId());
        Assert.assertNotNull(userDao.findByUsername("test"));
        Assert.assertNotNull(userDao.findById(user.getId()));
        Assert.assertTrue(userDao.findAll().contains(user));
    }

    @Test
    public void whenSaveCalled_thenSaveUserToDb() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        userDao.save(user);
        Assert.assertNotNull(userDao.findByUsername("test"));
        Assert.assertNotNull(userDao.findById(user.getId()));
        Assert.assertTrue(userDao.findAll().contains(user));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenUserObjectHasNullUsername_whenSaveCalled_thenThrowDataIntegrityViolationException()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        user.setUsername(null);
        userDao.save(user);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenUserObjectHasNullFields_whenSaveCalled_thenThrowDataIntegrityViolationException()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        user.setPassword(null);
        user.setRole(null);
        user.setPhoneNumber(null);
        user.setName(null);
        userDao.save(user);
    }

    @Test
    public void givenUserObjectHasNullEmail_whenSaveCalled_thenSaveUserToDb() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        user.setEmail(null);
        userDao.save(user);
        Assert.assertNotNull(userDao.findByUsername("test"));
        Assert.assertNotNull(userDao.findById(user.getId()));
        Assert.assertTrue(userDao.findAll().contains(user));
    }

    @Test
    public void givenUserObjectHasNegativeId_whenSaveCalled_thenSaveUserToDbAndUpdateId() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int id = -10;
        user.setId(id);
        userDao.save(user);
        Assert.assertNotEquals(id, user.getId());
        Assert.assertTrue(user.getId() > 0);
        Assert.assertTrue(userDao.findAll().contains(user));
    }

    @Test
    public void givenUserObjectIsNull_whenSaveCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        userDao.save(null);
        Assert.assertFalse(userDao.findAll().contains(user));
    }

    @Test
    public void whenUpdateCalled_thenUpdateUser() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        userDao.save(user);
        user.setName("new name");
        userDao.update(user);
        User updated = userDao.findByUsername("test").get();
        Assert.assertNotNull(updated);
        Assert.assertEquals("new name", updated.getName());
        Assert.assertEquals(user.getId(), updated.getId());
        Assert.assertEquals(user, updated);
    }

    @Test
    public void whenUpdateUsernameCalled_thenUpdateUsername() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        userDao.save(user);
        user.setUsername("new name");
        userDao.update(user);
        User updated = userDao.findByUsername("new name").get();
        Assert.assertNotNull(updated);
        Assert.assertFalse(userDao.findByUsername("test").isPresent());
        Assert.assertEquals("new name", updated.getUsername());
        Assert.assertEquals(user.getId(), updated.getId());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenUserObjectHasNullFields_whenUpdateCalled_thenThrowDataIntegrityViolationException()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        user.setPassword(null);
        user.setRole(null);
        user.setPhoneNumber(null);
        user.setName(null);
        userDao.update(user);
    }

    @Test
    public void givenUserObjectHasNotExistingId_whenUpdateCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        user.setId(1000);
        userDao.update(user);
        Assert.assertFalse(userDao.findById(user.getId()).isPresent());
        user.setId(-10);
        userDao.update(user);
        Assert.assertFalse(userDao.findAll().contains(user));
    }

    @Test
    public void givenUserObjectIsNull_whenUpdateCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        userDao.update(null);
        Assert.assertFalse(userDao.findAll().contains(null));
    }

    @Test
    public void whenDeleteUserByIdCalled_thenDeleteUser() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        userDao.save(user);
        int size = userDao.findAll().size();
        userDao.deleteById(user.getId());
        Assert.assertFalse(userDao.findAll().contains(user));
        Assert.assertEquals(size - 1, userDao.findAll().size());
    }

    @Test
    public void givenUserObjectHasNotExistingId_whenDeleteUserByIdCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int size = userDao.findAll().size();
        userDao.deleteById(100);
        Assert.assertEquals(size, userDao.findAll().size());
    }

    @Test
    public void whenDeleteUserCalled_thenDeleteUser() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        userDao.save(user);
        int size = userDao.findAll().size();
        userDao.delete(user);
        Assert.assertFalse(userDao.findAll().contains(user));
        Assert.assertEquals(size - 1, userDao.findAll().size());
    }

    @Test
    public void givenUserObjectIsNull_whenDeleteUserCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int size = userDao.findAll().size();
        userDao.delete(null);
        Assert.assertEquals(size, userDao.findAll().size());
    }

    @After
    public void clearDatabase() {
        userDao.delete(user);
    }

}
