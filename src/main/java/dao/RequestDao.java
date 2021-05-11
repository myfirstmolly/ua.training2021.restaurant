package dao;

import entities.Request;

import java.util.List;

public interface RequestDao extends CrudDao<Request> {

    List<Request> findAll(int limit, int offset);

    List<Request> findAllByUserId(int id, int limit, int offset);

    List<Request> findAllByStatusId(int id, int limit, int offset);

    List<Request> findAllByUserAndStatus(int userId, int statusId);

}
