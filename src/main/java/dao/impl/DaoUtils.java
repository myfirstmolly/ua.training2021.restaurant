package dao.impl;

import dao.Mapper;
import database.DBManager;
import entities.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Page;

import java.sql.*;
import java.util.*;

/**
 * generic utility class to manipulate with data in database
 *
 * @param <T>
 */
public final class DaoUtils<T extends Entity> {

    private static final Logger logger = LogManager.getLogger(DaoUtils.class);
    private final String tableName;
    private final DBManager dbManager;
    private final Mapper<T> mapper;

    public DaoUtils(DBManager dbManager, String tableName, Mapper<T> mapper) {
        this.dbManager = Objects.requireNonNull(dbManager);
        this.tableName = tableName;
        this.mapper = mapper;
    }

    /**
     * finds limited number of objects by executing sql statement passed as method parameter.
     * returns them as Page object
     *
     * @param limit  maximum number of objects on page
     * @param index  page index. indexation starts from 1
     * @param sql    sql statement
     * @param values parameters of sql statement
     * @return Page object which contains list of retrieved objects
     * @throws IllegalArgumentException if limit is less than 0 or index is less than 1
     */
    public Page<T> getPage(int limit, int index, String sql, Object... values) {
        if (limit < 0 || index < 1)
            throw new IllegalArgumentException();

        logger.info("sql: " + sql);
        String countTotal = StatementBuilder.getCountStatement(sql, tableName);
        logger.info("sql: " + countTotal);

        int offset = limit * (index - 1);
        List<T> entries = new ArrayList<>();
        int totalValues = 0;

        try (Connection con = dbManager.getConnection();
             PreparedStatement original = con.prepareStatement(sql);
             PreparedStatement count = con.prepareStatement(countTotal)) {
            // set values for original and count statements
            for (int i = 1; i <= values.length; i++) {
                original.setObject(i, values[i - 1]); //
                count.setObject(i, values[i - 1]);
            }
            // set limit and offset parameters for original statement
            original.setObject(values.length + 1, limit);
            original.setObject(values.length + 2, offset);

            try (ResultSet rs = original.executeQuery();
                 ResultSet countRs = count.executeQuery()) {
                while (rs.next())
                    entries.add(mapper.map(rs));
                if (countRs.next())
                    totalValues = countRs.getInt("count");
            }
        } catch (SQLException ex) {
            logger.error(String.format("cannot retrieve objects from table %s", tableName), ex);
        }

        int totalPages = totalValues / limit;
        if (totalValues % limit != 0)
            totalPages++;
        return new Page<>(index, totalPages, entries);
    }

    /**
     * @param statement sql statement
     * @param values    parameters of statement
     * @return Optional object from table
     */
    public Optional<T> getOptional(String statement, Object... values) {
        logger.info("sql: " + statement);
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            for (int i = 1; i <= values.length; i++)
                ps.setObject(i, values[i - 1]);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapper.map(rs));
            }
        } catch (SQLException ex) {
            logger.error(String.format("cannot get object from table %s", tableName), ex);
        }

        return Optional.empty();
    }

    /**
     * @param statement sql statement
     * @param values    parameters of sql statement
     * @return List of objects
     */
    public List<T> getList(String statement, Object... values) {
        logger.info("sql: " + statement);
        List<T> entries = new ArrayList<>();

        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            for (int i = 1; i <= values.length; i++)
                ps.setObject(i, values[i - 1]);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    entries.add(mapper.map(rs));
            }
        } catch (SQLException ex) {
            logger.error(String.format("cannot obtain objects from table %s", tableName), ex);
        }

        return entries;
    }

    /**
     * saves object to database and updates its id
     *
     * @param t        object to save
     * @param saveStmt sql statement
     * @param values   parameters of statement
     */
    public void save(T t, String saveStmt, Object... values) {
        if (values.length == 0) return;

        logger.info("sql: " + saveStmt);
        Connection con = null;
        try {
            con = dbManager.getConnection();
            PreparedStatement ps = con.prepareStatement(saveStmt, Statement.RETURN_GENERATED_KEYS);
            con.setAutoCommit(false);
            for (int i = 1; i <= values.length; i++) {
                ps.setObject(i, values[i - 1]);
            }
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    t.setId(keys.getInt(1));
                }
            }
        } catch (SQLException ex) {
            logger.error("exception occurred during statement execution", ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

    /**
     * @param updateStmt sql statement
     * @param values     parameters of statement
     */
    public void update(String updateStmt, Object... values) {
        if (values.length == 0) return;

        logger.info("sql: " + updateStmt);
        Connection con = null;
        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(updateStmt);
            for (int i = 1; i <= values.length; i++) {
                ps.setObject(i, values[i - 1]);
            }
            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error("exception occurred during statement execution", ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

    /**
     * @param id entity unique identifier
     */
    public void deleteById(int id, String statement) {
        if (id <= 0) return;

        logger.info("sql: " + statement);
        Connection con = null;

        try {
            con = dbManager.getConnection();
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(statement);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.error(String.format("cannot delete row{id=%s} from table '%s'", id, tableName), ex);
            dbManager.rollbackTransaction(con);
        } finally {
            dbManager.commitAndClose(con);
        }
    }

}
