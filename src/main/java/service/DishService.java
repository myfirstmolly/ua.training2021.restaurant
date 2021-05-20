package service;

import entities.Dish;
import util.Page;

import java.util.Optional;

/**
 * service layer for Dish entity
 */
public interface DishService {

    /**
     * finds limited number of Dishes (defined in implementation class)
     * and returns them as Page object
     *
     * @param page page index. indexation starts with 1.
     * @return Page object containing found dishes
     */
    Page<Dish> findAll(int page);

    /**
     * finds limited number of Dishes (defined in implementation class)
     * sorted by orderBy field and returns them as Page object
     *
     * @param orderBy field to sort by, can be equal to {'name', 'price', 'category'}
     * @param page    page index. indexation starts with 1.
     * @return Page object containing found dishes
     */
    Page<Dish> findAllOrderBy(String orderBy, int page);

    /**
     * finds limited number of Dishes (defined in implementation class)
     * with given category id and returns them as Page object
     *
     * @param id   category unique identifier
     * @param page index. indexation starts with 1.
     * @return Page object containing found dishes
     */
    Page<Dish> findAllByCategoryId(int id, int page);

    /**
     * finds one Dish by its id
     *
     * @param id dish unique identifier
     * @return Optional Dish object
     */
    Optional<Dish> findById(int id);

    /**
     * saves dish to db
     *
     * @param dish dish to save
     */
    void save(Dish dish);

    void update(Dish dish);

    void deleteById(int id);

}
