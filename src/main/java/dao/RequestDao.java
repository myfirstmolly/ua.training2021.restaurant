package dao;

import entities.Request;
import entities.Status;
import entities.User;
import exceptions.DatabaseException;

import java.util.List;

public interface RequestDao extends CrudDao<Request> {

    List<Request> findAllByUser(User user) throws DatabaseException;

    List<Request> findAllByStatus(Status status) throws DatabaseException;

}
