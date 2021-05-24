package model.dao.impl;

import model.dao.RequestItemDao;
import model.database.DBManager;
import model.entities.Dish;
import model.entities.RequestItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public final class RequestItemDaoImpl implements RequestItemDao {

    private static final String TABLE_NAME = "request_item";
    private static final Logger logger = LogManager.getLogger(RequestItemDaoImpl.class);
    public static final String JOIN_STATEMENT = "select ri.*, d.name, d.name_ukr, d.image_path " +
            "from request_item ri inner join dish d on ri.dish_id=d.id ";
    private final DaoUtils<RequestItem> daoUtils;
    private final DBManager dbManager;

    public RequestItemDaoImpl(DBManager dbManager) {
        this.dbManager = dbManager;
        daoUtils = new DaoUtils<>(dbManager, TABLE_NAME, rs -> {
            RequestItem requestItem = new RequestItem();
            requestItem.setId(rs.getInt("id"));
            requestItem.setRequestId(rs.getInt("request_id"));
            Dish dish = new Dish();
            dish.setId(rs.getInt("dish_id"));
            dish.setImagePath(rs.getString("image_path"));
            dish.setName(rs.getString("name"));
            dish.setNameUkr(rs.getString("name_ukr"));
            requestItem.setDish(dish);
            requestItem.setQuantity(rs.getInt("quantity"));
            requestItem.setPrice(rs.getInt("price"));
            requestItem.setCreatedAt(rs.getDate("created_at"));
            return requestItem;
        });
    }

    @Override
    public List<RequestItem> findAllByRequestId(int id) {
        String sql = JOIN_STATEMENT + "where ri.request_id=?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getList(sql, id);
    }

    @Override
    public List<RequestItem> findAll() {
        String sql = JOIN_STATEMENT;
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getList(sql);
    }

    @Override
    public Optional<RequestItem> findById(int id) {
        String sql = JOIN_STATEMENT + "where ri.id=?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getOptional(sql, id);
    }

    @Override
    public void save(RequestItem requestItem) {
        if (requestItem == null) {
            logger.warn("cannot save null object");
            return;
        }

        String sql = "insert into request_item(request_id, dish_id, quantity) values()";
        Integer dishId = requestItem.getDish() != null ? requestItem.getDish().getId() : null;
        logger.trace("delegated '" + sql + "' to DaoUtils");
        daoUtils.save(requestItem, sql, requestItem.getRequestId(), dishId, requestItem.getQuantity());
    }

    @Override
    public void addRequestItem(int userId, int dishId) {
        String findAllItems = "select id, quantity from request_item where dish_id=? and request_id=?";
        String updateCount = "update request_item set quantity=? where id=?";
        String insertItem = "insert into request_item(request_id, dish_id, quantity) values(?, ?, ?)";

        Connection con = null;
        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            int requestId = getRequestId(userId, con);

            PreparedStatement findAllItemsPs = con.prepareStatement(findAllItems);
            findAllItemsPs.setInt(1, dishId);
            findAllItemsPs.setInt(2, requestId);
            try (ResultSet rs = findAllItemsPs.executeQuery()) {
                PreparedStatement ps;
                if (rs.next()) {
                    int itemId = rs.getInt("id");
                    int count = rs.getInt("quantity");
                    ps = con.prepareStatement(updateCount);
                    ps.setInt(1, count + 1);
                    ps.setInt(2, itemId);
                } else {
                    ps = con.prepareStatement(insertItem);
                    ps.setInt(1, requestId);
                    ps.setInt(2, dishId);
                    ps.setInt(3, 1);
                }
                ps.executeUpdate();
                ps.close();
            }
            findAllItemsPs.close();
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
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setUpdate("quantity").setWhere("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        daoUtils.update(sql, requestItem.getQuantity(), requestItem.getId());
    }

    @Override
    public void deleteById(int id) {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setDelete().setWhere("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        daoUtils.deleteById(id, sql);
    }

    @Override
    public void delete(RequestItem requestItem) {
        if (requestItem == null) {
            logger.warn("Cannot delete null object in from table " + TABLE_NAME);
            return;
        }
        deleteById(requestItem.getId());
    }

    private int getRequestId(int userId, Connection con) throws SQLException {
        String getRequest = "select id from request where customer_id=? and status_id=0";
        String insertRequest = "insert into request(customer_id, status_id) values (?, ?)";

        PreparedStatement getRequestPs = con.prepareStatement(getRequest);
        getRequestPs.setInt(1, userId);
        logger.info("sql: " + getRequest);
        int requestId = 0;

        try (ResultSet rs = getRequestPs.executeQuery()) {
            if (rs.next()) {
                requestId = rs.getInt("id");
            } else {
                PreparedStatement insertRequestPs = con.prepareStatement(insertRequest,
                        Statement.RETURN_GENERATED_KEYS);
                logger.info("sql: " + insertRequest);
                insertRequestPs.setInt(1, userId);
                insertRequestPs.setInt(2, 0);
                insertRequestPs.executeUpdate();
                try (ResultSet keys = insertRequestPs.getGeneratedKeys()) {
                    if (keys.next()) {
                        requestId = keys.getInt(1);
                    }
                }
                insertRequestPs.close();
            }
        }
        getRequestPs.close();
        return requestId;
    }
}
