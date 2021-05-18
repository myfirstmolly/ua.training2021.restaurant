package dao;

import entities.Request;
import entities.Status;
import util.Page;

import java.util.List;

public interface RequestDao extends CrudDao<Request> {

    Page<Request> findAll(int limit, int index);

    Page<Request> findAllByUserId(int id, int limit, int index);

    Page<Request> findAllWhereStatusNotEqual(Status status, int limit, int index);

    Page<Request> findAllByStatusId(int id, int limit, int index);

    List<Request> findAllByUserAndStatus(int userId, int statusId);

}
