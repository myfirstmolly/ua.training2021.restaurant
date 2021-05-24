package model.dao.impl;

import model.dao.RequestDao;
import model.database.DBManager;
import model.entities.Request;
import model.entities.Status;
import model.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Page;

import java.util.List;
import java.util.Optional;

public final class RequestDaoImpl implements RequestDao {

    private static final String TABLE_NAME = "request";
    private static final Logger logger = LogManager.getLogger(RequestDaoImpl.class);
    private final DaoUtils<Request> daoUtils;
    private static final String JOIN_STRING = "select r.*, u.username, u.phone_number from request r join user u" +
            " on r.customer_id=u.id ";

    public RequestDaoImpl(DBManager dbManager) {
        daoUtils = new DaoUtils<>(dbManager, TABLE_NAME, rs -> {
            Request request = new Request();
            request.setId(rs.getInt("id"));
            User user = new User();
            user.setId(rs.getInt("customer_id"));
            user.setUsername(rs.getString("username"));
            user.setPhoneNumber(rs.getString("phone_number"));
            request.setCustomer(user);
            request.setStatus(Status.values()[rs.getInt("status_id")]);
            request.setDeliveryAddress(rs.getString("delivery_address"));
            request.setTotalPrice(rs.getLong("total_price"));
            request.setApprovedBy(rs.getInt("approved_by"));
            request.setCreatedAt(rs.getDate("created_at"));
            request.setUpdatedAt(rs.getDate("updated_at"));
            return request;
        });
    }

    @Override
    public Page<Request> findAll(int limit, int index) {
        String sql = JOIN_STRING + "order by id desc limit ? offset ?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getPage(limit, index, sql);
    }

    @Override
    public Page<Request> findAllByUserId(int id, int limit, int index) {
        String sql = JOIN_STRING + "where customer_id=? order by id desc limit ? offset ?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getPage(limit, index, sql, id);
    }

    @Override
    public Page<Request> findAllWhereStatusNotEqual(Status status, int limit, int index) {
        String sql = JOIN_STRING + "where status_id!=? order by id desc limit ? offset ?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getPage(limit, index, sql, status.getId());
    }

    @Override
    public Page<Request> findAllByStatusId(int id, int limit, int index) {
        String sql = JOIN_STRING + "where status_id=? order by id desc limit ? offset ?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getPage(limit, index, sql, id);
    }

    @Override
    public Page<Request> findAllByUserAndStatus(int userId, int statusId, int limit, int index) {
        String sql = JOIN_STRING + "where customer_id=? and status_id=? order by id desc limit ? offset ?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getPage(limit, index, sql, userId, statusId);
    }

    @Override
    public Optional<Request> findFirstByUserAndStatus(int userId, int statusId) {
        String sql = JOIN_STRING + "where customer_id=? and status_id=?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getOptional(sql, userId, statusId);
    }

    @Override
    public List<Request> findAll() {
        String sql = JOIN_STRING;
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getList(sql);
    }

    @Override
    public Optional<Request> findById(int id) {
        String sql = JOIN_STRING + "where r.id=?";
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
        Integer status = request.getStatus() == null ? null : request.getStatus().getId();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        daoUtils.save(request, sql, request.getCustomer().getId(), status, request.getDeliveryAddress());
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
        Integer status = request.getStatus() == null ? null : request.getStatus().getId();
        Integer approvedBy = request.getApprovedBy() == null || request.getApprovedBy() == 0 ?
                null : request.getApprovedBy();
        daoUtils.update(sql, request.getCustomer().getId(), status, request.getDeliveryAddress(), approvedBy,
                request.getId());
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
