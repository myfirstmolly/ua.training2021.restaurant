package service;

import entities.*;
import util.Page;

import java.util.Optional;

public interface RequestService {

    /**
     * finds limited number of Requests (defined in implementation class)
     * with given user id and returns them as Page object
     *
     * @param id
     * @param page
     * @return
     */
    Page<Request> findAllByUserId(int id, int page);

    Page<Request> findAll(int page);

    Page<Request> findAllByStatus(int id, int page);

    Page<Request> findAllByUserAndStatus(User user, Status status, int page);

    Optional<Request> findOneByUserAndStatus(User user, Status status);

    Optional<Request> findById(int id);

    Optional<RequestItem> findRequestItemById(int id);

    void addRequestItem(User user, Dish dish, int quantity);

    void updateRequestItem(RequestItem requestItem);

    void setRequestStatus(int requestId, Status status);

    void deleteRequestItem(int id);

    void deleteById(int id);

}
