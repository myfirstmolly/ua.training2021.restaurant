package model.dao.impl;

import model.dao.CategoryDao;
import model.dao.mapper.CategoryMapper;
import model.database.DBManager;
import model.entities.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CategoryDaoImpl implements CategoryDao {

    private static final Logger logger = LogManager.getLogger(CategoryDaoImpl.class);

    private final DBManager dbManager;

    public CategoryDaoImpl(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public List<Category> findAll() {
        String sql = "select * from category";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        logger.info("sql: " + sql);
        List<Category> entries = new ArrayList<>();

        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    entries.add(getMapper().map(rs));
            }
        } catch (SQLException ex) {
            logger.error("cannot obtain objects from table category", ex);
        }
        return entries;
    }

    @Override
    public Optional<Category> findById(int id) {
        String sql = "select * from category where id=?";
        logger.info("sql: " + sql);

        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(getMapper().map(rs));
            }
        } catch (SQLException ex) {
            logger.error("cannot get object from table category", ex);
        }
        return Optional.empty();
    }

    @Override
    public void save(Category category) {
        if (category == null) {
            logger.warn("cannot save null object");
            return;
        }
        String sql = "insert into category(name, name_ukr) values (?,?)";
        logger.info("sql: " + sql);

        Connection con = null;
        try {
            con = dbManager.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            con.setAutoCommit(false);
            ps.setString(1, category.getName());
            ps.setString(2, category.getNameUkr());

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    category.setId(keys.getInt(1));
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
    public void update(Category category) {
        if (category == null) {
            logger.warn("cannot update null object");
            return;
        }

        String sql = "update category set name=?, name_ukr=? where id=?";
        logger.info("sql: " + sql);
        Connection con = null;
        try {
            con = dbManager.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, category.getName());
            ps.setString(2, category.getNameUkr());
            ps.setInt(3, category.getId());
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
        if (id <= 0) return;

        String sql = "delete from category where id=?";
        logger.info("sql: " + sql);

        Connection con = null;
        try {
            con = dbManager.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.error(String.format("cannot delete row{id=%s} from table category", id), ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

    public static CategoryMapper getMapper() {
        return new CategoryMapper("id", "name", "name_ukr");
    }

}
