package dao.impl;

import dao.CategoryDao;
import dao.DishDao;
import database.DBManager;
import entities.Dish;
import exceptions.DataIntegrityViolationException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class DishDaoImpl extends AbstractDao<Dish> implements DishDao {

    public DishDaoImpl(DBManager dbManager) {
        super(dbManager, "dish", new Mapper(dbManager));
        super.saveStmt = "insert into dish (name_eng, name_ukr, price, " +
                "description_eng, description_ukr, image_path, category_id) " +
                "values (?,?,?,?,?,?,?)";
        super.updateStmt = "update dish set name_eng=?, name_ukr=?, price=?," +
                "description_eng=?, description_ukr=?, image_path=?, " +
                "category_id=? where id=?";
    }

    @Override
    public List<Dish> findAllByCategoryId(int id) {
        return super.findAllByParameter(new Param(id, "category_id"));
    }

    private static class Mapper implements AbstractDao.Mapper<Dish> {

        private final DBManager dbManager;

        public Mapper(DBManager dbManager) {
            this.dbManager = dbManager;
        }

        @Override
        public void setSaveStatementParams(Dish dish, PreparedStatement ps) throws SQLException {
            if (dish.getNameEng() == null || dish.getNameUkr() == null || dish.getPrice() < 0 ||
                    dish.getImagePath() == null)
                throw new DataIntegrityViolationException();

            ps.setString(1, dish.getNameEng());
            ps.setString(2, dish.getNameUkr());
            ps.setInt(3, dish.getPrice());
            ps.setString(4, dish.getDescriptionEng());
            ps.setString(5, dish.getDescriptionUkr());
            ps.setString(6, dish.getImagePath());
            ps.setInt(7, dish.getCategory().getId());
        }

        @Override
        public void setUpdateStatementParams(Dish dish, PreparedStatement ps) throws SQLException {
            setSaveStatementParams(dish, ps);
            ps.setInt(8, dish.getId());
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
