package dao.impl;

import dao.CategoryDao;
import dao.DishDao;
import database.DBManager;
import entities.Dish;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Page;

public final class DishDaoImpl extends DaoUtils<Dish> implements DishDao {

    private static final String TABLE_NAME = "dish";
    private static final Logger logger = LogManager.getLogger(DishDaoImpl.class);

    public DishDaoImpl(DBManager dbManager) {
        super(dbManager, TABLE_NAME, rs -> {
            Dish dish = new Dish();
            dish.setId(rs.getInt("id"));
            dish.setName(rs.getString("name"));
            dish.setPrice(rs.getInt("price"));
            dish.setDescription(rs.getString("description"));
            dish.setImagePath(rs.getString("image_path"));
            CategoryDao categoryDao = new CategoryDaoImpl(dbManager);
            dish.setCategory(categoryDao.findById(rs.getInt("category_id")).get());
            return dish;
        });
    }

    @Override
    public Page<Dish> findAllByCategoryId(int id, int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setSelect().setWhere("category_id").setLimit().setOffset().build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return super.getPage(limit, index, sql, id);
    }

    @Override
    public Page<Dish> findAll(int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setSelect().setOrderBy("id desc").setLimit().setOffset().build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return super.getPage(limit, index, sql);
    }

    @Override
    public Page<Dish> findAllSortedByName(int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setSelect().setOrderBy("name").setLimit().setOffset().build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return super.getPage(limit, index, sql);
    }

    @Override
    public Page<Dish> findAllSortedByCategory(int limit, int index) {
        String statement = "select d.* from dish d inner join category c on d.category_id=c.id " +
                "order by c.name limit ? offset ?";
        logger.trace("delegated " + statement + " to DaoUtils");
        return super.getPage(limit, index, statement);
    }

    @Override
    public Page<Dish> findAllSortedByPrice(int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setSelect().setOrderBy("price").setLimit().setOffset().build();
        logger.trace("delegated " + sql + " to DaoUtils");
        return super.getPage(limit, index, sql);
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
        super.save(dish, sql, dish.getName(), dish.getPrice(),
                dish.getDescription(), dish.getImagePath(),
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
        super.update(sql, dish.getName(), dish.getPrice(),
                dish.getDescription(), dish.getImagePath(),
                dish.getCategory().getId(), dish.getId());
    }

    private String[] getColumnNames() {
        return new String[]{"name", "price", "description", "image_path", "category_id"};
    }

}
