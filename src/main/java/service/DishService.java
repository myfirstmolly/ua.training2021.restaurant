package service;

import entities.Dish;

import java.util.List;

public interface DishService {

    List<Dish> findAll();

    List<Dish> findAllSortedByNameEng();

    List<Dish> findAllSortedByNameUkr();

    List<Dish> findAllSortedByPrice();

    List<Dish> findAllSortedByCategoryEng();

    List<Dish> findAllSortedByCategoryUkr();

    List<Dish> findAllByCategoryId(int id);

    Dish findById(int id);

    void add(Dish dish);

    void update(Dish dish);

    void deleteById(int id);

}
