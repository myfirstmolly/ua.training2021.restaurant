package dao.impl;

import dao.RequestDao;
import dao.RequestItemDao;
import dao.UserDao;
import database.DBManager;
import entities.Request;
import entities.Status;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class RequestDaoImpl extends AbstractDao<Request> implements RequestDao {

    public RequestDaoImpl(DBManager dbManager) {
        super(dbManager, "request", new Mapper(dbManager));
    }

    @Override
    public List<Request> findAll(int limit, int offset) {
        return super.findAllByParameter(limit, offset);
    }

    @Override
    public List<Request> findAllByUserId(int id, int limit, int offset) {
        return super.findAllByParameter(limit, offset, new Param(id, "customer_id"));
    }

    @Override
    public List<Request> findAllByStatusId(int id, int limit, int offset) {
        return super.findAllByParameter(limit, offset, new Param(id, "status_id"));
    }

    @Override
    public List<Request> findAllByUserAndStatus(int userId, int statusId) {
        return super.findAllByParameter(
                new Param(userId, "customer_id"),
                new Param(statusId, "status_id")
        );
    }

    @Override
    public void save(Request request) {
        if (request == null) return;
        super.save(request, getParams(request));
    }

    @Override
    public void update(Request request) {
        if (request == null) return;
        Param approvedBy;

        if (request.getApprovedBy() != null)
            approvedBy = new Param(request.getApprovedBy().getId(), "approved_by");
        else approvedBy = new Param(null, "approved_by");

        Param [] params = new Param[getParams(request).length + 1];
        System.arraycopy(getParams(request), 0, params, 0, getParams(request).length);
        params[getParams(request).length] = approvedBy;
        super.update(request.getId(), params);
    }

    private Param [] getParams(Request request) {
        Param customerId = new Param(request.getCustomer() != null ? request.getCustomer().getId() : null,
                "customer_id");
        Param statusId = new Param(request.getStatus() != null ? request.getStatus().toInt() : null,
                "status_id");
        Param deliveryAddress = new Param(request.getDeliveryAddress(), "delivery_address");
        return new Param[] {customerId, statusId, deliveryAddress};
    }

    private static class Mapper implements AbstractDao.Mapper<Request> {

        private final DBManager dbManager;

        public Mapper(DBManager dbManager) {
            this.dbManager = dbManager;
        }

        @Override
        public Request map(ResultSet rs) throws SQLException {
            Request request = new Request();
            request.setId(rs.getInt("id"));
            UserDao userDao = new UserDaoImpl(dbManager);
            request.setCustomer(userDao.findById(rs.getInt("customer_id")).get());
            request.setStatus(Status.values()[rs.getInt("status_id")]);
            request.setDeliveryAddress(rs.getString("delivery_address"));
            request.setTotalPrice(rs.getLong("total_price"));
            request.setApprovedBy(userDao.findById(rs.getInt("approved_by")).orElse(null));
            request.setCreatedAt(rs.getDate("created_at"));
            request.setUpdatedAt(rs.getDate("updated_at"));
            RequestItemDao requestItemDao = new RequestItemDaoImpl(dbManager);
            request.setRequestItems(requestItemDao.findAllByRequestId(request.getId()));
            return request;
        }
    }
}
