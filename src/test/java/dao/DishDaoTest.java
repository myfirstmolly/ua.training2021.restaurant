package dao;

import dao.impl.CategoryDaoImpl;
import dao.impl.DishDaoImpl;
import database.DBManager;
import entities.Category;
import entities.Dish;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DishDaoTest {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/restaurant_test?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private final Connection[] connectionPool = new Connection[10];
    private DishDao dishDao;
    private Dish dish;
    private List<Dish> dishes;

    @Before
    public void before() throws SQLException {
        DBManager dbManager = mock(DBManager.class);
        for (int i = 0; i < connectionPool.length; i++) {
            connectionPool[i] = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        }
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD), connectionPool);
        dishDao = new DishDaoImpl(dbManager);
        CategoryDao categoryDao = new CategoryDaoImpl(dbManager);
        Category category = categoryDao.findById(1).get();
        dish = new Dish("test", 8000,
                "test", "test",
                category);
        String name = dish.getName();
        dishes = new ArrayList<>();
        Dish temp = new Dish(null, 8000,
                "test", "test",
                category);

        for (int i = 0; i < 8; i++) {
            temp.setName(name + i);
            temp.setCategory(categoryDao.findById(i + 1).get());
            dishDao.save(temp);
            dishes.add(dishDao.findById(temp.getId()).get());
        }
    }

    @Test
    public void whenFindAllCalled_thenReturnListOfDishes() {
        List<Dish> dishes = dishDao.findAll();
        Assert.assertNotNull(dishes);
    }

    @Test
    public void givenLimitIs5_whenFindAllCalled_thenReturnListOf5OrLessDishes() {
        List<Dish> dishes = dishDao.findAll(5, 1).getContent();
        Assert.assertNotNull(dishes);
        Assert.assertTrue(dishes.size() <= 5);
    }

    @Test
    public void given11DishesLimit6Index3_whenFindAllCalled_thenReturnEmptyList() {
        List<Dish> dishes = dishDao.findAll(6, 3).getContent();
        Assert.assertNotNull(dishes);
        Assert.assertTrue(dishes.isEmpty());
    }

    @Test
    public void given9DishesLimit6Index2_whenFindAllCalled_thenReturnListOf3Elements() {
        List<Dish> dishes = dishDao.findAll(6, 2).getContent();
        Assert.assertNotNull(dishes);
        Assert.assertEquals(3, dishes.size());
    }

    @Test
    public void whenFindByIdCalled_thenReturnDish() {
        dishDao.save(dish);
        int id = dish.getId();
        Dish dishFromDb = dishDao.findById(id).get();
        Assert.assertNotNull(dishFromDb);
        Assert.assertEquals(id, dishFromDb.getId());
        Assert.assertEquals(dish.toString(), dishFromDb.toString());
        Assert.assertEquals(dish.hashCode(), dishFromDb.hashCode());
    }

    @Test
    public void givenDishNotExists_whenFindByIdCalled_thenReturnNull() {
        Assert.assertFalse(dishDao.findById(-10).isPresent());
    }

    @Test
    public void whenFindByCategoryIdCalled_thenReturnListOfDishes() {
        dishDao.save(dish);
        List<Dish> dishes = dishDao.findAllByCategoryId(dish.getCategory().getId(), 100, 1).getContent();
        Assert.assertNotNull(dishes);
        Assert.assertTrue(dishes.contains(dish));
    }

    @Test
    public void givenLimitIs5_whenFindByCategoryIdCalled_thenReturnListOf5OrLessDishes() {
        Dish template = new Dish("test", 8000,
                "test", "test",
                dish.getCategory());
        List<Dish> dishes = dishDao.findAllByCategoryId(template.getCategory().getId(), 5, 1).getContent();
        Assert.assertNotNull(dishes);
        Assert.assertTrue(dishes.size() <= 5);
    }

    @Test
    public void givenLimitIs5_whenFindSortedByNameEn_thenReturnListSortedByCategories() {
        List<Dish> dishes = dishDao.findAllSortedByName(5, 1).getContent();
        Assert.assertNotNull(dishes);
        Assert.assertEquals(5, dishes.size());

        List<Dish> actual = dishDao.findAll();
        actual.sort(Comparator.comparing(Dish::getName));

        for (int i = 0; i < dishes.size(); i++) {
            Assert.assertEquals(actual.get(i), dishes.get(i));
        }
    }

    @Test
    public void givenLimitIs5_whenFindSortedByPrice_thenReturnListSortedByCategories() {
        List<Dish> dishes = dishDao.findAllSortedByPrice(5, 1).getContent();
        Assert.assertNotNull(dishes);
        Assert.assertEquals(5, dishes.size());

        List<Dish> actual = dishDao.findAll();
        actual.sort(Comparator.comparing(Dish::getPrice));

        for (int i = 0; i < dishes.size(); i++) {
            Assert.assertEquals(actual.get(i), dishes.get(i));
        }
    }

    @Test
    public void givenLimitIs5_whenFindSortedByCategoryEn_thenReturnListSortedByCategories() {
        List<Dish> dishes = dishDao.findAllSortedByCategory(5, 1).getContent();
        Assert.assertNotNull(dishes);
        Assert.assertEquals(5, dishes.size());

        List<Dish> actual = dishDao.findAll();
        actual.sort(Comparator.comparing(d -> d.getCategory().getName()));

        for (int i = 0; i < dishes.size(); i++) {
            Assert.assertEquals(actual.get(i), dishes.get(i));
        }
    }

    @Test
    public void givenCategoryNotExists_whenFindByCategoryIdCalled_thenReturnEmptyList() {
        List<Dish> dishes = dishDao.findAllByCategoryId(100, 100, 1).getContent();
        Assert.assertNotNull(dishes);
        Assert.assertTrue(dishes.isEmpty());
    }

    @Test
    public void whenSaveCalled_thenUpdateDishId() {
        int id = dish.getId();
        dishDao.save(dish);
        Assert.assertNotEquals(id, dish.getId());
    }

    @Test
    public void whenSaveCalled_thenSaveDishToDb() {
        dishDao.save(dish);
    }

    @Test
    public void givenDishObjectHasNullNames_whenSaveCalled_thenReturn() {
        dish.setName(null);
        dishDao.save(dish);
        Assert.assertFalse(dishDao.findById(dish.getId()).isPresent());
    }

    @Test
    public void givenDishObjectHasNullDescription_whenSaveCalled_thenSaveDishToDb() {
        dish.setDescription(null);
        dishDao.save(dish);
        Assert.assertNull(dishDao.findById(dish.getId()).get().getDescription());
    }

    @Test
    public void givenDishObjectHasNegativeId_whenSaveCalled_thenSaveDishToDbAndUpdateId() {
        int id = -10;
        dish.setId(id);
        dishDao.save(dish);
        Assert.assertNotEquals(id, dish.getId());
        Assert.assertTrue(dish.getId() > 0);
        Assert.assertTrue(dishDao.findAll().contains(dish));
    }

    @Test
    public void givenDishObjectIsNull_whenSaveCalled_thenReturn() {
        dishDao.save(null);
        Assert.assertFalse(dishDao.findAll().contains(null));
    }

    @Test
    public void whenUpdateCalled_thenUpdateDish() {
        dishDao.save(dish);
        dish.setName("new name");
        dishDao.update(dish);
        Dish updated = dishDao.findById(dish.getId()).get();
        Assert.assertNotNull(updated);
        Assert.assertEquals("new name", updated.getName());
        Assert.assertEquals(dish.getId(), updated.getId());
        Assert.assertEquals(dish, updated);
    }

    @Test
    public void givenDishHasNullEngName_whenUpdateCalled_thenReturn() {
        dishDao.save(dish);
        dish.setName(null);
        dishDao.update(dish);
        Assert.assertNotNull(dishDao.findById(dish.getId()).get().getName());
    }

    @Test
    public void givenDishHasNotExistingId_whenUpdateCalled_thenReturn() {
        dish.setId(1000);
        dishDao.update(dish);
        Assert.assertFalse(dishDao.findById(dish.getId()).isPresent());
        dish.setId(-10);
        dishDao.update(dish);
        Assert.assertFalse(dishDao.findAll().contains(dish));
    }

    @Test
    public void givenDishObjectIsNull_whenUpdateCalled_thenReturn() {
        dishDao.update(null);
        Assert.assertFalse(dishDao.findAll().contains(null));
    }

    @Test
    public void whenDeleteDishByIdCalled_thenDeleteDish() {
        dishDao.save(dish);
        int size = dishDao.findAll().size();
        dishDao.deleteById(dish.getId());
        Assert.assertFalse(dishDao.findAll().contains(dish));
        Assert.assertEquals(size - 1, dishDao.findAll().size());
    }

    @Test
    public void givenDishHasNotExistingId_whenDeleteUserByIdCalled_thenReturn() {
        int size = dishDao.findAll().size();
        dishDao.deleteById(100);
        Assert.assertEquals(size, dishDao.findAll().size());
    }

    @Test
    public void whenDeleteDishCalled_thenDeleteDish() {
        dishDao.save(dish);
        int size = dishDao.findAll().size();
        dishDao.delete(dish);
        Assert.assertFalse(dishDao.findAll().contains(dish));
        Assert.assertEquals(size - 1, dishDao.findAll().size());
    }

    @Test
    public void givenDishObjectIsNull_whenDeleteCalled_thenReturn() {
        int size = dishDao.findAll().size();
        dishDao.delete(null);
        Assert.assertEquals(size, dishDao.findAll().size());
    }

    @After
    public void clearDatabase() throws SQLException {
        dishDao.delete(dish);
        for (Dish dish : dishes)
            dishDao.delete(dish);
        for (Connection c : connectionPool) {
            c.close();
        }
    }
}
