package model.dao.mapper;

import model.entities.RequestItem;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestItemMapper implements Mapper<RequestItem> {

    private final String id;
    private final String requestId;
    private final String quantity;
    private final String price;
    private final String createdAt;
    private final DishMapper dishMapper;

    public RequestItemMapper(String id, String requestId, String quantity, String price, String createdAt,
                             DishMapper dishMapper) {
        this.id = id;
        this.requestId = requestId;
        this.quantity = quantity;
        this.price = price;
        this.createdAt = createdAt;
        this.dishMapper = dishMapper;
    }

    @Override
    public RequestItem map(ResultSet rs) throws SQLException {
        RequestItem requestItem = new RequestItem();
        requestItem.setId(rs.getInt(id));
        requestItem.setRequestId(rs.getInt(requestId));
        requestItem.setDish(dishMapper.map(rs));
        requestItem.setQuantity(rs.getInt(quantity));
        requestItem.setPrice(rs.getInt(price));
        requestItem.setCreatedAt(rs.getDate(createdAt));
        return requestItem;
    }
}
