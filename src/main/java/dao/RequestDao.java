package dao;

import entities.Request;
import entities.Status;
import util.Page;

import java.util.List;
import java.util.Optional;

/**
 * data access object interface for request table
 */
public interface RequestDao extends CrudDao<Request> {

    /**
     * finds limited number of requests and returns them as Page object
     *
     * @param limit max number of requests per Page
     * @param index Page index. indexation starts with 1.
     * @return Page object with n<=limit requests
     * @throws IllegalArgumentException if limit is less than 0 or
     *                                  index is less than 1
     */
    Page<Request> findAll(int limit, int index);

    /**
     * finds limited number of requests with given user id
     * and returns them as Page object
     *
     * @param id    user unique identifier
     * @param limit max number of requests per Page
     * @param index Page index. indexation starts with 1.
     * @return Page object with n<=limit requests with given user id
     * @throws IllegalArgumentException if limit is less than 0 or
     *                                  index is less than 1
     */
    Page<Request> findAllByUserId(int id, int limit, int index);

    /**
     * finds limited number of requests with status which isn't
     * equal to given and returns them as Page object
     *
     * @param status status to exclude from search
     * @param limit  max number of requests per Page
     * @param index  Page index. indexation starts with 1.
     * @return Page object with n<=limit requests which have status not, which
     * isn't equal to given
     * @throws IllegalArgumentException if limit is less than 0 or
     *                                  index is less than 1
     */
    Page<Request> findAllWhereStatusNotEqual(Status status, int limit, int index);

    /**
     * finds limited number of requests with given status id
     * and returns them as Page object
     *
     * @param id    status unique identifier
     * @param limit max number of requests per Page
     * @param index Page index. indexation starts with 1.
     * @return Page object with n<=limit requests with given status id
     * @throws IllegalArgumentException if limit is less than 0 or
     *                                  index is less than 1
     */
    Page<Request> findAllByStatusId(int id, int limit, int index);

    /**
     * finds limited number of requests with given user id and status id
     * and returns them as Page object
     *
     * @param userId   user unique identifier
     * @param statusId status unique identifier
     * @param limit    max number of requests per Page
     * @param index    Page index. indexation starts with 1.
     * @return Page object with n<=limit requests with given user id and status id
     * @throws IllegalArgumentException if limit is less than 0 or
     *                                  index is less than 1
     */
    Page<Request> findAllByUserAndStatus(int userId, int statusId, int limit, int index);

    /**
     * finds first request with given user id and status id
     *
     * @param userId   user unique identifier
     * @param statusId status unique identifier
     * @return optional Request object
     */
    Optional<Request> findFirstByUserAndStatus(int userId, int statusId);

}
