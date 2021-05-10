package dao;

import entities.Request;

import java.util.List;

public interface RequestDao extends CrudDao<Request> {

    List<Request> findAllByUserId(int id);

    List<Request> findAllByStatusId(int id);

    List<Request> findAllByUserAndStatus(int userId, int statusId);

}
