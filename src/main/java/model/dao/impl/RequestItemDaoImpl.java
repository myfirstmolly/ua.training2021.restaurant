package model.dao.impl;

import model.dao.RequestItemDao;
import model.database.DBManager;
import model.database.DaoFactory;
import model.entities.Category;
import model.entities.Dish;
import model.entities.RequestItem;
import model.exceptions.ObjectNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public final class RequestItemDaoImpl implements RequestItemDao {

    private static final String TABLE_NAME = "request_item";
    private static final Logger logger = LogManager.getLogger(RequestItemDaoImpl.class);
    public static final String JOIN_STATEMENT = "select ri.*, d.name, d.name_ukr, d.image_path " +
            "from request_item ri inner join dish d on ri.dish_id=d.id ";
    private final DaoUtils<RequestItem> daoUtils;

    public RequestItemDaoImpl(DBManager dbManager) {
        daoUtils = new DaoUtils<>(dbManager, TABLE_NAME, rs -> {
            RequestItem requestItem = new RequestItem();
            requestItem.setId(rs.getInt("id"));
            requestItem.setRequestId(rs.getInt("request_id"));
            Dish dish = new Dish();
            dish.setId(rs.getInt("dish_id"));
            dish.setImagePath(rs.getString("image_path"));
            dish.setName(rs.getString("name"));
            dish.setNameUkr(rs.getString("name_ukr"));
            requestItem.setDish(dish);
            requestItem.setQuantity(rs.getInt("quantity"));
            requestItem.setPrice(rs.getInt("price"));
            requestItem.setCreatedAt(rs.getDate("created_at"));
            return requestItem;
        });
    }

    @Override
    public List<RequestItem> findAllByRequestId(int id) {
        String sql = JOIN_STATEMENT + "where ri.request_id=?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getList(sql, id);
    }

    @Override
    public List<RequestItem> findAll() {
        String sql = JOIN_STATEMENT;
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getList(sql);
    }

    @Override
    public Optional<RequestItem> findById(int id) {
        String sql = JOIN_STATEMENT + "where ri.id=?";
        logger.trace("delegated '" + sql + "' to DaoUtils");
        return daoUtils.getOptional(sql, id);
    }

    @Override
    public void save(RequestItem requestItem) {
        if (requestItem == null) {
            logger.warn("cannot save null object");
            return;
        }
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setInsert("request_id", "dish_id", "quantity").build();
        Integer dishId = requestItem.getDish() != null ? requestItem.getDish().getId() : null;
        logger.trace("delegated '" + sql + "' to DaoUtils");
        daoUtils.save(requestItem, sql, requestItem.getRequestId(), dishId, requestItem.getQuantity());
    }

    @Override
    public void update(RequestItem requestItem) {
        if (requestItem == null) {
            logger.warn("cannot update null object");
            return;
        }
        StatementBuilder s = new StatementBuilder(TABLE_NAME);
        String sql = s.setUpdate("quantity").setWhere("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        daoUtils.update(sql, requestItem.getQuantity(), requestItem.getId());
    }

    @Override
    public void deleteById(int id) {
        StatementBuilder b = new StatementBuilder(TABLE_NAME);
        String sql = b.setDelete().setWhere("id").build();
        logger.trace("delegated '" + sql + "' to DaoUtils");
        daoUtils.deleteById(id, sql);
    }

    @Override
    public void delete(RequestItem requestItem) {
        if (requestItem == null) {
            logger.warn("Cannot delete null object in from table " + TABLE_NAME);
            return;
        }
        deleteById(requestItem.getId());
    }
}
