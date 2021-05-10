package dao;

import dao.impl.CategoryDaoImpl;
import database.DBManager;
import entities.Category;
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

public class CategoryDaoTest {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/restaurant_test?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private DBManager dbManager;
    private CategoryDao categoryDao;
    private Category category;

    @Before
    public void before() throws SQLException {
        dbManager = mock(DBManager.class);
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        categoryDao = new CategoryDaoImpl(dbManager);
        category = new Category("test", "test");
    }

    @Test
    public void whenFindAllCalled_thenReturnListOfCategories() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        List<Category> categories = categoryDao.findAll();
        Assert.assertNotNull(categories);
    }

    @Test
    public void whenFindByIdCalled_thenReturnCategory() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        categoryDao.save(category);
        int id = category.getId();
        Category category = categoryDao.findById(id).get();
        Assert.assertNotNull(category);
        Assert.assertEquals(id, category.getId());
    }

    @Test
    public void givenCategoryNotExists_whenFindByIdCalled_thenReturnNull() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        Assert.assertFalse(categoryDao.findById(-10).isPresent());
    }

    @Test
    public void whenSaveCalled_thenUpdateCategoryId() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int id = category.getId();
        categoryDao.save(category);
        Assert.assertNotEquals(id, category.getId());
    }

    @Test
    public void whenSaveCalled_thenSaveCategoryToDb() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        categoryDao.save(category);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenCategoryObjectHasNullNames_whenSaveCalled_thenThrowDataIntegrityViolationException()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        category.setNameUkr(null);
        category.setNameEng(null);
        categoryDao.save(category);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenCategoryObjectHasNullEngName_whenSaveCalled_thenThrowDataIntegrityViolationException()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        category.setNameEng(null);
        categoryDao.save(category);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenCategoryObjectHasNullUkrName_whenSaveCalled_thenThrowDataIntegrityViolationException()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        category.setNameUkr(null);
        categoryDao.save(category);
    }

    @Test
    public void givenCategoryObjectHasNegativeId_whenSaveCalled_thenSaveCategoryToDbAndUpdateId()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int id = -10;
        category.setId(id);
        categoryDao.save(category);
        Assert.assertNotEquals(id, category.getId());
        Assert.assertTrue(category.getId() > 0);
        Assert.assertTrue(categoryDao.findAll().contains(category));
    }

    @Test
    public void givenCategoryObjectIsNull_whenSaveCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        categoryDao.save(null);
        Assert.assertFalse(categoryDao.findAll().contains(null));
    }

    @Test
    public void whenUpdateCalled_thenUpdateCategory() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        categoryDao.save(category);
        category.setNameUkr("new name");
        category.setNameEng("new name");
        categoryDao.update(category);
        Category updated = categoryDao.findById(category.getId()).get();
        Assert.assertNotNull(updated);
        Assert.assertEquals("new name", updated.getNameEng());
        Assert.assertEquals("new name", updated.getNameUkr());
        Assert.assertEquals(category.getId(), updated.getId());
        Assert.assertEquals(category, updated);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenCategoryHasNullEngName_whenUpdateCalled_thenThrowDataIntegrityViolationException()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        categoryDao.save(category);
        category.setNameEng(null);
        categoryDao.update(category);
    }

    @Test
    public void givenCategoryHasNotExistingId_whenUpdateCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        category.setId(1000);
        categoryDao.update(category);
        Assert.assertFalse(categoryDao.findById(category.getId()).isPresent());
        category.setId(-10);
        categoryDao.update(category);
        Assert.assertFalse(categoryDao.findAll().contains(category));
    }

    @Test
    public void givenCategoryObjectIsNull_whenUpdateCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        categoryDao.update(null);
        Assert.assertFalse(categoryDao.findAll().contains(null));
    }

    @Test
    public void whenDeleteCategoryByIdCalled_thenDeleteCategory() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        categoryDao.save(category);
        int size = categoryDao.findAll().size();
        categoryDao.deleteById(category.getId());
        Assert.assertFalse(categoryDao.findAll().contains(category));
        Assert.assertEquals(size - 1, categoryDao.findAll().size());
    }

    @Test
    public void givenCategoryHasNotExistingId_whenDeleteUserByIdCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int size = categoryDao.findAll().size();
        categoryDao.deleteById(100);
        Assert.assertEquals(size, categoryDao.findAll().size());
    }

    @Test
    public void whenDeleteCategoryCalled_thenDeleteCategory() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        categoryDao.save(category);
        int size = categoryDao.findAll().size();
        categoryDao.delete(category);
        Assert.assertFalse(categoryDao.findAll().contains(category));
        Assert.assertEquals(size - 1, categoryDao.findAll().size());
    }

    @Test
    public void givenCategoryObjectIsNull_whenDeleteCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int size = categoryDao.findAll().size();
        categoryDao.delete(null);
        Assert.assertEquals(size, categoryDao.findAll().size());
    }

    @After
    public void clearDatabase() {
        categoryDao.delete(category);
    }

}
