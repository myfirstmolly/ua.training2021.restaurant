package service;

import entities.Dish;
import util.Page;

import java.util.Optional;

public interface DishService {

    Page<Dish> findAll(int page);

    Page<Dish> findAllOrderBy(int page, String orderBy);

    Page<Dish> findAllByCategoryId(int id, int page);

    Optional<Dish> findById(int id);

    void add(Dish dish);

    void update(Dish dish);

    void deleteById(int id);

}
