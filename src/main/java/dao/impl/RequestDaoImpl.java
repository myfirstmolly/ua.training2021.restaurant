package dao.impl;

import dao.RequestDao;
import dao.RequestItemDao;
import dao.UserDao;
import database.DBManager;
import entities.Request;
import entities.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Page;

import java.util.List;

public final class RequestDaoImpl extends DaoUtils<Request> implements RequestDao {

    private static final String TABLE_NAME = "request";
    private static final Logger logger = LogManager.getLogger(RequestDaoImpl.class);

    public RequestDaoImpl(DBManager dbManager) {
        super(dbManager, TABLE_NAME, rs -> {
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
        });
    }

    @Override
    public Page<Request> findAll(int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setSelect().setLimit().setOrderBy("id desc").setOffset().build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return super.getPage(limit, index, sql);
    }

    @Override
    public Page<Request> findAllByUserId(int id, int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setSelect().setWhere("customer_id").setOrderBy("id desc").setLimit().setOffset().build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return super.getPage(limit, index, sql, id);
    }

    @Override
    public Page<Request> findAllByStatusId(int id, int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setSelect().setWhere("status_id").setOrderBy("id desc").setLimit().setOffset().build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return super.getPage(limit, index, sql, id);
    }

    @Override
    public List<Request> findAllByUserAndStatus(int userId, int statusId) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setSelect().setWhere("customer_id", "status_id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return super.getList(sql, userId, statusId);
    }

    @Override
    public void save(Request request) {
        if (request == null) {
            logger.warn("cannot save null object");
            return;
        }
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setInsert("customer_id", "status_id", "delivery_address").build();
        Integer customer = request.getCustomer() == null ? null : request.getCustomer().getId();
        Integer status = request.getStatus() == null ? null : request.getStatus().toInt();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        super.save(request, sql, customer, status, request.getDeliveryAddress());
    }

    @Override
    public void update(Request request) {
        if (request == null) {
            logger.warn("cannot update null object");
            return;
        }
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setUpdate("customer_id", "status_id", "delivery_address", "approved_by").setWhere("id")
                .build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        Integer customer = request.getCustomer() == null ? null : request.getCustomer().getId();
        Integer status = request.getStatus() == null ? null : request.getStatus().toInt();
        Integer approvedBy = request.getApprovedBy() == null ? null : request.getApprovedBy().getId();
        super.update(sql, customer, status, request.getDeliveryAddress(), approvedBy, request.getId());
    }
}
