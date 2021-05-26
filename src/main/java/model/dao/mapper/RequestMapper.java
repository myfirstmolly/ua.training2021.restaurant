package model.dao.mapper;

import model.entities.Request;
import model.entities.Status;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestMapper implements Mapper<Request> {

    private final String id;
    private final String statusId;
    private final String deliveryAddress;
    private final String totalPrice;
    private final String approvedBy;
    private final String createdAt;
    private final String updatedAt;
    private final UserMapper userMapper;

    public RequestMapper(String id, String statusId, String deliveryAddress, String totalPrice,
                         String approvedBy, String createdAt, String updatedAt,
                         UserMapper userMapper) {
        this.id = id;
        this.statusId = statusId;
        this.deliveryAddress = deliveryAddress;
        this.totalPrice = totalPrice;
        this.approvedBy = approvedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userMapper = userMapper;
    }

    @Override
    public Request map(ResultSet rs) throws SQLException {
        Request request = new Request();
        request.setId(rs.getInt(id));
        request.setStatus(Status.values()[rs.getInt(statusId) - 1]);
        request.setDeliveryAddress(rs.getString(deliveryAddress));
        request.setTotalPrice(rs.getLong(totalPrice));
        request.setApprovedBy(rs.getInt(approvedBy));
        request.setCustomer(userMapper.map(rs));
        request.setCreatedAt(rs.getDate(createdAt));
        request.setUpdatedAt(rs.getDate(updatedAt));
        return request;
    }
}
