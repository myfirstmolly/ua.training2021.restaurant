package model.service.impl;

import model.dao.RequestDao;
import model.dao.RequestItemDao;
import model.entities.*;
import model.exceptions.ObjectNotFoundException;
import model.service.RequestService;
import util.Page;

import java.util.List;
import java.util.Optional;

public class RequestServiceImpl implements RequestService {

    private final int LIMIT = 10;
    private final RequestDao requestDao;
    private final RequestItemDao requestItemDao;

    public RequestServiceImpl(RequestDao requestDao, RequestItemDao requestItemDao) {
        this.requestDao = requestDao;
        this.requestItemDao = requestItemDao;
    }

    @Override
    public List<RequestItem> findAllRequestItemsByRequest(Request request) {
        return requestItemDao.findAllByRequestId(request.getId());
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
        return requestDao.findAllByUserAndStatus(user.getId(), status.getId(), LIMIT, page);
    }

    @Override
    public Optional<Request> findOneByUserAndStatus(User user, Status status) {
        return requestDao.findFirstByUserAndStatus(user.getId(), status.getId());
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
    public void addRequestItem(User user, Dish dish) {
        requestItemDao.addRequestItem(user.getId(), dish.getId());
    }

    @Override
    public void updateRequestItem(RequestItem requestItem) {
        requestItemDao.update(requestItem);
    }

    @Override
    public void updateRequest(Request request) {
        requestDao.update(request);
    }

    @Override
    public void setRequestStatus(int requestId, Status status, User manager) {
        Request request = findById(requestId).orElseThrow(() -> new
                ObjectNotFoundException("request not found"));
        // setting request status 'COOKING' means that this request is approved by manager
        if (status.equals(Status.COOKING)) {
            request.setApprovedBy(manager.getId());
        }
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

}
