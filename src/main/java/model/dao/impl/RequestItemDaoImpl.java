package model.dao.impl;

import model.dao.RequestItemDao;
import model.dao.mapper.CategoryMapper;
import model.dao.mapper.DishMapper;
import model.dao.mapper.RequestItemMapper;
import model.database.DBManager;
import model.entities.RequestItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class RequestItemDaoImpl implements RequestItemDao {

    private static final Logger logger = LogManager.getLogger(RequestItemDaoImpl.class);
    public static final String JOIN_STATEMENT = "select ri.*, d.name, d.name_ukr, d.image_path, " +
            "d.description, d.description_ukr, d.image_path, d.category_id, " +
            "c.name as category_name, c.name_ukr as category_name_ukr " +
            "from request_item ri inner join dish d on ri.dish_id=d.id " +
            "inner join category c on c.id=d.category_id ";

    private final DBManager dbManager;

    public RequestItemDaoImpl(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public List<RequestItem> findAllByRequestId(int id) {
        String sql = JOIN_STATEMENT + "where ri.request_id=?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        List<RequestItem> entries = new ArrayList<>();

        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    entries.add(getMapper().map(rs));
            }
        } catch (SQLException ex) {
            logger.error("cannot obtain objects from table request_item", ex);
        }
        return entries;
    }

    @Override
    public List<RequestItem> findAll() {
        String sql = JOIN_STATEMENT;
        logger.trace("delegated '" + sql + "' to DaoUtils");
        List<RequestItem> entries = new ArrayList<>();

        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    entries.add(getMapper().map(rs));
            }
        } catch (SQLException ex) {
            logger.error("cannot obtain objects from table request_item", ex);
        }
        return entries;
    }

    @Override
    public Optional<RequestItem> findById(int id) {
        String sql = JOIN_STATEMENT + "where ri.id=?";
        logger.info("sql: " + sql);
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(getMapper().map(rs));
            }
        } catch (SQLException ex) {
            logger.error("cannot get object from table request_item", ex);
        }

        return Optional.empty();
    }

    @Override
    public void save(RequestItem requestItem) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addRequestItem(int userId, int dishId) {
        String sql = "call insert_order_item(?, ?)";
        logger.info(sql);
        Connection con = null;
        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, dishId);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            logger.error("exception occurred during statement execution", ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

    @Override
    public void increaseQuantity(int requestId) {
        String sql = "update request_item set quantity=quantity+1 where id=?";
        updateQty(requestId, sql);
    }

    @Override
    public void decreaseQuantity(int requestId) {
        String sql = "update request_item set quantity=quantity-1 where id=?";
        updateQty(requestId, sql);
    }

    private void updateQty(int requestId, String sql) {
        logger.info("sql: " + sql);
        Connection con = null;
        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, requestId);
            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error("exception occurred during statement execution", ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

    @Override
    public void update(RequestItem requestItem) {
        if (requestItem == null) {
            logger.warn("cannot update null object");
            return;
        }
        String sql = "update request_item set quantity=? where id=?";
        logger.info("sql: " + sql);
        Connection con = null;
        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, requestItem.getQuantity());
            ps.setInt(2, requestItem.getId());
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
        String sql = "delete from request_item where id=?";
        logger.info("sql: " + sql);
        Connection con = null;

        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.error(String.format("cannot delete row{id=%s} from table request_item", id), ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

    private static RequestItemMapper getMapper() {
        DishMapper dishMapper = new DishMapper("dish_id", "name", "name_ukr", "price",
                "description", "description_ukr", "image_path",
                new CategoryMapper("category_id", "category_name",
                        "category_name_ukr"));
        return new RequestItemMapper("id", "request_id", "quantity",
                "price", "created_at", dishMapper);
    }
}
