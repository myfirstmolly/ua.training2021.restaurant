package dao;

import entities.Category;
import entities.Dish;
import exceptions.DaoException;

import java.util.List;

public interface DishDao extends CrudDao<Dish> {

    List<Dish> findAllByCategory(Category category) throws DaoException;

    List<Dish> findAllSortedBy(Option option) throws DaoException;

    enum Option {
        NAME, PRICE, CATEGORY
    }

}
