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
    public Page<Request> findAllByStatus(int id, int page) {
        return requestDao.findAllByStatusId(id, LIMIT, page);
    }

    @Override
    public List<Request> findAllByUserAndStatus(User user, Status status) {
        return requestDao.findAllByUserAndStatus(user.getId(), Status.OPENED.toInt());
    }

    @Override
    public Optional<Request> findById(int id) {
        return requestDao.findById(id);
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
    public void setRequestStatus(int requestId, Status status) {
        if (!findById(requestId).isPresent())
            return;
        Request request = findById(requestId).get();
        request.setStatus(status);
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
