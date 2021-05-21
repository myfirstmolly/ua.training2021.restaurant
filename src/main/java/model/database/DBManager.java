package model.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * DB Manager, works with MySQl.
 * Implements Singleton pattern.
 */
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

    /**
     * Returns DB connection from Connection Pool. To configure
     * the Date Source and Connections Pool, update
     * WEB_APP_ROOT/META-INF/context.xml file.
     *
     * @return DB connection
     */
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

    /**
     * commits transaction and closes the connection.
     * NOTE: this method sets autocommit property to true.
     *
     * @param con Connection to commit and close
     */
    public void commitAndClose(Connection con) {
        try {
            if (con != null) {
                con.commit();
                con.setAutoCommit(true);
                con.close();
            }
        } catch (SQLException ex) {
            logger.error("unable to commit transaction and close connection, cause: ", ex);
        }
    }

    /**
     * rolls back the transaction
     *
     * @param con Connection to rollback
     */
    public void rollbackTransaction(Connection con) {
        try {
            logger.error("trying to rollback transaction...");
            if (con != null)
                con.rollback();
        } catch (SQLException e) {
            logger.error("unable to rollback transaction, cause: ", e);
        }
    }

}
