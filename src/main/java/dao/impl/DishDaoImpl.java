package dao.impl;

import dao.CategoryDao;
import dao.DishDao;
import database.DBManager;
import entities.Dish;
import util.Page;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class DishDaoImpl extends AbstractDao<Dish> implements DishDao {

    private static final String TABLE_NAME = "dish";

    public DishDaoImpl(DBManager dbManager) {
        super(dbManager, TABLE_NAME, new Mapper(dbManager));
    }

    @Override
    public Page<Dish> findAllByCategoryId(int id, int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        s.select().where("category_id").limit().offset();
        return super.getPage(limit, index, s.build(), " where category_id=?", id);
    }

    @Override
    public Page<Dish> findAll(int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        s.select().limit().offset();
        return super.getPage(limit, index, s.build(), "");
    }

    @Override
    public Page<Dish> findAllSortedByName(String locale, int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        s.select().orderBy("name_" + locale).limit().offset();
        return super.getPage(limit, index, s.build(), "");
    }

    @Override
    public Page<Dish> findAllSortedByCategory(String locale, int limit, int index) {
        String statement = String.format("select d.* from dish d inner join category c on d.category_id=c.id " +
                "order by %s limit ? offset ?", "c.name_" + locale);
        return super.getPage(limit, index, statement, "");
    }

    @Override
    public Page<Dish> findAllSortedByPrice(int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        s.select().orderBy("price").limit().offset();
        return super.getPage(limit, index, s.build(), "");
    }

    @Override
    public void save(Dish dish) {
        if (dish == null || dish.getCategory() == null) return;
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        b.insert(getColumnNames());
        super.save(dish, b.build(), dish.getNameEng(), dish.getNameUkr(), dish.getPrice(),
                dish.getDescriptionEng(), dish.getDescriptionUkr(), dish.getImagePath(),
                dish.getCategory().getId());
    }

    @Override
    public void update(Dish dish) {
        if (dish == null || dish.getCategory() == null) return;
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        b.update(getColumnNames()).where("id");
        super.update(dish.getId(), b.build(), dish.getNameEng(), dish.getNameUkr(), dish.getPrice(),
                dish.getDescriptionEng(), dish.getDescriptionUkr(), dish.getImagePath(),
                dish.getCategory().getId());
    }

    private String [] getColumnNames() {
        return new String[] {"name_eng", "name_ukr", "price", "description_eng",
                "description_ukr", "image_path", "category_id"};
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
