package dao.impl;

import dao.RequestItemDao;
import dao.DishDao;
import database.DBManager;
import entities.RequestItem;
import exceptions.DataIntegrityViolationException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class RequestItemDaoImpl extends AbstractDao<RequestItem> implements RequestItemDao {

    public RequestItemDaoImpl(DBManager dbManager) {
        super(dbManager, "request_item", new Mapper(dbManager));
        super.saveStmt = "insert into request_item (request_id, dish_id, quantity) values (?, ?, ?)";
        super.updateStmt = "update request_item set quantity=? where id=?";
    }

    @Override
    public List<RequestItem> findAllByRequestId(int id) {
        if (id <= 0) return List.of();
        return super.findAllByParameter(new Param(id, "request_id"));
    }

    private static class Mapper implements AbstractDao.Mapper<RequestItem> {
        private final DBManager dbManager;

        public Mapper(DBManager dbManager) {
            this.dbManager = dbManager;
        }

        @Override
        public void setSaveStatementParams(RequestItem requestItem, PreparedStatement ps) throws SQLException {
            if (requestItem.getRequestId() <= 0 || requestItem.getDish() == null || requestItem.getQuantity() <= 0)
                throw new DataIntegrityViolationException();

            ps.setInt(1, requestItem.getRequestId());
            ps.setInt(2, requestItem.getDish().getId());
            ps.setInt(3, requestItem.getQuantity());
        }

        @Override
        public void setUpdateStatementParams(RequestItem requestItem, PreparedStatement ps) throws SQLException {
            if (requestItem.getQuantity() <= 0)
                throw new DataIntegrityViolationException();

            ps.setInt(1, requestItem.getQuantity());
            ps.setInt(2, requestItem.getId());
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
