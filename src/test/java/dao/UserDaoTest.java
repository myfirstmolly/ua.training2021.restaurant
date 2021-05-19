package dao;

import dao.impl.UserDaoImpl;
import database.DBManager;
import entities.Role;
import entities.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
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
    private UserDao userDao;
    private User user;

    @Before
    public void before() throws SQLException {
        DBManager dbManager = mock(DBManager.class);
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        userDao = new UserDaoImpl(dbManager);
        user = new User("test", "test", "test",
                "test", "test", Role.CUSTOMER);
    }

    @Test
    public void whenFindAllCalled_thenReturnListOfUsers() {
        List<User> users = userDao.findAll();
        Assert.assertNotNull(users);
    }

    @Test
    public void whenFindByIdCalled_thenReturnUser() {
        userDao.save(user);
        int id = user.getId();
        User userFromDb = userDao.findById(id).get();
        Assert.assertNotNull(userFromDb);
        Assert.assertEquals(id, userFromDb.getId());
        Assert.assertEquals(user.toString(), userFromDb.toString());
        Assert.assertEquals(user.hashCode(), userFromDb.hashCode());
    }

    @Test
    public void givenUserNotExists_whenFindByIdCalled_thenReturnNull() {
        Assert.assertFalse(userDao.findById(-10).isPresent());
    }

    @Test
    public void whenFindByUsernameCalled_thenReturnUser() {
        userDao.save(user);
        User userFromDb = userDao.findByUsername(user.getUsername()).get();
        Assert.assertNotNull(userFromDb);
        Assert.assertEquals(user.getUsername(), userFromDb.getUsername());
    }

    @Test
    public void givenUserNotExists_whenFindByUsernameCalled_thenReturnNull() {
        Assert.assertFalse(userDao.findByUsername("not existing user").isPresent());
    }

    @Test
    public void givenUsernameIsNull_whenFindByUsernameCalled_thenReturnNull() {
        Assert.assertFalse(userDao.findByUsername(null).isPresent());
    }

    @Test
    public void whenSaveCalled_thenUpdateUserId() {
        int id = user.getId();
        userDao.save(user);
        Assert.assertNotEquals(id, user.getId());
        Assert.assertNotNull(userDao.findByUsername("test"));
        Assert.assertNotNull(userDao.findById(user.getId()));
        Assert.assertTrue(userDao.findAll().contains(user));
    }

    @Test
    public void whenSaveCalled_thenSaveUserToDb() {
        userDao.save(user);
        Assert.assertNotNull(userDao.findByUsername("test"));
        Assert.assertNotNull(userDao.findById(user.getId()));
        Assert.assertTrue(userDao.findAll().contains(user));
    }

    @Test
    public void givenUserObjectHasNullFields_whenSaveCalled_thenReturn() {
        user.setPassword(null);
        user.setRole(null);
        user.setPhoneNumber(null);
        user.setName(null);
        userDao.save(user);
        Assert.assertFalse(userDao.findById(user.getId()).isPresent());
    }

    @Test
    public void givenUserObjectHasNullEmail_whenSaveCalled_thenSaveUserToDb() {
        user.setEmail(null);
        userDao.save(user);
        Assert.assertNotNull(userDao.findByUsername("test"));
        Assert.assertNotNull(userDao.findById(user.getId()));
        Assert.assertTrue(userDao.findAll().contains(user));
    }

    @Test
    public void givenUserObjectHasNegativeId_whenSaveCalled_thenSaveUserToDbAndUpdateId() {
        int id = -10;
        user.setId(id);
        userDao.save(user);
        Assert.assertNotEquals(id, user.getId());
        Assert.assertTrue(user.getId() > 0);
        Assert.assertTrue(userDao.findAll().contains(user));
    }

    @Test
    public void givenUserObjectIsNull_whenSaveCalled_thenReturn() {
        userDao.save(null);
        Assert.assertFalse(userDao.findAll().contains(user));
    }

    @Test
    public void whenUpdateCalled_thenUpdateUser() {
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
    public void whenUpdateUsernameCalled_thenUpdateUsername() {
        userDao.save(user);
        user.setUsername("new name");
        userDao.update(user);
        User updated = userDao.findByUsername("new name").get();
        Assert.assertNotNull(updated);
        Assert.assertFalse(userDao.findByUsername("test").isPresent());
        Assert.assertEquals("new name", updated.getUsername());
        Assert.assertEquals(user.getId(), updated.getId());
    }

    @Test
    public void givenUserObjectHasNullFields_whenUpdateCalled_thenReturn() {
        userDao.save(user);
        user.setPassword(null);
        user.setRole(null);
        user.setPhoneNumber(null);
        user.setName(null);
        userDao.update(user);
        Assert.assertNotNull(userDao.findById(user.getId()).get().getPassword());
        Assert.assertNotNull(userDao.findById(user.getId()).get().getRole());
        Assert.assertNotNull(userDao.findById(user.getId()).get().getPhoneNumber());
        Assert.assertNotNull(userDao.findById(user.getId()).get().getName());
    }

    @Test
    public void givenUserObjectHasNotExistingId_whenUpdateCalled_thenReturn() {
        user.setId(1000);
        userDao.update(user);
        Assert.assertFalse(userDao.findById(user.getId()).isPresent());
        user.setId(-10);
        userDao.update(user);
        Assert.assertFalse(userDao.findAll().contains(user));
    }

    @Test
    public void givenUserObjectIsNull_whenUpdateCalled_thenReturn() {
        userDao.update(null);
        Assert.assertFalse(userDao.findAll().contains(null));
    }

    @Test
    public void whenDeleteUserByIdCalled_thenDeleteUser() {
        userDao.save(user);
        int size = userDao.findAll().size();
        userDao.deleteById(user.getId());
        Assert.assertFalse(userDao.findAll().contains(user));
        Assert.assertEquals(size - 1, userDao.findAll().size());
    }

    @Test
    public void givenUserObjectHasNotExistingId_whenDeleteUserByIdCalled_thenReturn() {
        int size = userDao.findAll().size();
        userDao.deleteById(100);
        Assert.assertEquals(size, userDao.findAll().size());
    }

    @Test
    public void whenDeleteUserCalled_thenDeleteUser() {
        userDao.save(user);
        int size = userDao.findAll().size();
        userDao.delete(user);
        Assert.assertFalse(userDao.findAll().contains(user));
        Assert.assertEquals(size - 1, userDao.findAll().size());
    }

    @Test
    public void givenUserObjectIsNull_whenDeleteUserCalled_thenReturn() {
        int size = userDao.findAll().size();
        userDao.delete(null);
        Assert.assertEquals(size, userDao.findAll().size());
    }

    @After
    public void clearDatabase() {
        userDao.delete(user);
    }

}
