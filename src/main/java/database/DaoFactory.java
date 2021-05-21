package database;

import dao.*;
import dao.impl.*;

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
