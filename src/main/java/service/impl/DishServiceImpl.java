package service.impl;

import dao.DishDao;
import dao.impl.DishDaoImpl;
import database.DBManager;
import entities.Dish;
import exceptions.ObjectNotFoundException;
import service.DishService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DishServiceImpl implements DishService {

    private final DishDao dishDao;
    private final int LIMIT = 18;

    public DishServiceImpl(DBManager dbManager) {
        dishDao = new DishDaoImpl(dbManager);
    }

    @Override
    public List<Dish> findAll(int page) {
        return dishDao.findAll(LIMIT, LIMIT * (page - 1));
    }

    @Override
    public List<Dish> findAllSortedByNameEng(int page) {
        return getSortedDishes(page, Comparator.comparing(Dish::getNameEng));
    }

    @Override
    public List<Dish> findAllSortedByNameUkr(int page) {
        return getSortedDishes(page, Comparator.comparing(Dish::getNameUkr));
    }

    @Override
    public List<Dish> findAllSortedByPrice(int page) {
        return getSortedDishes(page, Comparator.comparing(Dish::getPrice));
    }

    @Override
    public List<Dish> findAllSortedByCategoryEng(int page) {
        return getSortedDishes(page, Comparator.comparing(d -> d.getCategory().getNameEng()));
    }

    @Override
    public List<Dish> findAllSortedByCategoryUkr(int page) {
        return getSortedDishes(page, Comparator.comparing(d -> d.getCategory().getNameUkr()));
    }

    @Override
    public List<Dish> findAllByCategoryId(int id, int page) {
        return dishDao.findAllByCategoryId(id, LIMIT, LIMIT * (page - 1));
    }

    @Override
    public Dish findById(int id) {
        return dishDao.findById(id).orElseThrow(ObjectNotFoundException::new);
    }

    @Override
    public void add(Dish dish) {
        dishDao.save(dish);
    }

    @Override
    public void update(Dish dish) {
        dishDao.update(dish);
    }

    @Override
    public void deleteById(int id) {
        dishDao.deleteById(id);
    }

    private List<Dish> getSortedDishes(int page, Comparator<Dish> comparator) {
        page--;
        return dishDao.findAll()
                .stream()
                .sorted(comparator)
                .collect(Collectors.toList())
                .subList(LIMIT * (page - 1), LIMIT * page);
    }

}
