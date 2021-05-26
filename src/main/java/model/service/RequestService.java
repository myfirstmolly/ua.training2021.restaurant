package model.service;

import model.entities.Request;
import model.entities.Status;
import model.entities.User;
import util.Page;

import java.util.Optional;

public interface RequestService {

    /**
     * finds limited number of Requests (defined in implementation class)
     * with given user id and returns them as Page object
     *
     * @param id   user unique identifier
     * @param page page index. indexation starts with 1
     * @return Page object containing found requests
     */
    Page<Request> findAllByUserId(int id, int page);

    /**
     * finds limited number of Requests (defined in implementation class)
     * and returns them as Page object
     *
     * @param page page index. indexation starts with 1
     * @return Page object containing found requests
     */
    Page<Request> findAll(int page);

    /**
     * finds limited number of Requests (defined in implementation class)
     * with given status id and returns them as Page object
     *
     * @param id   status unique identifier
     * @param page page index. indexation starts with 1
     * @return Page object containing found requests
     */
    Page<Request> findAllByStatus(int id, int page);

    /**
     * finds limited number of Requests (defined in implementation class)
     * mapped to given user and status and returns them as Page object
     *
     * @param user   user to whom requests are mapped
     * @param status status which requests have
     * @param page   page index. indexation starts with 1
     * @return Page object containing found requests
     */
    Page<Request> findAllByUserAndStatus(User user, Status status, int page);

    /**
     * finds one Request by user and status
     *
     * @param user   user
     * @param status request status
     * @return Optional Dish object
     */
    Optional<Request> findOneByUserAndStatus(User user, Status status);

    /**
     * finds one Request by its unique identifier
     *
     * @param id Request unique identifier
     * @return Optional Dish object
     */
    Optional<Request> findById(int id);

    /**
     * updates Request
     *
     * @param request request to update
     */
    void approveRequest(Request request);

    /**
     * updates Request status
     *
     * @param requestId request unique identifier
     * @param status    new request status
     * @param manager   manager who updates status
     */
    void setRequestStatus(int requestId, Status status, User manager);

    /**
     * deletes Request
     *
     * @param id request unique identifier
     */
    void deleteById(int id);
}
