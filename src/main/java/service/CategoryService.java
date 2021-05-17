package service;

import entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findAll();

    Optional<Category> findById(int id);

    void add(Category category);

    void update(Category category);

    void deleteById(int id);

}
