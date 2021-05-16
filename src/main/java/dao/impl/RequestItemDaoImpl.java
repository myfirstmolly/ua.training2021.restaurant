package dao.impl;

import dao.DishDao;
import dao.RequestItemDao;
import database.DBManager;
import entities.RequestItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public final class RequestItemDaoImpl extends DaoUtils<RequestItem> implements RequestItemDao {

    private static final String TABLE_NAME = "request_item";
    private static final Logger logger = LogManager.getLogger(RequestItemDaoImpl.class);

    public RequestItemDaoImpl(DBManager dbManager) {
        super(dbManager, TABLE_NAME, rs -> {
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
        return super.getList(sql, id);
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
        super.save(requestItem, sql, requestItem.getRequestId(), dishId, requestItem.getQuantity());
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
        super.update(sql, requestItem.getQuantity(), requestItem.getId());
    }
}
