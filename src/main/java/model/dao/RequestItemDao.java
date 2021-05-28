package model.dao;

import model.entities.RequestItem;

import java.util.List;

/**
 * data access object interface for request_item table
 */
public interface RequestItemDao extends CrudDao<RequestItem> {

    /**
     * finds all request items with given request id
     *
     * @param id request unique identifier
     * @return list of RequestItems with given request id
     */
    List<RequestItem> findAllByRequestId(int id);

    /**
     * adds request item to database. since request items are only added when
     * user hasn't approved their request yet, this method firstly checks if user
     * with this id already has request with status 'OPENED'. if such one exists,
     * order items will be associated with this order. if not, new order will be created.
     *
     * @param userId user unique identifier
     * @param dishId dish unique identifier
     */
    void addRequestItem(int userId, int dishId);

    void increaseQuantity(int requestId);

    void decreaseQuantity(int requestId);

}
