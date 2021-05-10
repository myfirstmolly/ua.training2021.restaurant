package dao;

import entities.Bill;

import java.util.List;

public interface BillItemDao extends CrudDao<Bill> {

    List<Bill> findAllByRequestId(int id);

}
