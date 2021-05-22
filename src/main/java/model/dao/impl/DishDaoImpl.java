package model.dao.impl;

import model.dao.DishDao;
import model.database.DBManager;
import model.database.DaoFactory;
import model.entities.Category;
import model.entities.Dish;
import model.exceptions.ObjectNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Page;

import java.util.List;
import java.util.Optional;

public final class DishDaoImpl implements DishDao {

    private static final String TABLE_NAME = "dish";
    private static final Logger logger = LogManager.getLogger(DishDaoImpl.class);
    private final DaoUtils<Dish> daoUtils;
    private static final String JOIN_STRING = "select d.*, c.name as category_name " +
            "from dish d inner join category c on c.id=d.category_id ";

    public DishDaoImpl(DBManager dbManager) {
        daoUtils = new DaoUtils<>(dbManager, TABLE_NAME, rs -> {
            Dish dish = new Dish();
            dish.setId(rs.getInt("id"));
            dish.setName(rs.getString("name"));
            dish.setNameUkr(rs.getString("name_ukr"));
            dish.setPrice(rs.getInt("price"));
            dish.setDescription(rs.getString("description"));
            dish.setDescriptionUkr(rs.getString("description_ukr"));
            dish.setImagePath(rs.getString("image_path"));
            Category category = new Category();
            category.setId(rs.getInt("category_id"));
            category.setName(rs.getString("category_name"));
            dish.setCategory(category);
            return dish;
        });
    }

    @Override
    public Page<Dish> findAllByCategoryId(int id, int limit, int index) {
        String sql = JOIN_STRING + "where category_id=? limit ? offset ?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getPage(limit, index, sql, id);
    }

    @Override
    public Page<Dish> findAll(int limit, int index) {
        String sql = JOIN_STRING + "limit ? offset ?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getPage(limit, index, sql);
    }

    @Override
    public Page<Dish> findAllSortedByName(int limit, int index) {
        String sql = JOIN_STRING + "order by name limit ? offset ?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getPage(limit, index, sql);
    }

    @Override
    public Page<Dish> findAllSortedByNameUkr(int limit, int index) {
        String sql = JOIN_STRING + "order by name_ukr limit ? offset ?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getPage(limit, index, sql);
    }

    @Override
    public Page<Dish> findAllSortedByCategory(int limit, int index) {
        String sql = JOIN_STRING + "order by category_id limit ? offset ?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getPage(limit, index, sql);
    }

    @Override
    public Page<Dish> findAllSortedByPrice(int limit, int index) {
        String sql = JOIN_STRING + "order by price limit ? offset ?";
        logger.trace("delegated " + sql + " to DaoUtils");
        return daoUtils.getPage(limit, index, sql);
    }

    @Override
    public List<Dish> findAll() {
        String sql = JOIN_STRING;
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getList(sql);
    }

    @Override
    public Optional<Dish> findById(int id) {
        String sql = JOIN_STRING + "where d.id=?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getOptional(sql, id);
    }

    @Override
    public void save(Dish dish) {
        if (dish == null || dish.getCategory() == null) {
            logger.warn("cannot save null object");
            return;
        }
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setInsert(getColumnNames()).build();
        logger.trace("delegated " + sql + " to DaoUtils");
        daoUtils.save(dish, sql, dish.getName(), dish.getNameUkr(), dish.getPrice(),
                dish.getDescription(), dish.getDescriptionUkr(), dish.getImagePath(),
                dish.getCategory().getId());
    }

    @Override
    public void update(Dish dish) {
        if (dish == null || dish.getCategory() == null) {
            logger.warn("cannot update null object");
            return;
        }
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setUpdate(getColumnNames()).setWhere("id").build();
        logger.trace("delegated " + sql + " to DaoUtils");
        daoUtils.update(sql, dish.getName(), dish.getNameUkr(), dish.getPrice(),
                dish.getDescription(), dish.getDescriptionUkr(), dish.getImagePath(),
                dish.getCategory().getId(), dish.getId());
    }

    @Override
    public void deleteById(int id) {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setDelete().setWhere("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        daoUtils.deleteById(id, sql);
    }

    @Override
    public void delete(Dish dish) {
        if (dish == null) {
            logger.warn("Cannot delete null object in from table " + TABLE_NAME);
            return;
        }
        deleteById(dish.getId());
    }

    private String[] getColumnNames() {
        return new String[]{"name", "name_ukr", "price", "description", "description_ukr", "image_path", "category_id"};
    }

}
