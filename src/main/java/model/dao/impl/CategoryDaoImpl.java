package model.dao.impl;

import model.dao.CategoryDao;
import model.database.DBManager;
import model.entities.Category;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;
import java.util.Optional;

public final class CategoryDaoImpl implements CategoryDao {

    private static final String TABLE_NAME = "category";
    private static final Logger logger = LogManager.getLogger(CategoryDaoImpl.class);
    private final DaoUtils<Category> daoUtils;

    public CategoryDaoImpl(DBManager dbManager) {
        daoUtils = new DaoUtils<>(dbManager, TABLE_NAME, rs -> {
            Category category = new Category();
            category.setId(rs.getInt("id"));
            category.setName(rs.getString("name"));
            return category;
        });
    }

    @Override
    public List<Category> findAll() {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setSelect().build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getList(sql);
    }

    @Override
    public Optional<Category> findById(int id) {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setSelect().setWhere("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getOptional(sql, id);
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
        daoUtils.save(category, sql, category.getName());
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
        daoUtils.update(sql, category.getName(), category.getId());
    }

    @Override
    public void deleteById(int id) {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setDelete().setWhere("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        daoUtils.deleteById(id, sql);
    }

    @Override
    public void delete(Category category) {
        if (category == null) {
            logger.warn("Cannot delete null object in from table " + TABLE_NAME);
            return;
        }
        deleteById(category.getId());
    }
}
