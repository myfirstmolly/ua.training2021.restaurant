package model.service.impl;

import model.dao.RequestDao;
import model.dao.RequestItemDao;
import model.entities.*;
import model.service.RequestService;
import util.Page;

import java.util.List;
import java.util.Optional;

public class RequestServiceImpl implements RequestService {

    private final int LIMIT = 18;
    private final RequestDao requestDao;
    private final RequestItemDao requestItemDao;

    public RequestServiceImpl(RequestDao requestDao, RequestItemDao requestItemDao) {
        this.requestDao = requestDao;
        this.requestItemDao = requestItemDao;
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
    public void addRequestItem(User user, Dish dish, int quantity) {
        // since OPENED status represents dishes in user's cart, each user must have either zero or 1 request
        // with this status
        Optional<Request> request = requestDao.findFirstByUserAndStatus(user.getId(), Status.OPENED.getId());
        if (!request.isPresent()) { // <-- if user wants to add item to cart, but OPENED request doesn't exist yet, it will be created
            Request req = new Request(user, Status.OPENED);
            requestDao.save(req);
            requestItemDao.save(new RequestItem(req.getId(), dish, quantity));
            return;
        }

        // if OPENED request already exists, we need to check if dish that is
        // being added to cart is there already
        RequestItem requestItem = getDishRequestItem(dish, request.get().getRequestItems());
        if (requestItem == null) { // <-- if dish isn't in cart, then it will be added there with quantity equal to param quantity
            requestItem = new RequestItem(request.get().getId(), dish, quantity);
            requestItemDao.save(requestItem);
        } else { // <-- if dish is in the cart already, then its quantity will be increased.
            requestItem.setQuantity(requestItem.getQuantity() + 1);
            requestItemDao.update(requestItem);
        }
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
        if (!findById(requestId).isPresent())
            return;
        Request request = findById(requestId).get();

        // if manager tries to set request status which is previous to current, do nothing
        if (status.getId() <= request.getStatus().getId())
            return;

        // setting request status 'COOKING' means that this request is approved by manager
        if (status.equals(Status.COOKING)) {
            request.setApprovedBy(manager);
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
