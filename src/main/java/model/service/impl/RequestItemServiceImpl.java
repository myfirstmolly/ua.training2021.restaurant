package model.service.impl;

import model.dao.RequestItemDao;
import model.entities.RequestItem;
import model.service.RequestItemService;

import java.util.List;
import java.util.Optional;

public class RequestItemServiceImpl implements RequestItemService {

    private final RequestItemDao requestItemDao;

    public RequestItemServiceImpl(RequestItemDao requestItemDao) {
        this.requestItemDao = requestItemDao;
    }

    @Override
    public List<RequestItem> findAllByRequestId(int id) {
        return requestItemDao.findAllByRequestId(id);
    }

    @Override
    public Optional<RequestItem> findById(int id) {
        return requestItemDao.findById(id);
    }

    @Override
    public void addItem(int userId, int dishId) {
        requestItemDao.addRequestItem(userId, dishId);
    }

    @Override
    public void increaseQuantity(int requestItemId) {
        requestItemDao.increaseQuantity(requestItemId);
    }

    @Override
    public void decreaseQuantity(int requestItemId) {
        requestItemDao.decreaseQuantity(requestItemId);
    }

    @Override
    public void deleteById(int id) {
        requestItemDao.deleteById(id);
    }
}
