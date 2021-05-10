package dao;

import entities.Dish;

import java.util.List;

public interface DishDao extends CrudDao<Dish> {

    List<Dish> findAllByCategoryId(int id);

}
