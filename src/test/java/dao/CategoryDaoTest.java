package dao;

import dao.impl.CategoryDaoImpl;
import database.DBManager;
import entities.Category;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CategoryDaoTest {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/restaurant_test?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private CategoryDao categoryDao;
    private Category category;
    private final DBManager dbManager = mock(DBManager.class);

    @Before
    public void before() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        categoryDao = new CategoryDaoImpl(dbManager);
        category = new Category("test");
    }

    @Test
    public void whenFindAllCalled_thenReturnListOfCategories() {
        List<Category> categories = categoryDao.findAll();
        Assert.assertNotNull(categories);
    }

    @Test
    public void whenFindByIdCalled_thenReturnCategory() throws SQLException {
        categoryDao.save(category);
        int id = category.getId();
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        Category categoryFromDb = categoryDao.findById(id).get();
        Assert.assertNotNull(categoryFromDb);
        Assert.assertEquals(id, categoryFromDb.getId());
        Assert.assertEquals(category.toString(), categoryFromDb.toString());
        Assert.assertEquals(category.hashCode(), categoryFromDb.hashCode());
    }

    @Test
    public void givenCategoryNotExists_whenFindByIdCalled_thenReturnNull() {
        Assert.assertFalse(categoryDao.findById(-10).isPresent());
    }

    @Test
    public void whenSaveCalled_thenUpdateCategoryId() {
        int id = category.getId();
        categoryDao.save(category);
        Assert.assertNotEquals(id, category.getId());
    }

    @Test
    public void whenSaveCalled_thenSaveCategoryToDb() {
        categoryDao.save(category);
    }

    @Test
    public void givenCategoryObjectHasNullNames_whenSaveCalled_thenReturn() {
        category.setName(null);
        categoryDao.save(category);
        Assert.assertFalse(categoryDao.findById(category.getId()).isPresent());
    }

    @Test
    public void givenCategoryObjectHasNullName_whenSaveCalled_thenReturn() {
        category.setName(null);
        categoryDao.save(category);
        Assert.assertFalse(categoryDao.findById(category.getId()).isPresent());
    }

    @Test
    public void givenCategoryObjectHasNegativeId_whenSaveCalled_thenSaveCategoryToDbAndUpdateId() {
        int id = -10;
        category.setId(id);
        categoryDao.save(category);
        Assert.assertNotEquals(id, category.getId());
        Assert.assertTrue(category.getId() > 0);
        Assert.assertTrue(categoryDao.findAll().contains(category));
    }

    @Test
    public void givenCategoryObjectIsNull_whenSaveCalled_thenReturn() {
        categoryDao.save(null);
        Assert.assertFalse(categoryDao.findAll().contains(null));
    }

    @Test
    public void whenUpdateCalled_thenUpdateCategory() {
        categoryDao.save(category);
        category.setName("new name");
        categoryDao.update(category);
        Category updated = categoryDao.findById(category.getId()).get();
        Assert.assertNotNull(updated);
        Assert.assertEquals("new name", updated.getName());
        Assert.assertEquals(category.getId(), updated.getId());
        Assert.assertEquals(category, updated);
    }

    @Test
    public void givenCategoryHasNullName_whenUpdateCalled_thenReturn() {
        categoryDao.save(category);
        category.setName(null);
        categoryDao.update(category);
        Assert.assertNotNull(categoryDao.findById(category.getId()).get().getName());
    }

    @Test
    public void givenCategoryHasNotExistingId_whenUpdateCalled_thenReturn() {
        category.setId(1000);
        categoryDao.update(category);
        Assert.assertFalse(categoryDao.findById(category.getId()).isPresent());
        category.setId(-10);
        categoryDao.update(category);
        Assert.assertFalse(categoryDao.findAll().contains(category));
    }

    @Test
    public void givenCategoryObjectIsNull_whenUpdateCalled_thenReturn() {
        categoryDao.update(null);
        Assert.assertFalse(categoryDao.findAll().contains(null));
    }

    @Test
    public void whenDeleteCategoryByIdCalled_thenDeleteCategory() {
        categoryDao.save(category);
        int size = categoryDao.findAll().size();
        categoryDao.deleteById(category.getId());
        Assert.assertFalse(categoryDao.findAll().contains(category));
        Assert.assertEquals(size - 1, categoryDao.findAll().size());
    }

    @Test
    public void givenCategoryHasNotExistingId_whenDeleteUserByIdCalled_thenReturn() {
        int size = categoryDao.findAll().size();
        categoryDao.deleteById(100);
        Assert.assertEquals(size, categoryDao.findAll().size());
    }

    @Test
    public void whenDeleteCategoryCalled_thenDeleteCategory() {
        categoryDao.save(category);
        int size = categoryDao.findAll().size();
        categoryDao.delete(category);
        Assert.assertFalse(categoryDao.findAll().contains(category));
        Assert.assertEquals(size - 1, categoryDao.findAll().size());
    }

    @Test
    public void givenCategoryObjectIsNull_whenDeleteCalled_thenReturn() {
        int size = categoryDao.findAll().size();
        categoryDao.delete(null);
        Assert.assertEquals(size, categoryDao.findAll().size());
    }

    @After
    public void clearDatabase() {
        categoryDao.delete(category);
    }

}
