package dao.impl;

import dao.RequestDao;
import dao.RequestItemDao;
import dao.UserDao;
import database.DBManager;
import entities.Request;
import entities.Status;
import util.Page;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class RequestDaoImpl extends AbstractDao<Request> implements RequestDao {

    private static final String TABLE_NAME = "request";

    public RequestDaoImpl(DBManager dbManager) {
        super(dbManager, TABLE_NAME, new Mapper(dbManager));
    }

    @Override
    public Page<Request> findAll(int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        s.select().limit().offset();
        return super.getPage(limit, index, s.build());
    }

    @Override
    public Page<Request> findAllByUserId(int id, int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        s.select().where("customer_id").orderBy("updated_at desc").limit().offset();
        return super.getPage(limit, index, s.build(), id);
    }

    @Override
    public Page<Request> findAllByStatusId(int id, int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        s.select().where("status_id").orderBy("updated_at desc").limit().offset();
        return super.getPage(limit, index, s.build(), id);
    }

    @Override
    public List<Request> findAllByUserAndStatus(int userId, int statusId) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        s.select().where("customer_id", "status_id");
        return super.getList(s.build(), userId, statusId);
    }

    @Override
    public void save(Request request) {
        if (request == null) return;
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        s.insert("customer_id", "status_id", "delivery_address");
        Object customer = request.getCustomer() == null ? null : request.getCustomer().getId();
        Object status = request.getStatus() == null ? null : request.getStatus().toInt();
        super.save(request, s.build(), customer, status, request.getDeliveryAddress());
    }

    @Override
    public void update(Request request) {
        if (request == null) return;
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        s.update("customer_id", "status_id", "delivery_address", "approved_by").where("id");
        Integer customer = request.getCustomer() == null ? null : request.getCustomer().getId();
        Integer status = request.getStatus() == null ? null : request.getStatus().toInt();
        Integer approvedBy = request.getApprovedBy() == null ? null : request.getApprovedBy().getId();
        super.update(request.getId(), s.build(), customer, status, request.getDeliveryAddress(), approvedBy);
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
