package service.impl;

import dao.RequestDao;
import dao.RequestItemDao;
import dao.impl.RequestDaoImpl;
import dao.impl.RequestItemDaoImpl;
import database.DBManager;
import entities.*;
import service.RequestService;
import util.Page;

import java.util.List;
import java.util.Optional;

public class RequestServiceImpl implements RequestService {

    private final int LIMIT = 18;
    private final RequestDao requestDao;
    private final RequestItemDao requestItemDao;

    public RequestServiceImpl(DBManager dbManager) {
        requestDao = new RequestDaoImpl(dbManager);
        requestItemDao = new RequestItemDaoImpl(dbManager);
    }

    @Override
    public Page<Request> findAllByUserId(int id, int page) {
        return requestDao.findAllByUserId(id, LIMIT, page);
    }

    @Override
    public Page<Request> findAll(int page) {
        return requestDao.findAllWhereStatusNotEqual(Status.OPENED, LIMIT, page);
    }

    @Override
    public Page<Request> findAllByStatus(int id, int page) {
        return requestDao.findAllByStatusId(id, LIMIT, page);
    }

    @Override
    public Page<Request> findAllByUserAndStatus(User user, Status status, int page) {
        return requestDao.findAllByUserAndStatus(user.getId(), status.toInt(), LIMIT, page);
    }

    @Override
    public Optional<Request> findOneByUserAndStatus(User user, Status status) {
        return requestDao.findFirstByUserAndStatus(user.getId(), status.toInt());
    }

    @Override
    public Optional<Request> findById(int id) {
        return requestDao.findById(id);
    }

    @Override
    public Optional<RequestItem> findRequestItemById(int id) {
        return requestItemDao.findById(id);
    }

    @Override
    public void addRequestItem(User user, Dish dish, int quantity) {
        Optional<Request> request = requestDao.findFirstByUserAndStatus(user.getId(), Status.OPENED.toInt());
        if (!request.isPresent()) {
            Request req = new Request(user, Status.OPENED);
            requestDao.save(req);
            requestItemDao.save(new RequestItem(req.getId(), dish, quantity));
            return;
        }

        RequestItem requestItem = getDishRequestItem(dish, request.get().getRequestItems());
        if (requestItem == null) {
            requestItem = new RequestItem(request.get().getId(), dish, quantity);
            requestItemDao.save(requestItem);
        } else {
            requestItem.setQuantity(requestItem.getQuantity() + 1);
            requestItemDao.update(requestItem);
        }
    }

    @Override
    public void updateRequestItem(RequestItem requestItem) {
        requestItemDao.update(requestItem);
    }

    @Override
    public void setRequestStatus(int requestId, Status status) {
        if (!findById(requestId).isPresent())
            return;
        Request request = findById(requestId).get();
        request.setStatus(status);
        requestDao.update(request);
    }

    @Override
    public void deleteRequestItem(int id) {
        requestItemDao.deleteById(id);
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
