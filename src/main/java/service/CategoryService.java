package service;

import entities.Category;

import java.util.List;
import java.util.Optional;

/**
 * service layer for Category entity
 */
public interface CategoryService {

    /**
     * finds all categories that exist
     *
     * @return List of all Categories
     */
    List<Category> findAll();

    /**
     * finds category by its id
     *
     * @param id category unique identifier
     * @return Optional Category object
     */
    Optional<Category> findById(int id);

    /**
     * saves category to db
     *
     * @param category category to save
     */
    void save(Category category);

    /**
     * updates category
     *
     * @param category category to update
     */
    void update(Category category);

    /**
     * deletes category with given id
     *
     * @param id category unique identifier
     */
    void deleteById(int id);

}
