package dao.impl;

import dao.CategoryDao;
import database.DBManager;
import entities.Category;
import exceptions.DataIntegrityViolationException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class CategoryDaoImpl extends AbstractDao<Category> implements CategoryDao {

    public CategoryDaoImpl(DBManager dbManager) {
        super(dbManager, "category", new Mapper());
        super.saveStmt = "insert into category (name_eng, name_ukr) values(?,?)";
        super.updateStmt = "update category set name_eng=?, name_ukr=? where id=?";
    }

    private static class Mapper implements AbstractDao.Mapper<Category> {
        @Override
        public void setSaveStatementParams(Category category, PreparedStatement ps) throws SQLException {
            if (category.getNameEng() == null || category.getNameUkr() == null)
                throw new DataIntegrityViolationException();

            ps.setString(1, category.getNameEng());
            ps.setString(2, category.getNameUkr());
        }

        @Override
        public void setUpdateStatementParams(Category category, PreparedStatement ps) throws SQLException {
            setSaveStatementParams(category, ps);
            ps.setInt(3, category.getId());
        }

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
