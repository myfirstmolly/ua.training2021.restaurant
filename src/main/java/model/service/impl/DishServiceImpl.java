package model.service.impl;

import model.dao.DishDao;
import model.entities.Dish;
import model.service.DishService;
import util.Page;

import java.util.Optional;

public class DishServiceImpl implements DishService {

    private final DishDao dishDao;
    private final int LIMIT = 12;

    public DishServiceImpl(DishDao dishDao) {
        this.dishDao = dishDao;
    }

    @Override
    public Page<Dish> findAll(int page) {
        return dishDao.findAll(LIMIT, page);
    }

    @Override
    public Page<Dish> findAllOrderBy(String orderBy, int page) {
        if ("name".equals(orderBy))
            return dishDao.findAllSortedByName(LIMIT, page);
        if ("price".equals(orderBy))
            return dishDao.findAllSortedByPrice(LIMIT, page);
        if ("category".equals(orderBy))
            return dishDao.findAllSortedByCategory(LIMIT, page);
        return findAll(page);
    }

    @Override
    public Page<Dish> findAllByCategoryId(int id, int page) {
        return dishDao.findAllByCategoryId(id, LIMIT, page);
    }

    @Override
    public Optional<Dish> findById(int id) {
        return dishDao.findById(id);
    }

    @Override
    public void save(Dish dish) {
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

}
