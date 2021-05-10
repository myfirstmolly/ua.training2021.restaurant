package dao;

import dao.impl.RequestDaoImpl;
import dao.impl.UserDaoImpl;
import database.DBManager;
import entities.Request;
import entities.Status;
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

//TODO: add tests for find by user and status id
public class RequestDaoTest {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/restaurant_test?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private DBManager dbManager;
    private RequestDao requestDao;
    private Request request;

    @Before
    public void before() throws SQLException {
        dbManager = mock(DBManager.class);
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        requestDao = new RequestDaoImpl(dbManager);
        UserDao userDao = new UserDaoImpl(dbManager);
        User user = userDao.findById(1).get();
        request = new Request(user, Status.OPENED, "test");
    }

    @Test
    public void whenFindAllCalled_thenReturnListOfRequests() throws SQLException {
        List<Request> requests = requestDao.findAll();
        Assert.assertNotNull(requests);
    }

    @Test
    public void whenFindByIdCalled_thenReturnRequest() throws SQLException {
        requestDao.save(request);
        int id = request.getId();
        Request request = requestDao.findById(id).get();
        Assert.assertNotNull(request);
        Assert.assertEquals(id, request.getId());
    }

    @Test
    public void givenRequestNotExists_whenFindByIdCalled_thenReturnNull() throws SQLException {
        Assert.assertFalse(requestDao.findById(-10).isPresent());
    }

    @Test
    public void whenSaveCalled_thenUpdateRequestId() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int id = request.getId();
        requestDao.save(request);
        Assert.assertNotEquals(id, request.getId());
        Assert.assertNotNull(requestDao.findById(request.getId()));
        Assert.assertTrue(requestDao.findAll().contains(request));
    }

    @Test
    public void whenSaveCalled_thenSaveRequestToDb() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        requestDao.save(request);
        Assert.assertNotNull(requestDao.findById(request.getId()));
        Assert.assertTrue(requestDao.findAll().contains(request));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenRequestObjectHasNullDeliveryAddress_whenSaveCalled_thenThrowDataIntegrityViolationException()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        request.setDeliveryAddress(null);
        requestDao.save(request);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenRequestObjectHasNullFields_whenSaveCalled_thenThrowDataIntegrityViolationException()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        request.setCustomer(null);
        request.setDeliveryAddress(null);
        request.setStatus(null);
        requestDao.save(request);
    }

    @Test
    public void givenRequestObjectHasNullApprovedBy_whenSaveCalled_thenSaveRequestToDb() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        request.setApprovedBy(null);
        requestDao.save(request);
        Assert.assertNotNull(requestDao.findById(request.getId()));
        Assert.assertTrue(requestDao.findAll().contains(request));
    }

    @Test
    public void givenRequestObjectHasNegativeId_whenSaveCalled_thenSaveRequestToDbAndUpdateId() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int id = -10;
        request.setId(id);
        requestDao.save(request);
        Assert.assertNotEquals(id, request.getId());
        Assert.assertTrue(request.getId() > 0);
        Assert.assertTrue(requestDao.findAll().contains(request));
    }

    @Test
    public void givenRequestObjectIsNull_whenSaveCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        requestDao.save(null);
        Assert.assertFalse(requestDao.findAll().contains(request));
    }

    @Test
    public void whenUpdateCalled_thenUpdateRequest() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        requestDao.save(request);
        request.setDeliveryAddress("new address");
        requestDao.update(request);
        Request updated = requestDao.findById(request.getId()).get();
        Assert.assertNotNull(updated);
        Assert.assertEquals("new address", updated.getDeliveryAddress());
        Assert.assertEquals(request.getId(), updated.getId());
        Assert.assertEquals(request, updated);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenRequestObjectHasNullFields_whenUpdateCalled_thenThrowDataIntegrityViolationException()
            throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        request.setDeliveryAddress(null);
        request.setCustomer(null);
        request.setStatus(null);
        requestDao.update(request);
    }

    @Test
    public void givenRequestObjectHasNotExistingId_whenUpdateCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        request.setId(1000);
        requestDao.update(request);
        Assert.assertFalse(requestDao.findById(request.getId()).isPresent());
        request.setId(-10);
        requestDao.update(request);
        Assert.assertFalse(requestDao.findAll().contains(request));
    }

    @Test
    public void givenRequestObjectIsNull_whenUpdateCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        requestDao.update(null);
        Assert.assertFalse(requestDao.findAll().contains(null));
    }

    @Test
    public void whenDeleteRequestByIdCalled_thenDeleteRequest() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        requestDao.save(request);
        int size = requestDao.findAll().size();
        requestDao.deleteById(request.getId());
        Assert.assertFalse(requestDao.findAll().contains(request));
        Assert.assertEquals(size - 1, requestDao.findAll().size());
    }

    @Test
    public void givenRequestObjectHasNotExistingId_whenDeleteRequestByIdCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int size = requestDao.findAll().size();
        requestDao.deleteById(100);
        Assert.assertEquals(size, requestDao.findAll().size());
    }

    @Test
    public void whenDeleteRequestCalled_thenDeleteRequest() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        requestDao.save(request);
        int size = requestDao.findAll().size();
        requestDao.delete(request);
        Assert.assertFalse(requestDao.findAll().contains(request));
        Assert.assertEquals(size - 1, requestDao.findAll().size());
    }

    @Test
    public void givenRequestObjectIsNull_whenDeleteRequestCalled_thenReturn() throws SQLException {
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        int size = requestDao.findAll().size();
        requestDao.delete(null);
        Assert.assertEquals(size, requestDao.findAll().size());
    }

    @After
    public void clearDatabase() {
        requestDao.delete(request);
    }
}
