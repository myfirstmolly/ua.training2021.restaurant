package model.dao;

import model.entities.Category;

import java.util.List;

/**
 * data access object interface for category table
 */
public interface CategoryDao extends CrudDao<Category> {

    /**
     * finds all categories with given locale
     *
     * @param locale locale code
     * @return List of categories
     */
    List<Category> findAll(String locale);

}
