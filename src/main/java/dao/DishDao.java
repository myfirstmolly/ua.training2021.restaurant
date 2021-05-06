package dao;

import entities.Category;
import entities.Dish;
import exceptions.DatabaseException;

import java.util.List;

public interface DishDao extends CrudDao<Dish> {

    List<Dish> findAllByCategory(Category category) throws DatabaseException;

    List<Dish> findAllSortedBy(Option option) throws DatabaseException;

    enum Option {
        NAME, PRICE, CATEGORY
    }

}
