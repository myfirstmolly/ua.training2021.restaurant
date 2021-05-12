package dao.impl;

import dao.CategoryDao;
import database.DBManager;
import entities.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class CategoryDaoImpl extends AbstractDao<Category> implements CategoryDao {

    public CategoryDaoImpl(DBManager dbManager) {
        super(dbManager, "category", new Mapper());
    }

    @Override
    public void save(Category category) {
        if (category == null) return;
        super.save(category, getParams(category));
    }

    @Override
    public void update(Category category) {
        if (category == null) return;
        super.update(category.getId(), getParams(category));
    }

    private Param [] getParams(Category category) {
        Param nameEng = new Param(category.getNameEng(), "name_eng");
        Param nameUkr = new Param(category.getNameUkr(), "name_ukr");
        return new Param[] {nameEng, nameUkr};
    }

    private static class Mapper implements AbstractDao.Mapper<Category> {
        @Override
        public Category map(ResultSet rs) throws SQLException {
            Category category = new Category();
            category.setId(rs.getInt("id"));
            category.setNameEng(rs.getString("name_eng"));
            category.setNameUkr(rs.getString("name_ukr"));
            return category;
        }
    }

}
