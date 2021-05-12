package dao;

import dao.impl.RequestDaoImpl;
import dao.impl.UserDaoImpl;
import database.DBManager;
import entities.Request;
import entities.Status;
import entities.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestDaoTest {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/restaurant_test?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "password";
    private RequestDao requestDao;
    private Request request;
    private List<Request> requests;

    @Before
    public void before() throws SQLException {
        DBManager dbManager = mock(DBManager.class);
        when(dbManager.getConnection()).thenReturn(DriverManager.getConnection(DB_URL, USERNAME, PASSWORD));
        requestDao = new RequestDaoImpl(dbManager);
        UserDao userDao = new UserDaoImpl(dbManager);
        User user = userDao.findById(1).get();
        request = new Request(user, Status.OPENED);
        requests = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Request temp = new Request(user, Status.OPENED);
            requestDao.save(temp);
            requests.add(temp);
        }
    }

    @Test
    public void whenFindAllCalled_thenReturnListOfRequests() {
        List<Request> requests = requestDao.findAll();
        Assert.assertNotNull(requests);
    }

    @Test
    public void givenLimit5_whenFindAllCalled_thenReturnListOf5OrLessRequests() {
        List<Request> requests = requestDao.findAll(5, 0);
        Assert.assertNotNull(requests);
        Assert.assertTrue(requests.size() <= 5);
    }

    @Test
    public void given11RequestsLimit5Offset11_whenFindAllCalled_thenReturnEmptyList() {
        List<Request> requests = requestDao.findAll(5, 11);
        Assert.assertNotNull(requests);
        Assert.assertTrue(requests.isEmpty());
    }

    @Test
    public void whenFindByIdCalled_thenReturnRequest() {
        requestDao.save(request);
        int id = request.getId();
        Request requestFromDb = requestDao.findById(id).get();
        Assert.assertNotNull(requestFromDb);
        Assert.assertEquals(id, requestFromDb.getId());
        Assert.assertEquals(request.toString(), requestFromDb.toString());
        Assert.assertEquals(request.hashCode(), requestFromDb.hashCode());
    }

    @Test
    public void givenRequestNotExists_whenFindByIdCalled_thenReturnNull() {
        Assert.assertFalse(requestDao.findById(-10).isPresent());
    }

    @Test
    public void whenFindByRequestIdCalled_thenReturnListOfRequests() {
        requestDao.save(request);
        List<Request> requests = requestDao.findAllByUserId(request.getCustomer().getId(), 100, 0);
        Assert.assertNotNull(requests);
        Assert.assertTrue(requests.contains(request));
    }

    @Test
    public void givenRequestIdIsNegative_whenFindByUserIdCalled_thenReturnEmptyList() {
        requestDao.save(request);
        List<Request> requests = requestDao.findAllByUserId(-100, 100, 0);
        Assert.assertNotNull(requests);
        Assert.assertTrue(requests.isEmpty());
    }

    @Test
    public void givenRequestNotExists_whenFindByUserIdCalled_thenReturnEmptyList() {
        requestDao.save(request);
        List<Request> requests = requestDao.findAllByUserId(100, 100, 0);
        Assert.assertNotNull(requests);
        Assert.assertTrue(requests.isEmpty());
    }

    @Test
    public void whenFindByStatusIdCalled_thenReturnListOfRequests() {
        requestDao.save(request);
        List<Request> requests = requestDao.findAllByStatusId(request.getStatus().toInt(), 100, 0);
        Assert.assertNotNull(requests);
        Assert.assertTrue(requests.contains(request));
    }

    @Test
    public void givenStatusIdIsNegative_whenFindByStatusIdCalled_thenReturnEmptyList() {
        requestDao.save(request);
        List<Request> requests = requestDao.findAllByStatusId(-100, 100, 0);
        Assert.assertNotNull(requests);
        Assert.assertTrue(requests.isEmpty());
    }

    @Test
    public void givenStatusNotExists_whenFindByStatusIdCalled_thenReturnEmptyList() {
        requestDao.save(request);
        List<Request> requests = requestDao.findAllByStatusId(100, 100, 0);
        Assert.assertNotNull(requests);
        Assert.assertTrue(requests.isEmpty());
    }

    @Test
    public void whenFindByUserAndStatusIdCalled_thenReturnListOfRequests() {
        requestDao.save(request);
        List<Request> requests = requestDao.findAllByUserAndStatus
                (request.getCustomer().getId(), request.getStatus().toInt());
        Assert.assertNotNull(requests);
        Assert.assertTrue(requests.contains(request));
    }

    @Test
    public void givenStatusNotExists_whenFindByUserAndStatusIdCalled_thenReturnEmptyList() {
        requestDao.save(request);
        List<Request> requests = requestDao.findAllByUserAndStatus
                (request.getCustomer().getId(), 100);
        Assert.assertNotNull(requests);
        Assert.assertTrue(requests.isEmpty());
    }

    @Test
    public void givenUserNotExists_whenFindByUserAndStatusIdCalled_thenReturnEmptyList() {
        requestDao.save(request);
        List<Request> requests = requestDao.findAllByUserAndStatus
                (100, request.getStatus().toInt());
        Assert.assertNotNull(requests);
        Assert.assertTrue(requests.isEmpty());
    }

    @Test
    public void givenUserAndStatusNotExist_whenFindByUserAndStatusIdCalled_thenReturnEmptyList() {
        requestDao.save(request);
        List<Request> requests = requestDao.findAllByUserAndStatus
                (100, 100);
        Assert.assertNotNull(requests);
        Assert.assertTrue(requests.isEmpty());
    }

    @Test
    public void whenSaveCalled_thenUpdateRequestId() {
        int id = request.getId();
        requestDao.save(request);
        Assert.assertNotEquals(id, request.getId());
        Assert.assertNotNull(requestDao.findById(request.getId()));
        Assert.assertTrue(requestDao.findAll().contains(request));
    }

    @Test
    public void whenSaveCalled_thenSaveRequestToDb() {
        requestDao.save(request);
        Assert.assertNotNull(requestDao.findById(request.getId()));
        Assert.assertTrue(requestDao.findAll().contains(request));
    }

    @Test
    public void givenRequestObjectHasNullFields_whenSaveCalled_thenThrowDataIntegrityViolationException() {
        request.setCustomer(null);
        request.setDeliveryAddress(null);
        request.setStatus(null);
        requestDao.save(request);
        Assert.assertFalse(requestDao.findById(request.getId()).isPresent());
    }

    @Test
    public void givenRequestObjectHasNullApprovedBy_whenSaveCalled_thenSaveRequestToDb() {
        request.setApprovedBy(null);
        requestDao.save(request);
        Assert.assertNotNull(requestDao.findById(request.getId()));
        Assert.assertTrue(requestDao.findAll().contains(request));
    }

    @Test
    public void givenRequestObjectHasNegativeId_whenSaveCalled_thenSaveRequestToDbAndUpdateId() {
        int id = -10;
        request.setId(id);
        requestDao.save(request);
        Assert.assertNotEquals(id, request.getId());
        Assert.assertTrue(request.getId() > 0);
        Assert.assertTrue(requestDao.findAll().contains(request));
    }

    @Test
    public void givenRequestObjectIsNull_whenSaveCalled_thenReturn() {
        requestDao.save(null);
        Assert.assertFalse(requestDao.findAll().contains(request));
    }

    @Test
    public void whenUpdateCalled_thenUpdateRequest() {
        requestDao.save(request);
        request.setDeliveryAddress("new address");
        requestDao.update(request);
        Request updated = requestDao.findById(request.getId()).get();
        Assert.assertNotNull(updated);
        Assert.assertEquals("new address", updated.getDeliveryAddress());
        Assert.assertEquals(request.getId(), updated.getId());
        Assert.assertEquals(request, updated);
    }

    @Test
    public void givenRequestObjectHasNullFields_whenUpdateCalled_thenReturn() {
        requestDao.save(request);
        request.setDeliveryAddress(null);
        request.setCustomer(null);
        request.setStatus(null);
        requestDao.update(request);
        Request r = requestDao.findById(request.getId()).get();
        Assert.assertNull(r.getDeliveryAddress());
        Assert.assertNotNull(r.getCustomer());
        Assert.assertNotNull(r.getStatus());
    }

    @Test
    public void givenRequestObjectHasNotExistingId_whenUpdateCalled_thenReturn() {
        request.setId(1000);
        requestDao.update(request);
        Assert.assertFalse(requestDao.findById(request.getId()).isPresent());
        request.setId(-10);
        requestDao.update(request);
        Assert.assertFalse(requestDao.findAll().contains(request));
    }

    @Test
    public void givenRequestObjectIsNull_whenUpdateCalled_thenReturn() {
        requestDao.update(null);
        Assert.assertFalse(requestDao.findAll().contains(null));
    }

    @Test
    public void whenDeleteRequestByIdCalled_thenDeleteRequest() {
        requestDao.save(request);
        int size = requestDao.findAll().size();
        requestDao.deleteById(request.getId());
        Assert.assertFalse(requestDao.findAll().contains(request));
        Assert.assertEquals(size - 1, requestDao.findAll().size());
    }

    @Test
    public void givenRequestObjectHasNotExistingId_whenDeleteRequestByIdCalled_thenReturn() {
        int size = requestDao.findAll().size();
        requestDao.deleteById(100);
        Assert.assertEquals(size, requestDao.findAll().size());
    }

    @Test
    public void whenDeleteRequestCalled_thenDeleteRequest() {
        requestDao.save(request);
        int size = requestDao.findAll().size();
        requestDao.delete(request);
        Assert.assertFalse(requestDao.findAll().contains(request));
        Assert.assertEquals(size - 1, requestDao.findAll().size());
    }

    @Test
    public void givenRequestObjectIsNull_whenDeleteRequestCalled_thenReturn() {
        int size = requestDao.findAll().size();
        requestDao.delete(null);
        Assert.assertEquals(size, requestDao.findAll().size());
    }

    @After
    public void clearDatabase() {
        requestDao.delete(request);
        for (Request r : requests)
            requestDao.delete(r);
    }
}
