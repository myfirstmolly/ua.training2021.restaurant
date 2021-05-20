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
import java.util.Optional;

public final class RequestDaoImpl implements RequestDao {

    private static final String TABLE_NAME = "request";
    private static final Logger logger = LogManager.getLogger(RequestDaoImpl.class);
    private final DaoUtils<Request> daoUtils;

    public RequestDaoImpl(DBManager dbManager) {
        daoUtils = new DaoUtils<>(dbManager, TABLE_NAME, rs -> {
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
        return daoUtils.getPage(limit, index, sql);
    }

    @Override
    public Page<Request> findAllByUserId(int id, int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setSelect().setWhere("customer_id").setOrderBy("id desc").setLimit().setOffset().build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getPage(limit, index, sql, id);
    }

    @Override
    public Page<Request> findAllWhereStatusNotEqual(Status status, int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setSelect().setWhere("status_id!").setOrderBy("id desc").setLimit().setOffset().build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getPage(limit, index, sql, status.toInt());
    }

    @Override
    public Page<Request> findAllByStatusId(int id, int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setSelect().setWhere("status_id").setOrderBy("id desc").setLimit().setOffset().build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getPage(limit, index, sql, id);
    }

    @Override
    public Page<Request> findAllByUserAndStatus(int userId, int statusId, int limit, int index) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setSelect().setWhere("customer_id", "status_id").setLimit().setOffset().build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getPage(limit, index, sql, userId, statusId);
    }

    @Override
    public Optional<Request> findFirstByUserAndStatus(int userId, int statusId) {
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setSelect().setWhere("customer_id", "status_id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getOptional(sql, userId, statusId);
    }

    @Override
    public List<Request> findAll() {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setSelect().build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getList(sql);
    }

    @Override
    public Optional<Request> findById(int id) {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setSelect().setWhere("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getOptional(sql, id);
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
        daoUtils.save(request, sql, customer, status, request.getDeliveryAddress());
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
        daoUtils.update(sql, customer, status, request.getDeliveryAddress(), approvedBy, request.getId());
    }

    @Override
    public void deleteById(int id) {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setDelete().setWhere("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        daoUtils.deleteById(id, sql);
    }

    @Override
    public void delete(Request request) {
        if (request == null) {
            logger.warn("Cannot delete null object in from table " + TABLE_NAME);
            return;
        }
        deleteById(request.getId());
    }
}
