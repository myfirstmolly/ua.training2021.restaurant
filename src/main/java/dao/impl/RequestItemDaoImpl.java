package dao.impl;

import dao.DishDao;
import dao.RequestItemDao;
import database.DBManager;
import entities.RequestItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class RequestItemDaoImpl extends AbstractDao<RequestItem> implements RequestItemDao {

    public RequestItemDaoImpl(DBManager dbManager) {
        super(dbManager, "request_item", new Mapper(dbManager));
    }

    @Override
    public List<RequestItem> findAllByRequestId(int id) {
        if (id <= 0) return List.of();
        return super.findAllByParameter(new Param(id, "request_id"));
    }

    @Override
    public void save(RequestItem requestItem) {
        if (requestItem == null) return;
        Param requestId = new Param(requestItem.getRequestId(), "request_id");
        Param dishId = new Param(requestItem.getDish() != null ? requestItem.getDish().getId() : null,
                "dish_id");
        Param quantity = new Param(requestItem.getQuantity(), "quantity");
        super.save(requestItem, requestId, dishId, quantity);
    }

    @Override
    public void update(RequestItem requestItem) {
        if (requestItem == null) return;
        Param quantity = new Param(requestItem.getQuantity(), "quantity");
        super.update(requestItem.getId(), quantity);
    }

    private static class Mapper implements AbstractDao.Mapper<RequestItem> {

        private final DBManager dbManager;

        public Mapper(DBManager dbManager) {
            this.dbManager = dbManager;
        }

        @Override
        public RequestItem map(ResultSet rs) throws SQLException {
            RequestItem requestItem = new RequestItem();
            requestItem.setId(rs.getInt("id"));
            requestItem.setRequestId(rs.getInt("request_id"));
            DishDao dishDao = new DishDaoImpl(dbManager);
            requestItem.setDish(dishDao.findById(rs.getInt("dish_id")).get());
            requestItem.setQuantity(rs.getInt("quantity"));
            requestItem.setPrice(rs.getInt("price"));
            requestItem.setCreatedAt(rs.getDate("created_at"));
            return requestItem;
        }

    }

}
