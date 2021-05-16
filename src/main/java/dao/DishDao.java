package dao;

import entities.Dish;
import util.Page;

public interface DishDao extends CrudDao<Dish> {

    Page<Dish> findAllByCategoryId(int id, int limit, int index);

    Page<Dish> findAll (int limit, int index);

    Page<Dish> findAllSortedByName (int limit, int index);

    Page<Dish> findAllSortedByCategory (int limit, int index);

    Page<Dish> findAllSortedByPrice (int limit, int index);

}
