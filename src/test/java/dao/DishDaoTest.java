package dao;

import dao.impl.CategoryDaoImpl;
import dao.impl.DishDaoImpl;
import database.DBManager;
import entities.Category;
import entities.Dish;
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

public class DishDaoTest {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/restaurant_test?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private DBManager dbManager;
    private DishDao dishDao;
    private Dish dish;

    @Before
    public void before() throws SQLException {
        dbManager = mock(DBManager.class);
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        dishDao = new DishDaoImpl(dbManager);
        CategoryDao categoryDao = new CategoryDaoImpl(dbManager);
        Category category = categoryDao.findById(1).get();
        dish = new Dish("test", "test", 8000,
                "test", "test", "test",
                category);
    }

    @Test
    public void whenFindAllCalled_thenReturnListOfCategories() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        List<Dish> categories = dishDao.findAll();
        Assert.assertNotNull(categories);
    }

    @Test
    public void whenFindByIdCalled_thenReturnDish() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        dishDao.save(dish);
        int id = dish.getId();
        Dish dishFromDb = dishDao.findById(id).get();
        Assert.assertNotNull(dishFromDb);
        Assert.assertEquals(id, dishFromDb.getId());
    }

    @Test
    public void givenDishNotExists_whenFindByIdCalled_thenReturnNull() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        Assert.assertFalse(dishDao.findById(-10).isPresent());
    }

    @Test
    public void whenSaveCalled_thenUpdateDishId() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int id = dish.getId();
        dishDao.save(dish);
        Assert.assertNotEquals(id, dish.getId());
    }

    @Test
    public void whenSaveCalled_thenSaveDishToDb() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        dishDao.save(dish);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenDishObjectHasNullNames_whenSaveCalled_thenThrowDataIntegrityViolationException()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        dish.setNameUkr(null);
        dish.setNameEng(null);
        dishDao.save(dish);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenDishObjectHasNullEngName_whenSaveCalled_thenThrowDataIntegrityViolationException()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        dish.setNameEng(null);
        dishDao.save(dish);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenDishObjectHasNullUkrName_whenSaveCalled_thenThrowDataIntegrityViolationException()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        dish.setNameUkr(null);
        dishDao.save(dish);
    }

    @Test
    public void givenDishObjectHasNullDescription_whenSaveCalled_thenSaveDishToDb()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        dish.setDescriptionEng(null);
        dishDao.save(dish);
        Assert.assertTrue(dishDao.findAll().contains(dish));
    }

    @Test
    public void givenDishObjectHasNegativeId_whenSaveCalled_thenSaveDishToDbAndUpdateId()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int id = -10;
        dish.setId(id);
        dishDao.save(dish);
        Assert.assertNotEquals(id, dish.getId());
        Assert.assertTrue(dish.getId() > 0);
        Assert.assertTrue(dishDao.findAll().contains(dish));
    }

    @Test
    public void givenDishObjectIsNull_whenSaveCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        dishDao.save(null);
        Assert.assertFalse(dishDao.findAll().contains(null));
    }

    @Test
    public void whenUpdateCalled_thenUpdateDish() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        dishDao.save(dish);
        dish.setNameUkr("new name");
        dish.setNameEng("new name");
        dishDao.update(dish);
        Dish updated = dishDao.findById(dish.getId()).get();
        Assert.assertNotNull(updated);
        Assert.assertEquals("new name", updated.getNameEng());
        Assert.assertEquals("new name", updated.getNameUkr());
        Assert.assertEquals(dish.getId(), updated.getId());
        Assert.assertEquals(dish, updated);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenDishHasNullEngName_whenUpdateCalled_thenThrowDataIntegrityViolationException()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        dishDao.save(dish);
        dish.setNameEng(null);
        dishDao.update(dish);
    }

    @Test
    public void givenDishHasNotExistingId_whenUpdateCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        dish.setId(1000);
        dishDao.update(dish);
        Assert.assertFalse(dishDao.findById(dish.getId()).isPresent());
        dish.setId(-10);
        dishDao.update(dish);
        Assert.assertFalse(dishDao.findAll().contains(dish));
    }

    @Test
    public void givenDishObjectIsNull_whenUpdateCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        dishDao.update(null);
        Assert.assertFalse(dishDao.findAll().contains(null));
    }

    @Test
    public void whenDeleteDishByIdCalled_thenDeleteDish() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        dishDao.save(dish);
        int size = dishDao.findAll().size();
        dishDao.deleteById(dish.getId());
        Assert.assertFalse(dishDao.findAll().contains(dish));
        Assert.assertEquals(size - 1, dishDao.findAll().size());
    }

    @Test
    public void givenDishHasNotExistingId_whenDeleteUserByIdCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int size = dishDao.findAll().size();
        dishDao.deleteById(100);
        Assert.assertEquals(size, dishDao.findAll().size());
    }

    @Test
    public void whenDeleteDishCalled_thenDeleteDish() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        dishDao.save(dish);
        int size = dishDao.findAll().size();
        dishDao.delete(dish);
        Assert.assertFalse(dishDao.findAll().contains(dish));
        Assert.assertEquals(size - 1, dishDao.findAll().size());
    }

    @Test
    public void givenDishObjectIsNull_whenDeleteCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int size = dishDao.findAll().size();
        dishDao.delete(null);
        Assert.assertEquals(size, dishDao.findAll().size());
    }

    @After
    public void clearDatabase() {
        dishDao.delete(dish);
    }
}
