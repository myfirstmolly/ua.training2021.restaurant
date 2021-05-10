package dao;

import entities.RequestItem;

import java.util.List;

public interface RequestItemDao extends CrudDao<RequestItem> {

    List<RequestItem> findAllByRequestId(int id);

}
