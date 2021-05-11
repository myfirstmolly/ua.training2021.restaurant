package dao.impl;

import dao.CategoryDao;
import dao.DishDao;
import database.DBManager;
import entities.Dish;
import exceptions.DataIntegrityViolationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class DishDaoImpl extends AbstractDao<Dish> implements DishDao {

    public DishDaoImpl(DBManager dbManager) {
        super(dbManager, "dish", new Mapper(dbManager));
    }

    @Override
    public List<Dish> findAllByCategoryId(int id, int limit, int offset) {
        return super.findAllByParameter(limit, offset, new Param(id, "category_id"));
    }

    @Override
    public List<Dish> findAll(int limit, int offset) {
        return super.findAllByParameter(limit, offset);
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
        if (dish.getNameEng() == null || dish.getNameUkr() == null || dish.getPrice() < 0 ||
                dish.getImagePath() == null)
            throw new DataIntegrityViolationException();

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
