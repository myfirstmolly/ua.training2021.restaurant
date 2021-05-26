package model.service.impl;

import model.dao.RequestDao;
import model.entities.Request;
import model.entities.Status;
import model.entities.User;
import model.exceptions.ObjectNotFoundException;
import model.service.RequestService;
import util.Page;

import java.util.Optional;

public class RequestServiceImpl implements RequestService {

    private final int LIMIT = 10;
    private final RequestDao requestDao;

    public RequestServiceImpl(RequestDao requestDao) {
        this.requestDao = requestDao;
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
    public void approveRequest(Request request) {
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
    public void deleteById(int id) {
        requestDao.deleteById(id);
    }

}
