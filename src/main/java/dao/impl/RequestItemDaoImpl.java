package dao.impl;

import dao.DishDao;
import dao.RequestItemDao;
import database.DBManager;
import entities.RequestItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public final class RequestItemDaoImpl implements RequestItemDao {

    private static final String TABLE_NAME = "request_item";
    private static final Logger logger = LogManager.getLogger(RequestItemDaoImpl.class);
    private final DaoUtils<RequestItem> daoUtils;

    public RequestItemDaoImpl(DBManager dbManager) {
        daoUtils = new DaoUtils<>(dbManager, TABLE_NAME, rs -> {
            RequestItem requestItem = new RequestItem();
            requestItem.setId(rs.getInt("id"));
            requestItem.setRequestId(rs.getInt("request_id"));
            DishDao dishDao = new DishDaoImpl(dbManager);
            requestItem.setDish(dishDao.findById(rs.getInt("dish_id")).get());
            requestItem.setQuantity(rs.getInt("quantity"));
            requestItem.setPrice(rs.getInt("price"));
            requestItem.setCreatedAt(rs.getDate("created_at"));
            return requestItem;
        });
    }

    @Override
    public List<RequestItem> findAllByRequestId(int id) {
        if (id <= 0) return List.of();
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setSelect().setWhere("request_id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getList(sql, id);
    }

    @Override
    public List<RequestItem> findAll() {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setSelect().build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getList(sql);
    }

    @Override
    public Optional<RequestItem> findById(int id) {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setSelect().setWhere("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getOptional(sql, id);
    }

    @Override
    public void save(RequestItem requestItem) {
        if (requestItem == null) {
            logger.warn("cannot save null object");
            return;
        }
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setInsert("request_id", "dish_id", "quantity").build();
        Integer dishId = requestItem.getDish() != null ? requestItem.getDish().getId() : null;
        logger.trace("delegated '" + sql + "' to DaoUtils");
        daoUtils.save(requestItem, sql, requestItem.getRequestId(), dishId, requestItem.getQuantity());
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
}
