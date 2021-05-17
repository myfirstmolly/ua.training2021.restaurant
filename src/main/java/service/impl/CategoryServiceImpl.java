package service.impl;

import dao.CategoryDao;
import dao.impl.CategoryDaoImpl;
import database.DBManager;
import entities.Category;
import service.CategoryService;

import java.util.List;
import java.util.Optional;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;

    public CategoryServiceImpl(DBManager dbManager) {
        categoryDao = new CategoryDaoImpl(dbManager);
    }

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public Optional<Category> findById(int id) {
        return categoryDao.findById(id);
    }

    @Override
    public void add(Category category) {
        categoryDao.save(category);
    }

    @Override
    public void update(Category category) {
        categoryDao.update(category);
    }

    @Override
    public void deleteById(int id) {
        categoryDao.deleteById(id);
    }

}
