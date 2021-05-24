package model.dao;

import model.dao.impl.*;
import model.database.DBManager;

/**
 * factory for dao objects
 */
public class DaoFactory {

    public static CategoryDao getCategoryDao() {
        return new CategoryDaoImpl(DBManager.getInstance());
    }

    public static DishDao getDishDao() {
        return new DishDaoImpl(DBManager.getInstance());
    }

    public static RequestDao getRequestDao() {
        return new RequestDaoImpl(DBManager.getInstance());
    }

    public static RequestItemDao getRequestItemDao() {
        return new RequestItemDaoImpl(DBManager.getInstance());
    }

    public static UserDao getUserDao() {
        return new UserDaoImpl(DBManager.getInstance());
    }

}
