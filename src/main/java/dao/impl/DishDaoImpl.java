package dao.impl;

import dao.CategoryDao;
import dao.DishDao;
import database.DBManager;
import entities.Dish;
import util.Page;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class DishDaoImpl extends AbstractDao<Dish> implements DishDao {

    public DishDaoImpl(DBManager dbManager) {
        super(dbManager, "dish", new Mapper(dbManager));
    }

    @Override
    public Page<Dish> findAllByCategoryId(int id, int limit, int index) {
        Param category = new Param(id, "category_id");
        return super.findAllByParameter(limit, index, category);
    }

    @Override
    public Page<Dish> findAll(int limit, int index) {
        return super.findAllByParameter(limit, index);
    }

    @Override
    public Page<Dish> findAllSortedByName(String locale, int limit, int index) {
        return super.findAllByParameter(limit, index, "name_" + locale);
    }

    @Override
    public Page<Dish> findAllSortedByCategory(String locale, int limit, int index) {
        if (limit < 0 || index < 1)
            throw new IllegalArgumentException();

        int offset = limit * (index - 1);
        String statement = "select d.* from dish d inner join category c on d.category_id=c.id " +
                "order by c.? limit ? offset ?";
        List<Dish> page = super.findAllByParameter(statement, "name_" + locale, limit, offset);
        int total = super.getTotal();
        return new Page<>(index, total, page);
    }

    @Override
    public Page<Dish> findAllSortedByPrice(int limit, int index) {
        return super.findAllByParameter(limit, index, "price");
    }

    @Override
    public void save(Dish dish) {
        if (dish == null) return;
        super.save(dish, getParams(dish));
    }

    @Override
    public void update(Dish dish) {
        if (dish == null) return;
        super.update(dish.getId(), getParams(dish));
    }

    private Param [] getParams(Dish dish) {
        Param nameEng = new Param(dish.getNameEng(), "name_eng");
        Param nameUkr = new Param(dish.getNameUkr(), "name_ukr");
        Param price = new Param(dish.getPrice(), "price");
        Param descriptionEng = new Param(dish.getDescriptionEng(), "description_eng");
        Param descriptionUkr = new Param(dish.getDescriptionUkr(), "description_ukr");
        Param imagePath = new Param(dish.getImagePath(), "image_path");
        Param categoryId = new Param(dish.getCategory().getId(), "category_id");
        return new Param[] {nameEng, nameUkr, price, descriptionEng, descriptionUkr, imagePath, categoryId};
    }

    private static class Mapper implements AbstractDao.Mapper<Dish> {

        private final DBManager dbManager;

        public Mapper(DBManager dbManager) {
            this.dbManager = dbManager;
        }

        @Override
        public Dish map(ResultSet rs) throws SQLException {
            Dish dish = new Dish();
            dish.setId(rs.getInt("id"));
            dish.setNameEng(rs.getString("name_eng"));
            dish.setNameUkr(rs.getString("name_ukr"));
            dish.setPrice(rs.getInt("price"));
            dish.setDescriptionEng(rs.getString("description_eng"));
            dish.setDescriptionUkr(rs.getString("description_ukr"));
            dish.setImagePath(rs.getString("image_path"));
            CategoryDao categoryDao = new CategoryDaoImpl(dbManager);
            dish.setCategory(categoryDao.findById(rs.getInt("category_id")).get());
            return dish;
        }
    }

}
