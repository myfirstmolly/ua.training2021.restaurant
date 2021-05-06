import dao.UserDao;
import dao.impl.UserDaoImpl;
import database.DBManager;
import entities.Role;
import entities.User;
import exceptions.DatabaseException;
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
    private static final String USER = "user";
    private static final String PASS = "userpass";
    private DBManager dbManager;
    private UserDao userDao;

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
        Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = con.createStatement();
        statement.executeUpdate(sql);
        dbManager = mock(DBManager.class);
        userDao = new UserDaoImpl(dbManager);
    }

    @Test
    public void whenFindAllCalled_thenReturnListOfUsers() throws DatabaseException, SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USER, PASS));
        List<User> users = userDao.findAll();
        System.out.println(users);
        Assert.assertNotNull(users);
    }

    @Test
    public void whenFindByIdCalled_thenReturnUser() throws DatabaseException, SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USER, PASS));
        User user = userDao.findById(1);
        Assert.assertNotNull(user);
        Assert.assertEquals(1, user.getId());
    }

    @Test
    public void whenFindByUsernameCalled_thenReturnUser() throws DatabaseException, SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USER, PASS));
        User user = userDao.findByUsername("manager");
        Assert.assertNotNull(user);
        Assert.assertEquals("manager", user.getUsername());
    }

    @Test
    public void whenSaveCalled_thenUpdateUserId() throws DatabaseException, SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USER, PASS));
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setEmail("test");
        user.setPhoneNumber("test");
        user.setName("test");
        user.setRole(Role.CUSTOMER);
        int id = user.getId();
        userDao.save(user);
        Assert.assertNotEquals(id, user.getId());
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USER, PASS));
        Assert.assertNotNull(userDao.findByUsername("test"));
        Assert.assertNotNull(userDao.findById(user.getId()));
        Assert.assertTrue(userDao.findAll().contains(user));
    }

    @After
    public void clearDatabase() throws SQLException {
        String sql = "drop table if exists user;\n" +
                "drop table if exists user_role;\n";
        Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
        Statement statement = con.createStatement();
        statement.executeUpdate(sql);
    }

}
