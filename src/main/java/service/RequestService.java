package service;

import entities.Dish;
import entities.Request;
import entities.User;
import util.Page;

public interface RequestService {

    Page<Request> findAllByCustomerId(int id, int page);

    Page<Request> findAllByStatus(int id, int page);

    Request findById(int id);

    void addRequestItem(User user, Dish dish, int quantity);

    void setRequestPending(int requestId);

    void setRequestCooking(int requestId);

    void setRequestDelivering(int requestId);

    void setRequestDone(int requestId);

    void deleteById(int id);

}
