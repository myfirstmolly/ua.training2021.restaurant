package dao.impl;

import dao.RequestDao;
import dao.UserDao;
import database.DBManager;
import entities.Request;
import entities.Status;
import exceptions.DataIntegrityViolationException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

public final class RequestDaoImpl extends AbstractDao<Request> implements RequestDao {

    public RequestDaoImpl(DBManager dbManager) {
        super(dbManager, "request", new Mapper(dbManager));
        super.saveStmt = "insert into request (customer_id, status_id, delivery_address)" +
                "values (?,?,?)";
        super.updateStmt = "update request set customer_id=?, status_id=?, delivery_address=?," +
                "approved_by=? where id=?";
    }

    @Override
    public List<Request> findAllByUserId(int id) {
        return super.findAllByParameter(new Param(id, "customer_id"));
    }

    @Override
    public List<Request> findAllByStatusId(int id) {
        return super.findAllByParameter(new Param(id, "status_id"));
    }

    @Override
    public Optional<Request> findByUserAndStatus(int userId, int statusId) {
        return super.findOneByParameter(
                new Param(userId, "customer_id"),
                new Param(statusId, "status_id")
        );
    }

    private static class Mapper implements AbstractDao.Mapper<Request> {

        private final DBManager dbManager;

        public Mapper(DBManager dbManager) {
            this.dbManager = dbManager;
        }

        @Override
        public void setSaveStatementParams(Request request, PreparedStatement ps) throws SQLException {
            if (request.getCustomer() == null || request.getCustomer().getId() <= 0 ||
                    request.getStatus() == null || request.getDeliveryAddress() == null)
                throw new DataIntegrityViolationException();

            ps.setInt(1, request.getCustomer().getId());
            ps.setInt(2, request.getStatus().toInt());
            ps.setString(3, request.getDeliveryAddress());
        }

        @Override
        public void setUpdateStatementParams(Request request, PreparedStatement ps) throws SQLException {
            setSaveStatementParams(request, ps);
            if (request.getApprovedBy() != null)
                ps.setInt(4, request.getApprovedBy().getId());
            else ps.setNull(4, Types.INTEGER);
            ps.setInt(5, request.getId());
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
            return request;
        }
    }
}
