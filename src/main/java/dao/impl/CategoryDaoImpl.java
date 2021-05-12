package dao.impl;

import dao.CategoryDao;
import database.DBManager;
import entities.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class CategoryDaoImpl extends AbstractDao<Category> implements CategoryDao {

    private static final String TABLE_NAME = "category";

    public CategoryDaoImpl(DBManager dbManager) {
        super(dbManager, TABLE_NAME, new Mapper());
    }

    @Override
    public void save(Category category) {
        if (category == null) return;
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        b.insert("name_eng", "name_ukr");
        super.save(category, b.build(), category.getNameEng(), category.getNameUkr());
    }

    @Override
    public void update(Category category) {
        if (category == null) return;
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        b.update("name_eng", "name_ukr").where("id");
        super.update(category.getId(), b.build(), category.getNameEng(), category.getNameUkr());
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
