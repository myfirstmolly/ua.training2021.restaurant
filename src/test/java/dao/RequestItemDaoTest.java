package dao;

import dao.impl.DishDaoImpl;
import dao.impl.RequestItemDaoImpl;
import database.DBManager;
import entities.Dish;
import entities.RequestItem;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestItemDaoTest {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/restaurant_test?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private RequestItemDao requestItemDao;
    private RequestItem requestItem;

    @Before
    public void before() throws SQLException {
        DBManager dbManager = mock(DBManager.class);
        requestItemDao = new RequestItemDaoImpl(dbManager);
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        DishDao dishDao = new DishDaoImpl(dbManager);
        Dish dish = dishDao.findById(1).get();
        requestItem = new RequestItem(1, dish, 2);
    }

    @Test
    public void whenFindAllCalled_thenReturnListOfRequestItems() {
        List<RequestItem> requestItems = requestItemDao.findAll();
        Assert.assertNotNull(requestItems);
    }

    @Test
    public void whenFindByIdCalled_thenReturnRequestItem() {
        requestItemDao.save(this.requestItem);
        int id = this.requestItem.getId();
        RequestItem requestItemFromDb = requestItemDao.findById(id).get();
        Assert.assertNotNull(requestItemFromDb);
        Assert.assertEquals(id, requestItemFromDb.getId());
        requestItem.setPrice(requestItemFromDb.getPrice());
        Assert.assertEquals(requestItem.toString(), requestItemFromDb.toString());
        Assert.assertEquals(requestItem.hashCode(), requestItemFromDb.hashCode());
    }

    @Test
    public void givenRequestItemNotExists_whenFindByIdCalled_thenReturnNull() {
        Assert.assertFalse(requestItemDao.findById(-10).isPresent());
    }

    @Test
    public void whenFindByRequestIdCalled_thenReturnListOfRequestItems() {
        requestItemDao.save(requestItem);
        List<RequestItem> requestItems = requestItemDao.findAllByRequestId(1);
        Assert.assertNotNull(requestItems);
        Assert.assertTrue(requestItems.contains(requestItem));
    }

    @Test
    public void givenRequestNotExists_whenFindByRequestIdCalled_thenReturnEmptyList() {
        List<RequestItem> requestItems = requestItemDao.findAllByRequestId(100);
        Assert.assertNotNull(requestItems);
        Assert.assertTrue(requestItems.isEmpty());
    }

    @Test
    public void whenSaveCalled_thenUpdateRequestItemId() {
        int id = requestItem.getId();
        requestItemDao.save(requestItem);
        Assert.assertNotEquals(id, requestItem.getId());
    }

    @Test
    public void whenSaveCalled_thenSaveRequestItemToDb() {
        requestItemDao.save(requestItem);
        Assert.assertTrue(requestItemDao.findAll().contains(requestItem));
    }

    @Test
    public void givenRequestItemObjectHasWrongFields_whenSaveCalled_thenThrowDataIntegrityViolationException() {
        requestItem.setDish(null);
        requestItem.setRequestId(-99);
        requestItemDao.save(requestItem);
        Assert.assertFalse(requestItemDao.findById(requestItem.getId()).isPresent());
    }

    @Test
    public void givenRequestItemObjectHasNegativeId_whenSaveCalled_thenSaveRequestItemToDbAndUpdateId() {
        int id = -10;
        requestItem.setId(id);
        requestItemDao.save(requestItem);
        Assert.assertNotEquals(id, requestItem.getId());
        Assert.assertTrue(requestItem.getId() > 0);
        Assert.assertTrue(requestItemDao.findAll().contains(requestItem));
    }

    @Test
    public void givenRequestItemObjectIsNull_whenSaveCalled_thenReturn() {
        requestItemDao.save(null);
        Assert.assertFalse(requestItemDao.findAll().contains(requestItem));
    }

    @Test
    public void whenUpdateCalled_thenUpdateRequestItem() {
        requestItemDao.save(requestItem);
        requestItem.setQuantity(10);
        requestItemDao.update(requestItem);
        RequestItem updated = requestItemDao.findById(requestItem.getId()).get();
        Assert.assertNotNull(updated);
        Assert.assertEquals(10, updated.getQuantity());
        Assert.assertEquals(requestItem.getId(), updated.getId());
        Assert.assertEquals(requestItem, updated);
    }

    @Test
    public void givenRequestItemHasNegativeQuantity_whenUpdateCalled_thenThrowDataIntegrityViolationException() {
        requestItemDao.save(requestItem);
        requestItem.setQuantity(-10);
        requestItemDao.update(requestItem);
        Assert.assertTrue(requestItemDao.findById(requestItem.getId()).get().getQuantity() >= 0);
    }

    @Test
    public void givenRequestItemHasNotExistingId_whenUpdateCalled_thenReturn() {
        requestItem.setId(1000);
        requestItemDao.update(requestItem);
        Assert.assertFalse(requestItemDao.findById(requestItem.getId()).isPresent());
        requestItem.setId(-10);
        requestItemDao.update(requestItem);
        Assert.assertFalse(requestItemDao.findAll().contains(requestItem));
    }

    @Test
    public void givenRequestItemObjectIsNull_whenUpdateCalled_thenReturn() {
        requestItemDao.update(null);
        Assert.assertFalse(requestItemDao.findAll().contains(null));
    }

    @Test
    public void whenDeleteRequestItemByIdCalled_thenDeleteRequestItem() {
        requestItemDao.save(requestItem);
        int size = requestItemDao.findAll().size();
        requestItemDao.deleteById(requestItem.getId());
        Assert.assertFalse(requestItemDao.findAll().contains(requestItem));
        Assert.assertEquals(size - 1, requestItemDao.findAll().size());
    }

    @Test
    public void givenRequestItemHasNotExistingId_whenDeleteUserByIdCalled_thenReturn() {
        int size = requestItemDao.findAll().size();
        requestItemDao.deleteById(100);
        Assert.assertEquals(size, requestItemDao.findAll().size());
    }

    @Test
    public void whenDeleteRequestItemCalled_thenDeleteRequestItem() {
        requestItemDao.save(requestItem);
        int size = requestItemDao.findAll().size();
        requestItemDao.delete(requestItem);
        Assert.assertFalse(requestItemDao.findAll().contains(requestItem));
        Assert.assertEquals(size - 1, requestItemDao.findAll().size());
    }

    @Test
    public void givenRequestItemObjectIsNull_whenDeleteCalled_thenReturn() {
        int size = requestItemDao.findAll().size();
        requestItemDao.delete(null);
        Assert.assertEquals(size, requestItemDao.findAll().size());
    }

    @After
    public void clearDatabase() {
        requestItemDao.delete(requestItem);
    }

}
