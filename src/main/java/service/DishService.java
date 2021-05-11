package service;

import entities.Dish;

import java.util.List;

public interface DishService {

    List<Dish> findAll(int page);

    List<Dish> findAllSortedByNameEng(int page);

    List<Dish> findAllSortedByNameUkr(int page);

    List<Dish> findAllSortedByPrice(int page);

    List<Dish> findAllSortedByCategoryEng(int page);

    List<Dish> findAllSortedByCategoryUkr(int page);

    List<Dish> findAllByCategoryId(int id, int page);

    Dish findById(int id);

    void add(Dish dish);

    void update(Dish dish);

    void deleteById(int id);

}
