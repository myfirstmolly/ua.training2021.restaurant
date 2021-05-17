package service.impl;

import dao.DishDao;
import dao.impl.DishDaoImpl;
import database.DBManager;
import entities.Dish;
import service.DishService;
import util.Page;

import java.util.Optional;

public class DishServiceImpl implements DishService {

    private final DishDao dishDao;
    private final int LIMIT = 18;

    public DishServiceImpl(DBManager dbManager) {
        dishDao = new DishDaoImpl(dbManager);
    }

    @Override
    public Page<Dish> findAll(int page) {
        return dishDao.findAll(LIMIT, page);
    }

    @Override
    public Page<Dish> findAllOrderBy(int page, String orderBy) {
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

}
