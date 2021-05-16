package dao.impl;

import dao.CategoryDao;
import database.DBManager;
import entities.Category;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public final class CategoryDaoImpl extends DaoUtils<Category> implements CategoryDao {

    private static final String TABLE_NAME = "category";
    private static final Logger logger = LogManager.getLogger(CategoryDaoImpl.class);

    public CategoryDaoImpl(DBManager dbManager) {
        super(dbManager, TABLE_NAME, rs -> {
            Category category = new Category();
            category.setId(rs.getInt("id"));
            category.setName(rs.getString("name"));
            return category;
        });
    }

    @Override
    public void save(Category category) {
        if (category == null) {
            logger.warn("cannot save null object");
            return;
        }
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setInsert("name").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        super.save(category, sql, category.getName());
    }

    @Override
    public void update(Category category) {
        if (category == null) {
            logger.warn("cannot update null object");
            return;
        }
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setUpdate("name").setWhere("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        super.update(sql, category.getName(), category.getId());
    }
}
