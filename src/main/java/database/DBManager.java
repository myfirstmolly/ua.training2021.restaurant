package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBManager {

    private static DBManager dbManager;
    private static final Logger logger = LogManager.getLogger(DBManager.class);

    private DBManager() {
    }

    public static synchronized DBManager getInstance() {
        if (dbManager == null)
            dbManager = new DBManager();
        return dbManager;
    }

    public Connection getConnection() throws SQLException {
        try {
            Context initialContext = new InitialContext();
            Context context = (Context) initialContext.lookup("java:comp/env");
            DataSource ds = (DataSource) context.lookup("jdbc/restaurant");
            return ds.getConnection();
        } catch (NamingException ex) {
            logger.error("unable to get connection from the pool");
            throw new RuntimeException(ex);
        }
    }

}
