package model.dao.impl;

import model.dao.DishDao;
import model.dao.mapper.CategoryMapper;
import model.dao.mapper.DishMapper;
import model.database.DBManager;
import model.entities.Dish;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class DishDaoImpl implements DishDao {

    private static final Logger logger = LogManager.getLogger(DishDaoImpl.class);
    private final DBManager dbManager;

    public DishDaoImpl(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public Page<Dish> findAllByCategoryId(int id, int limit, int index) {
        if (limit < 0 || index < 1)
            throw new IllegalArgumentException();

        String sql = "select d.*, c.name as category_name, c.name_ukr as category_name_ukr " +
                "from dish d inner join category c " +
                "on c.id=d.category_id " +
                "where category_id=? limit ? offset ?";
        logger.info("sql: " + sql);
        String countTotal = "select count(*) from dish where category_id=?";
        logger.info("sql: " + countTotal);

        int offset = limit * (index - 1);
        List<Dish> entries = new ArrayList<>();
        int totalValues = 0;

        try (Connection con = dbManager.getConnection();
             PreparedStatement original = con.prepareStatement(sql);
             PreparedStatement count = con.prepareStatement(countTotal)) {

            original.setInt(1, id);
            original.setObject(2, limit);
            original.setObject(3, offset);
            count.setInt(1, id);

            try (ResultSet rs = original.executeQuery();
                 ResultSet countRs = count.executeQuery()) {
                while (rs.next())
                    entries.add(getMapper().map(rs));
                if (countRs.next())
                    totalValues = countRs.getInt(1);
            }
        } catch (SQLException ex) {
            logger.error("cannot retrieve objects from table dish", ex);
        }

        int totalPages = totalValues / limit;
        if (totalValues % limit != 0)
            totalPages++;
        return new Page<>(index, totalPages, entries);
    }

    @Override
    public Page<Dish> findAll(int limit, int index) {
        if (limit < 0 || index < 1)
            throw new IllegalArgumentException();

        String sql = "select d.*, c.name as category_name, c.name_ukr as category_name_ukr " +
                "from dish d inner join category c " +
                "on c.id=d.category_id " +
                "limit ? offset ?";
        logger.info("sql: " + sql);
        return getDishPage(limit, index, sql);
    }

    @Override
    public Page<Dish> findAllSorted(int limit, int index, String sortBy) {
        if (limit < 0 || index < 1)
            throw new IllegalArgumentException();

        String sql = String.format("select d.*, c.name as category_name, c.name_ukr as category_name_ukr " +
                "from dish d inner join category c " +
                "on c.id=d.category_id " +
                "order by %s limit ? offset ?", sortBy);
        logger.info("sql: " + sql);
        return getDishPage(limit, index, sql);
    }

    @Override
    public List<Dish> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Dish> findById(int id) {
        if (id < 0)
            return Optional.empty();

        String sql = "select d.*, c.name as category_name, c.name_ukr as category_name_ukr " +
                "from dish d inner join category c " +
                "on c.id=d.category_id " +
                "where d.id=?";

        logger.info("sql: " + sql);
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(getMapper().map(rs));
            }
        } catch (SQLException ex) {
            logger.error("cannot get object from table dish", ex);
        }

        return Optional.empty();
    }

    @Override
    public void save(Dish dish) {
        if (dish == null || dish.getCategory() == null) {
            logger.warn("cannot save null object");
            return;
        }

        String sql = "insert into dish(name, name_ukr, price, description, image_path, category_id)" +
                " values (?, ?, ?, ?, ?, ?)";
        logger.info("sql: " + sql);
        Connection con = null;
        try {
            con = dbManager.getConnection();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            con.setAutoCommit(false);
            setParameters(dish, ps);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    dish.setId(keys.getInt(1));
                }
            }
        } catch (SQLException ex) {
            logger.error("exception occurred during statement execution", ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

    @Override
    public void update(Dish dish) {
        if (dish == null || dish.getCategory() == null) {
            logger.warn("cannot update null object");
            return;
        }

        String sql = "update dish set name=?, name_ukr=?, price=?, description=?, image_path=?, category_id=?" +
                "where id=?";
        logger.info("sql: " + sql);
        Connection con = null;
        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql);
            setParameters(dish, ps);
            ps.setInt(7, dish.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error("exception occurred during statement execution", ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "delete from dish where id=?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        Connection con = null;
        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.error(String.format("cannot delete row{id=%s} from table dish", id), ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

    private Page<Dish> getDishPage(int limit, int index, String sql) {
        String countTotal = "select count(*) from dish";
        logger.info("sql: " + countTotal);

        int offset = limit * (index - 1);
        List<Dish> entries = new ArrayList<>();
        int totalValues = 0;

        try (Connection con = dbManager.getConnection();
             PreparedStatement original = con.prepareStatement(sql);
             PreparedStatement count = con.prepareStatement(countTotal)) {

            original.setObject(1, limit);
            original.setObject(2, offset);

            try (ResultSet rs = original.executeQuery();
                 ResultSet countRs = count.executeQuery()) {
                while (rs.next())
                    entries.add(getMapper().map(rs));
                if (countRs.next())
                    totalValues = countRs.getInt(1);
            }
        } catch (SQLException ex) {
            logger.error("cannot retrieve objects from table dish", ex);
        }

        int totalPages = totalValues / limit;
        if (totalValues % limit != 0)
            totalPages++;
        return new Page<>(index, totalPages, entries);
    }

    private void setParameters(Dish dish, PreparedStatement ps) throws SQLException {
        ps.setString(1, dish.getName());
        ps.setString(2, dish.getNameUkr());
        ps.setInt(3, dish.getPrice());
        ps.setString(4, dish.getDescription());
        ps.setString(5, dish.getDescriptionUkr());
        ps.setInt(6, dish.getCategory().getId());
    }

    private static DishMapper getMapper() {
        return new DishMapper("id", "name", "name_ukr", "price",
                "description", "description_ukr", "image_path",
                new CategoryMapper("category_id", "category_name",
                        "category_name_ukr"));
    }

}
