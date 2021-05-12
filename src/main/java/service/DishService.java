package service;

import entities.Dish;
import util.Page;

public interface DishService {

    Page<Dish> findAll(int page);

    Page<Dish> findAllSortedByNameEng(int page);

    Page<Dish> findAllSortedByNameUkr(int page);

    Page<Dish> findAllSortedByPrice(int page);

    Page<Dish> findAllSortedByCategoryEng(int page);

    Page<Dish> findAllSortedByCategoryUkr(int page);

    Page<Dish> findAllByCategoryId(int id, int page);

    Dish findById(int id);

    void add(Dish dish);

    void update(Dish dish);

    void deleteById(int id);

}
