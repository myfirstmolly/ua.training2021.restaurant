package service;

import entities.*;
import util.Page;

import java.util.List;
import java.util.Optional;

public interface RequestService {

    Page<Request> findAllByUserId(int id, int page);

    Page<Request> findAll(int page);

    Page<Request> findAllByStatus(int id, int page);

    List<Request> findAllByUserAndStatus(User user, Status status);

    Optional<Request> findById(int id);

    Optional<RequestItem> findRequestItemById(int id);

    void addRequestItem(User user, Dish dish, int quantity);

    void updateRequestItem(RequestItem requestItem);

    void setRequestStatus(int requestId, Status status);

    void deleteRequestItem(int id);

    void deleteById(int id);

}
