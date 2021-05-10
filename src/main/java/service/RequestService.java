package service;

import entities.Dish;
import entities.Request;
import entities.User;

import java.util.List;

public interface RequestService {

    List<Request> findAllByCustomerId(int id);

    List<Request> findAllPending();

    Request findById(int id);

    void addRequestItem(User user, Dish dish, int quantity);

    void setRequestPending(int requestId);

    void setRequestCooking(int requestId);

    void setRequestDelivering(int requestId);

    void setRequestDone(int requestId);

    void deleteById(int id);

}
