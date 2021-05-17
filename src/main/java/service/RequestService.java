package service;

import entities.Dish;
import entities.Request;
import entities.Status;
import entities.User;
import util.Page;

import java.util.List;
import java.util.Optional;

public interface RequestService {

    Page<Request> findAllByUserId(int id, int page);

    Page<Request> findAllByStatus(int id, int page);

    List<Request> findAllByUserAndStatus(User user, Status status);

    Optional<Request> findById(int id);

    void addRequestItem(User user, Dish dish, int quantity);

    void setRequestStatus(int requestId, Status status);

    void deleteById(int id);

}
