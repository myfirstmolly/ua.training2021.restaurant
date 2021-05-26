package model.service;

import model.entities.RequestItem;

import java.util.List;
import java.util.Optional;

public interface RequestItemService {

    /**
     * finds all request items mapped to request
     *
     * @param id request unique identifier
     * @return list of request items
     */
    List<RequestItem> findAllByRequestId(int id);

    /**
     * finds one RequestItem by its unique identifier
     *
     * @param id RequestItem unique identifier
     * @return Optional Dish object
     */
    Optional<RequestItem> findById(int id);

    /**
     * saves RequestItem user's cart.
     *
     * @param userId user whose cart should be updated
     * @param dishId dish to add to cart
     */
    void addItem(int userId, int dishId);

    /**
     * updates RequestItem
     *
     * @param requestItem item to update
     */
    void updateQuantity(RequestItem requestItem);

    /**
     * deletes RequestItem
     *
     * @param id item unique identifier
     */
    void deleteById(int id);

}
