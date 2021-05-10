package dao;

import entities.Request;

import java.util.List;
import java.util.Optional;

public interface RequestDao extends CrudDao<Request> {

    List<Request> findAllByUserId(int id);

    List<Request> findAllByStatusId(int id);

    Optional<Request> findByUserAndStatus(int userId, int statusId);

}
