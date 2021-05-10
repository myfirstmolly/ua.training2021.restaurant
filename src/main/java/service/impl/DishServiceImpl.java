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

    public DishServiceImpl(DBManager dbManager) {
        dishDao = new DishDaoImpl(dbManager);
    }

    @Override
    public List<Dish> findAll() {
        return dishDao.findAll();
    }

    @Override
    public List<Dish> findAllSortedByNameEng() {
        return dishDao.findAll()
                .stream()
                .sorted(Comparator.comparing(Dish::getNameEng))
                .collect(Collectors.toList());
    }

    @Override
    public List<Dish> findAllSortedByNameUkr() {
        return dishDao.findAll()
                .stream()
                .sorted(Comparator.comparing(Dish::getNameUkr))
                .collect(Collectors.toList());
    }

    @Override
    public List<Dish> findAllSortedByPrice() {
        return dishDao.findAll()
                .stream()
                .sorted(Comparator.comparing(Dish::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public List<Dish> findAllSortedByCategoryEng() {
        return dishDao.findAll()
                .stream()
                .sorted(Comparator.comparing(d ->
                        d.getCategory().getNameEng()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Dish> findAllSortedByCategoryUkr() {
        return dishDao.findAll()
                .stream()
                .sorted(Comparator.comparing(d ->
                        d.getCategory().getNameUkr()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Dish> findAllByCategoryId(int id) {
        return dishDao.findAllByCategoryId(id);
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
