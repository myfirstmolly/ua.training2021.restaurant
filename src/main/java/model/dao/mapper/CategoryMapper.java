package model.dao.mapper;

import model.entities.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryMapper implements Mapper<Category> {

    private final String columnId;
    private final String columnName;
    private final String columnNameUkr;

    public CategoryMapper(String columnId, String columnName, String columnNameUkr) {
        this.columnId = columnId;
        this.columnName = columnName;
        this.columnNameUkr = columnNameUkr;
    }

    @Override
    public Category map(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt(columnId));
        category.setName(rs.getString(columnName));
        category.setNameUkr(rs.getString(columnNameUkr));
        return category;
    }

}
