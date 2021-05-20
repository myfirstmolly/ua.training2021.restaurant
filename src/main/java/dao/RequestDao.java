package dao;

import entities.Request;
import entities.Status;
import util.Page;

import java.util.List;
import java.util.Optional;

public interface RequestDao extends CrudDao<Request> {

    Page<Request> findAll(int limit, int index);

    Page<Request> findAllByUserId(int id, int limit, int index);

    Page<Request> findAllWhereStatusNotEqual(Status status, int limit, int index);

    Page<Request> findAllByStatusId(int id, int limit, int index);

    Page<Request> findAllByUserAndStatus(int userId, int statusId, int limit, int index);

    Optional<Request> findFirstByUserAndStatus(int userId, int statusId);

}
