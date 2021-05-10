package dao;

import dao.impl.BillItemDaoImpl;
import dao.impl.DishDaoImpl;
import database.DBManager;
import entities.Bill;
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

public class BillDaoTest {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/restaurant_test?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private DBManager dbManager;
    private BillItemDao billItemDao;
    private Bill bill;

    @Before
    public void before() throws SQLException {
        dbManager = mock(DBManager.class);
        billItemDao = new BillItemDaoImpl(dbManager);
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        DishDao dishDao = new DishDaoImpl(dbManager);
        Dish dish = dishDao.findById(1).get();
        bill = new Bill(1, dish, 2);
    }

    @Test
    public void whenFindAllCalled_thenReturnListOfBillItems() {
        List<Bill> bills = billItemDao.findAll();
        Assert.assertNotNull(bills);
    }

    @Test
    public void whenFindByIdCalled_thenReturnBillItem() {
        billItemDao.save(this.bill);
        int id = this.bill.getId();
        Bill bill = billItemDao.findById(id).get();
        Assert.assertNotNull(bill);
        Assert.assertEquals(id, bill.getId());
    }

    @Test
    public void givenBillItemNotExists_whenFindByIdCalled_thenReturnNull() {
        Assert.assertFalse(billItemDao.findById(-10).isPresent());
    }

    @Test
    public void whenFindByRequestIdCalled_thenReturnListOfBillItems() {
        billItemDao.save(bill);
        List<Bill> bills = billItemDao.findAllByRequestId(1);
        Assert.assertNotNull(bills);
        Assert.assertTrue(bills.contains(bill));
    }

    @Test
    public void givenRequestNotExists_whenFindByRequestIdCalled_thenReturnEmptyList() {
        List<Bill> bills = billItemDao.findAllByRequestId(100);
        Assert.assertNotNull(bills);
        Assert.assertTrue(bills.isEmpty());
    }

    @Test
    public void whenSaveCalled_thenUpdateBillItemId() {
        int id = bill.getId();
        billItemDao.save(bill);
        Assert.assertNotEquals(id, bill.getId());
    }

    @Test
    public void whenSaveCalled_thenSaveBillItemToDb() {
        billItemDao.save(bill);
        Assert.assertTrue(billItemDao.findAll().contains(bill));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenBillItemObjectHasNegativeQuantity_whenSaveCalled_thenThrowDataIntegrityViolationException() {
        bill.setQuantity(-10);
        billItemDao.save(bill);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenBillItemObjectHasWrongFields_whenSaveCalled_thenThrowDataIntegrityViolationException() {
        bill.setDish(null);
        bill.setRequestId(-99);
        billItemDao.save(bill);
    }

    @Test
    public void givenBillItemObjectHasNegativeId_whenSaveCalled_thenSaveBillItemToDbAndUpdateId() {
        int id = -10;
        bill.setId(id);
        billItemDao.save(bill);
        Assert.assertNotEquals(id, bill.getId());
        Assert.assertTrue(bill.getId() > 0);
        Assert.assertTrue(billItemDao.findAll().contains(bill));
    }

    @Test
    public void givenBillItemObjectIsNull_whenSaveCalled_thenReturn() {
        billItemDao.save(null);
        Assert.assertFalse(billItemDao.findAll().contains(bill));
    }

    @Test
    public void whenUpdateCalled_thenUpdateBillItem() {
        billItemDao.save(bill);
        bill.setQuantity(10);
        billItemDao.update(bill);
        Bill updated = billItemDao.findById(bill.getId()).get();
        Assert.assertNotNull(updated);
        Assert.assertEquals(10, updated.getQuantity());
        Assert.assertEquals(bill.getId(), updated.getId());
        Assert.assertEquals(bill, updated);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenBillItemHasNegativeQuantity_whenUpdateCalled_thenThrowDataIntegrityViolationException() {
        billItemDao.save(bill);
        bill.setQuantity(-10);
        billItemDao.update(bill);
    }

    @Test
    public void givenBillItemHasNotExistingId_whenUpdateCalled_thenReturn() {
        bill.setId(1000);
        billItemDao.update(bill);
        Assert.assertFalse(billItemDao.findById(bill.getId()).isPresent());
        bill.setId(-10);
        billItemDao.update(bill);
        Assert.assertFalse(billItemDao.findAll().contains(bill));
    }

    @Test
    public void givenBillItemObjectIsNull_whenUpdateCalled_thenReturn() {
        billItemDao.update(null);
        Assert.assertFalse(billItemDao.findAll().contains(null));
    }

    @Test
    public void whenDeleteBillItemByIdCalled_thenDeleteBillItem() {
        billItemDao.save(bill);
        int size = billItemDao.findAll().size();
        billItemDao.deleteById(bill.getId());
        Assert.assertFalse(billItemDao.findAll().contains(bill));
        Assert.assertEquals(size - 1, billItemDao.findAll().size());
    }

    @Test
    public void givenBillItemHasNotExistingId_whenDeleteUserByIdCalled_thenReturn() {
        int size = billItemDao.findAll().size();
        billItemDao.deleteById(100);
        Assert.assertEquals(size, billItemDao.findAll().size());
    }

    @Test
    public void whenDeleteBillItemCalled_thenDeleteBillItem() {
        billItemDao.save(bill);
        int size = billItemDao.findAll().size();
        billItemDao.delete(bill);
        Assert.assertFalse(billItemDao.findAll().contains(bill));
        Assert.assertEquals(size - 1, billItemDao.findAll().size());
    }

    @Test
    public void givenBillItemObjectIsNull_whenDeleteCalled_thenReturn() {
        int size = billItemDao.findAll().size();
        billItemDao.delete(null);
        Assert.assertEquals(size, billItemDao.findAll().size());
    }

    @After
    public void clearDatabase() {
        billItemDao.delete(bill);
    }

}
