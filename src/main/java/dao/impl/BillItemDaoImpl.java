package dao.impl;

import dao.BillItemDao;
import dao.DishDao;
import database.DBManager;
import entities.Bill;
import exceptions.DataIntegrityViolationException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public final class BillItemDaoImpl extends AbstractDao<Bill> implements BillItemDao {

    public BillItemDaoImpl(DBManager dbManager) {
        super(dbManager, "bill", new Mapper(dbManager));
        super.saveStmt = "insert into bill (request_id, dish_id, quantity) values (?, ?, ?)";
        super.updateStmt = "update bill set quantity=? where id=?";
    }

    @Override
    public List<Bill> findAllByRequestId(int id) {
        if (id <= 0) return List.of();
        return super.findAllByParameter(new Param(id, "request_id"));
    }

    private static class Mapper implements AbstractDao.Mapper<Bill> {
        private final DBManager dbManager;

        public Mapper(DBManager dbManager) {
            this.dbManager = dbManager;
        }

        @Override
        public void setSaveStatementParams(Bill bill, PreparedStatement ps) throws SQLException {
            if (bill.getRequestId() <= 0 || bill.getDish() == null || bill.getQuantity() <= 0)
                throw new DataIntegrityViolationException();

            ps.setInt(1, bill.getRequestId());
            ps.setInt(2, bill.getDish().getId());
            ps.setInt(3, bill.getQuantity());
        }

        @Override
        public void setUpdateStatementParams(Bill bill, PreparedStatement ps) throws SQLException {
            if (bill.getQuantity() <= 0)
                throw new DataIntegrityViolationException();

            ps.setInt(1, bill.getQuantity());
            ps.setInt(2, bill.getId());
        }

        @Override
        public Bill map(ResultSet rs) throws SQLException {
            Bill bill = new Bill();
            bill.setId(rs.getInt("id"));
            bill.setRequestId(rs.getInt("request_id"));
            DishDao dishDao = new DishDaoImpl(dbManager);
            bill.setDish(dishDao.findById(rs.getInt("dish_id")).get());
            bill.setQuantity(rs.getInt("quantity"));
            bill.setPrice(rs.getInt("price"));
            bill.setCreatedAt(rs.getDate("created_at"));
            return bill;
        }
    }

}
