package dao;

import entities.RequestItem;

import java.util.List;

/**
 * data access object interface for request_item table
 */
public interface RequestItemDao extends CrudDao<RequestItem> {

    /**
     * finds all request items with given request id
     *
     * @param id request unique identifier
     * @return list of RequestItems with given request id
     */
    List<RequestItem> findAllByRequestId(int id);

}
