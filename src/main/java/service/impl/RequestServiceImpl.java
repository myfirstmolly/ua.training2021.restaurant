package service.impl;

import dao.RequestDao;
import dao.RequestItemDao;
import dao.impl.RequestDaoImpl;
import dao.impl.RequestItemDaoImpl;
import database.DBManager;
import entities.*;
import exceptions.ObjectNotFoundException;
import service.RequestService;
import util.Page;

import java.util.List;

public class RequestServiceImpl implements RequestService {

    private final int LIMIT = 18;
    private final RequestDao requestDao;
    private final RequestItemDao requestItemDao;

    public RequestServiceImpl(DBManager dbManager) {
        requestDao = new RequestDaoImpl(dbManager);
        requestItemDao = new RequestItemDaoImpl(dbManager);
    }

    @Override
    public Page<Request> findAllByCustomerId(int id, int page) {
        return requestDao.findAllByUserId(id, LIMIT, LIMIT * (page - 1));
    }

    @Override
    public Page<Request> findAllByStatus(int id, int page) {
        return requestDao.findAllByStatusId(id, LIMIT, page);
    }

    @Override
    public Request findById(int id) {
        return requestDao.findById(id).orElseThrow(ObjectNotFoundException::new);
    }

    @Override
    public void addRequestItem(User user, Dish dish, int quantity) {
        List<Request> requests = requestDao.findAllByUserAndStatus(user.getId(), Status.OPENED.toInt());
        Request request;
        if (requests.size() == 0) {
            request = new Request(user, Status.OPENED);
            requestItemDao.save(new RequestItem(request.getId(), dish, quantity));
            return;
        } else {
            request = requests.get(0);
        }

        RequestItem requestItem = getDishRequestItem(dish, request.getRequestItems());
        if (requestItem == null) {
            requestItem = new RequestItem(request.getId(), dish, quantity);
        } else {
            requestItem.setQuantity(requestItem.getQuantity() + 1);
        }

        requestItemDao.save(requestItem);
    }

    @Override
    public void setRequestPending(int requestId) {
        Request request = findById(requestId);
        request.setStatus(Status.PENDING);
        requestDao.update(request);
    }

    @Override
    public void setRequestCooking(int requestId) {
        Request request = findById(requestId);
        request.setStatus(Status.COOKING);
        requestDao.update(request);
    }

    @Override
    public void setRequestDelivering(int requestId) {
        Request request = findById(requestId);
        request.setStatus(Status.DELIVERING);
        requestDao.update(request);
    }

    @Override
    public void setRequestDone(int requestId) {
        Request request = findById(requestId);
        request.setStatus(Status.DONE);
        requestDao.update(request);
    }

    @Override
    public void deleteById(int id) {
        requestDao.deleteById(id);
    }

    private RequestItem getDishRequestItem(Dish dish, List<RequestItem> requestItems) {
        if (requestItems.size() == 0)
            return null;
        for (RequestItem rs : requestItems) {
            if (rs.getDish().equals(dish))
                return rs;
        }
        return null;
    }

}
