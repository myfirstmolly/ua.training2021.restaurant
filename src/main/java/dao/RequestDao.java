package dao;

import entities.Request;
import entities.Status;
import entities.User;
import exceptions.DaoException;

import java.util.List;

public interface RequestDao extends CrudDao<Request> {

    List<Request> findAllByUser(User user) throws DaoException;

    List<Request> findAllByStatus(Status status) throws DaoException;

}
