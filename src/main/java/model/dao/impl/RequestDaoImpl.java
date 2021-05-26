package model.dao.impl;

import model.dao.RequestDao;
import model.dao.mapper.RequestMapper;
import model.dao.mapper.UserMapper;
import model.database.DBManager;
import model.entities.Request;
import model.entities.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class RequestDaoImpl implements RequestDao {

    private static final Logger logger = LogManager.getLogger(RequestDaoImpl.class);
    private final DBManager dbManager;
    private static final String JOIN_STRING = "select r.*, u.username, u.password, u.name, " +
            "u.phone_number, u.email, u.role_id " +
            "from request r join user u" +
            " on r.customer_id=u.id ";

    public RequestDaoImpl(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public Page<Request> findAll(int limit, int index) {
        if (limit < 0 || index < 1)
            throw new IllegalArgumentException();

        String sql = JOIN_STRING + "order by id desc limit ? offset ?";
        logger.info("sql: " + sql);

        String countTotal = "select count(*) from request;";
        logger.info("sql: " + countTotal);

        return getRequestPage(limit, index, sql, countTotal);
    }

    @Override
    public Page<Request> findAllByUserId(int id, int limit, int index) {
        String sql = JOIN_STRING + "where customer_id=? order by id desc limit ? offset ?";
        logger.info("sql: " + sql);

        String countTotal = "select count(*) from request where customer_id=?;";
        return getRequestPage(limit, index, sql, countTotal, id);
    }

    @Override
    public Page<Request> findAllWhereStatusNotEqual(Status status, int limit, int index) {
        String sql = JOIN_STRING + "where status_id!=? order by id desc limit ? offset ?";
        logger.info("sql: " + sql);
        int id = status.getId();

        String countTotal = "select count(*) from request where status_id!=?;";
        return getRequestPage(limit, index, sql, countTotal, id);
    }

    @Override
    public Page<Request> findAllByStatusId(int id, int limit, int index) {
        String sql = JOIN_STRING + "where status_id=? order by id desc limit ? offset ?";
        logger.info("sql: " + sql);

        String countTotal = "select count(*) from request where status_id=?;";
        return getRequestPage(limit, index, sql, countTotal, id);
    }

    @Override
    public Page<Request> findAllByUserAndStatus(int userId, int statusId, int limit, int index) {
        String sql = JOIN_STRING + "where customer_id=? and status_id=? order by id desc limit ? offset ?";
        logger.info("sql: " + sql);
        String countTotal = "select count(*) from request where customer_id=? and status_id=?";
        logger.info("sql: " + countTotal);
        return getRequestPage(limit, index, sql, countTotal, userId, statusId);
    }

    @Override
    public Optional<Request> findFirstByUserAndStatus(int userId, int statusId) {
        String sql = JOIN_STRING + "where customer_id=? and status_id=?";
        logger.info("sql: " + sql);
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, statusId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(getMapper().map(rs));
            }
        } catch (SQLException ex) {
            logger.error("cannot get object from table request", ex);
        }

        return Optional.empty();
    }

    @Override
    public List<Request> findAll() {
        String sql = JOIN_STRING;
        logger.info("sql: " + sql);
        List<Request> entries = new ArrayList<>();

        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    entries.add(getMapper().map(rs));
            }
        } catch (SQLException ex) {
            logger.error("cannot obtain objects from table request", ex);
        }
        return entries;
    }

    @Override
    public Optional<Request> findById(int id) {
        String sql = JOIN_STRING + "where r.id=?";
        logger.info("sql: " + sql);
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(getMapper().map(rs));
            }
        } catch (SQLException ex) {
            logger.error("cannot get object from table request", ex);
        }

        return Optional.empty();
    }

    @Override
    public void save(Request request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Request request) {
        if (request == null) {
            logger.warn("cannot update null object");
            return;
        }
        String sql = "update request set status_id=?, delivery_address=?, approved_by=? where id=?";
        logger.trace("delegated '" + sql + "' to DaoUtils");

        logger.info("sql: " + sql);
        Connection con = null;
        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, request.getStatus().getId());
            ps.setString(2, request.getDeliveryAddress());
            if (request.getApprovedBy() == null || request.getApprovedBy() == 0)
                ps.setObject(3, null);
            else {
                ps.setInt(3, request.getApprovedBy());
            }
            ps.setInt(4, request.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error("exception occurred during statement execution", ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

    @Override
    public void deleteById(int id) {
        if (id <= 0) return;
        String sql = "delete from request where id=?";
        logger.info("sql: " + sql);
        Connection con = null;
        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.error(String.format("cannot delete row{id=%s} from table dish", id), ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

    private static RequestMapper getMapper() {
        UserMapper userMapper = new UserMapper("customer_id", "username", "password",
                "name", "phone_number", "email", "role_id");
        return new RequestMapper("id", "status_id", "delivery_address",
                "total_price", "approved_by", "created_at",
                "updated_at", userMapper);
    }

    private Page<Request> getRequestPage(int limit, int index, String sql, String countTotal, Integer... ids) {
        logger.info("sql: " + countTotal);

        int offset = limit * (index - 1);
        List<Request> entries = new ArrayList<>();
        int totalValues = 0;

        try (Connection con = dbManager.getConnection();
             PreparedStatement original = con.prepareStatement(sql);
             PreparedStatement count = con.prepareStatement(countTotal)) {

            for (int i = 0; i < ids.length; i++) {
                original.setInt(i + 1, ids[i]);
                count.setInt(i + 1, ids[i]);
            }
            original.setInt(ids.length + 1, limit);
            original.setInt(ids.length + 2, offset);

            try (ResultSet rs = original.executeQuery();
                 ResultSet countRs = count.executeQuery()) {
                while (rs.next())
                    entries.add(getMapper().map(rs));
                if (countRs.next())
                    totalValues = countRs.getInt("count");
            }
        } catch (SQLException ex) {
            logger.error("cannot retrieve objects from table request", ex);
        }

        int totalPages = totalValues / limit;
        if (totalValues % limit != 0)
            totalPages++;
        return new Page<>(index, totalPages, entries);
    }

}
