package dao;

import entities.Dish;
import util.Page;

/**
 * data access object interface for dish table
 */
public interface DishDao extends CrudDao<Dish> {

    /**
     * finds limited number of dishes with given category and returns them as Page object
     *
     * @param id    category unique identifier
     * @param limit max number of dishes per Page
     * @param index Page index. indexation starts with 1.
     * @return Page object with n<=limit dishes with given category id
     * @throws IllegalArgumentException if limit is less than 0 or
     *                                  index is less than 1
     */
    Page<Dish> findAllByCategoryId(int id, int limit, int index);

    /**
     * finds limited number of dishes and returns them as Page object
     *
     * @param limit max number of dishes per Page
     * @param index Page index. indexation starts with 1.
     * @return Page object with n<=limit dishes with given category id
     * @throws IllegalArgumentException if limit is less than 0 or
     *                                  index is less than 1
     */
    Page<Dish> findAll(int limit, int index);

    /**
     * finds limited number of sorted by name dishes and returns them as Page object
     *
     * @param limit max number of dishes per Page
     * @param index Page index. indexation starts with 1.
     * @return Page object with n<=limit dishes with given category id
     * @throws IllegalArgumentException if limit is less than 0 or
     *                                  index is less than 1
     */
    Page<Dish> findAllSortedByName(int limit, int index);

    /**
     * finds limited number of sorted by category name dishes and returns them as Page object
     *
     * @param limit max number of dishes per Page
     * @param index Page index. indexation starts with 1.
     * @return Page object with n<=limit dishes with given category id
     * @throws IllegalArgumentException if limit is less than 0 or
     *                                  index is less than 1
     */
    Page<Dish> findAllSortedByCategory(int limit, int index);

    /**
     * finds limited number of sorted by price dishes and returns them as Page object
     *
     * @param limit max number of dishes per Page
     * @param index Page index. indexation starts with 1.
     * @return Page object with n<=limit dishes with given category id
     * @throws IllegalArgumentException if limit is less than 0 or
     *                                  index is less than 1
     */
    Page<Dish> findAllSortedByPrice(int limit, int index);

}
