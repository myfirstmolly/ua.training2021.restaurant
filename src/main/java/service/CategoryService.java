package service;

import entities.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category findById(int id);

    void add(Category category);

    void update(Category category);

    void deleteById(int id);

}
