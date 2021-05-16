package service.impl;

import dao.DishDao;
import dao.impl.DishDaoImpl;
import database.DBManager;
import entities.Dish;
import exceptions.ObjectNotFoundException;
import service.DishService;
import util.Page;

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
    public Page<Dish> findAllSortedByName(int page) {
        return dishDao.findAllSortedByName(LIMIT, page);
    }

    @Override
    public Page<Dish> findAllSortedByPrice(int page) {
        return dishDao.findAllSortedByPrice(LIMIT, page);
    }

    @Override
    public Page<Dish> findAllSortedByCategory(int page) {
        return dishDao.findAllSortedByCategory(LIMIT, page);
    }

    @Override
    public Page<Dish> findAllByCategoryId(int id, int page) {
        return dishDao.findAllByCategoryId(id, LIMIT, page);
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

}
