import dao.UserDao;
import dao.impl.UserDaoImpl;
import database.DBManager;
import entities.Role;
import entities.User;
import exceptions.DaoException;
import exceptions.DataIntegrityViolationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDaoTest {

    private static final String DB_URL = "jdbc:h2:~/restaurant";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "userpass";
    private DBManager dbManager;
    private UserDao userDao;
    private User user;

    @Before
    public void initDb() throws SQLException {
        String sql = "create table if not exists user_role (\n" +
                "id int primary key,\n" +
                "name varchar(45) unique not null\n" +
                ");\n" +
                "insert into user_role\n" +
                "values (0, 'manager'), (1, 'customer');\n" +
                "create table if not exists user (\n" +
                "id int primary key auto_increment,\n" +
                "username varchar(16) unique not null,\n" +
                "password varchar(255) not null,\n" +
                "name varchar(32) not null,\n" +
                "phone_number varchar(14) not null,\n" +
                "email varchar(320) null,\n" +
                "role_id int not null,\n" +
                "constraint fk_user_role_id\n" +
                "foreign key(role_id) references user_role(id)\n" +
                "on update cascade\n" +
                "on delete restrict\n" +
                ");\n " +
                "insert into user values \n" +
                "(default, 'manager', 'password', 'rita', '(067)346-31-37', null, 0)," +
                "(default, 'customer', 'password', 'rita', '(067)346-31-37', null, 1);";
        Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        Statement statement = con.createStatement();
        statement.executeUpdate(sql);
        dbManager = mock(DBManager.class);
        userDao = new UserDaoImpl(dbManager);
        user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setEmail("test");
        user.setPhoneNumber("test");
        user.setName("test");
        user.setRole(Role.CUSTOMER);
    }

    @Test
    public void whenFindAllCalled_thenReturnListOfUsers() throws DaoException, SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        List<User> users = userDao.findAll();
        Assert.assertNotNull(users);
    }

    @Test
    public void whenFindByIdCalled_thenReturnUser() throws DaoException, SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        User user = userDao.findById(1).get();
        Assert.assertNotNull(user);
        Assert.assertEquals(1, user.getId());
    }

    @Test
    public void givenUserNotExists_whenFindByIdCalled_thenReturnNull() throws DaoException, SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        Assert.assertFalse(userDao.findById(-10).isPresent());
    }

    @Test
    public void whenFindByUsernameCalled_thenReturnUser() throws DaoException, SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        User user = userDao.findByUsername("manager").get();
        Assert.assertNotNull(user);
        Assert.assertEquals("manager", user.getUsername());
    }

    @Test
    public void givenUserNotExists_whenFindByUsernameCalled_thenReturnNull() throws DaoException, SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        Assert.assertFalse(userDao.findByUsername("not existing user").isPresent());
    }

    @Test
    public void givenUsernameIsNull_whenFindByUsernameCalled_thenReturnNull() throws DaoException, SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        Assert.assertFalse(userDao.findByUsername(null).isPresent());
    }

    @Test
    public void whenSaveCalled_thenUpdateUserId() throws DaoException, SQLException,
            DataIntegrityViolationException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int id = user.getId();
        userDao.save(user);
        Assert.assertNotEquals(id, user.getId());
        Assert.assertNotNull(userDao.findByUsername("test"));
        Assert.assertNotNull(userDao.findById(user.getId()));
        Assert.assertTrue(userDao.findAll().contains(user));
    }

    @Test
    public void whenSaveCalled_thenSaveUserToDb() throws DaoException, SQLException,
            DataIntegrityViolationException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        userDao.save(user);
        Assert.assertNotNull(userDao.findByUsername("test"));
        Assert.assertNotNull(userDao.findById(user.getId()));
        Assert.assertTrue(userDao.findAll().contains(user));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenUserObjectHasNullUsername_whenSaveCalled_thenThrowDataIntegrityViolationException()
            throws DaoException, SQLException, DataIntegrityViolationException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        user.setUsername(null);
        userDao.save(user);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenUserObjectHasNullFields_whenSaveCalled_thenThrowDataIntegrityViolationException()
            throws DaoException, SQLException, DataIntegrityViolationException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        user.setPassword(null);
        user.setRole(null);
        user.setPhoneNumber(null);
        user.setName(null);
        userDao.save(user);
    }

    @Test
    public void givenUserObjectHasNullEmail_whenSaveCalled_thenSaveUserToDb()
            throws DaoException, SQLException, DataIntegrityViolationException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        user.setEmail(null);
        userDao.save(user);
        Assert.assertNotNull(userDao.findByUsername("test"));
        Assert.assertNotNull(userDao.findById(user.getId()));
        Assert.assertTrue(userDao.findAll().contains(user));
    }

    @Test
    public void givenUserObjectHasNegativeId_whenSaveCalled_thenSaveUserToDbAndUpdateId()
            throws DaoException, SQLException, DataIntegrityViolationException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        user.setId(-10);
        userDao.save(user);
        Assert.assertNotEquals(-10, user.getId());
        Assert.assertTrue(user.getId() > 0);
    }

    @Test
    public void givenUserObjectIsNull_whenSaveCalled_thenReturn()
            throws DaoException, SQLException, DataIntegrityViolationException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        user = null;
        userDao.save(user);
        Assert.assertFalse(userDao.findAll().contains(user));
    }

    @Test
    public void whenUpdateCalled_thenUpdateUser() throws DaoException, SQLException,
            DataIntegrityViolationException {
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
    public void whenUpdateUsernameCalled_thenUpdateUsername() throws DaoException, SQLException,
            DataIntegrityViolationException {
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
            throws DaoException, SQLException, DataIntegrityViolationException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        user.setPassword(null);
        user.setRole(null);
        user.setPhoneNumber(null);
        user.setName(null);
        userDao.update(user);
    }

    @Test
    public void givenUserObjectHasNotExistingId_whenUpdateCalled_thenReturn()
            throws DaoException, SQLException, DataIntegrityViolationException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        user.setId(1000);
        userDao.update(user);
        Assert.assertFalse(userDao.findById(user.getId()).isPresent());
        user.setId(-10);
        userDao.update(user);
        Assert.assertFalse(userDao.findAll().contains(null));
    }

    @Test
    public void givenUserObjectIsNull_whenUpdateCalled_thenReturn()
            throws DaoException, SQLException, DataIntegrityViolationException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        userDao.update(null);
        Assert.assertFalse(userDao.findAll().contains(null));
    }

    @Test
    public void whenDeleteUserByIdCalled_thenDeleteUser() throws DaoException, SQLException,
            DataIntegrityViolationException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        userDao.save(user);
        int size = userDao.findAll().size();
        userDao.deleteById(user.getId());
        Assert.assertFalse(userDao.findAll().contains(user));
        Assert.assertEquals(size - 1, userDao.findAll().size());
    }

    @Test
    public void givenUserObjectHasNotExistingId_whenDeleteUserByIdCalled_thenReturn()
            throws DaoException, SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int size = userDao.findAll().size();
        userDao.deleteById(100);
        Assert.assertEquals(size, userDao.findAll().size());
    }

    @Test
    public void whenDeleteUserCalled_thenDeleteUser() throws DaoException, SQLException,
            DataIntegrityViolationException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        userDao.save(user);
        int size = userDao.findAll().size();
        userDao.delete(user);
        Assert.assertFalse(userDao.findAll().contains(user));
        Assert.assertEquals(size - 1, userDao.findAll().size());
    }

    @Test
    public void givenUserObjectIsNull_whenDeleteUserCalled_thenReturn() throws DaoException, SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int size = userDao.findAll().size();
        userDao.delete(null);
        Assert.assertEquals(size, userDao.findAll().size());
    }

    @After
    public void clearDatabase() throws SQLException {
        String sql = "drop table if exists user;\n" +
                "drop table if exists user_role;\n";
        Connection con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        Statement statement = con.createStatement();
        statement.executeUpdate(sql);
    }

}
