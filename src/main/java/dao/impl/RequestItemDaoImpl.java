package dao.impl;

import dao.DishDao;
import dao.RequestItemDao;
import database.DBManager;
import entities.RequestItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class RequestItemDaoImpl extends AbstractDao<RequestItem> implements RequestItemDao {

    private static final String TABLE_NAME = "request_item";

    public RequestItemDaoImpl(DBManager dbManager) {
        super(dbManager, TABLE_NAME, new Mapper(dbManager));
    }

    @Override
    public List<RequestItem> findAllByRequestId(int id) {
        if (id <= 0) return List.of();
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        s.select().where("request_id");
        return super.getList(s.build(), id);
    }

    @Override
    public void save(RequestItem requestItem) {
        if (requestItem == null) return;
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        s.insert("request_id", "dish_id", "quantity");
        Object dishId = requestItem.getDish() != null ? requestItem.getDish().getId() : null;
        super.save(requestItem, s.build(), requestItem.getRequestId(), dishId, requestItem.getQuantity());
    }

    @Override
    public void update(RequestItem requestItem) {
        if (requestItem == null) return;
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        s.update("quantity").where("id");
        super.update(requestItem.getId(), s.build(), requestItem.getQuantity());
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
