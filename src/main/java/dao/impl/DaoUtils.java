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
public class DaoUtils<T extends Entity> {

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
     * @return list of objects in table
     */
    public List<T> findAll() {
        StatementBuilder b = new StatementBuilder(tableName);
        String sql = b.setSelect().build();
        return getList(sql);
    }

    /**
     * @param id entity unique identifier
     * @return nullable object with given id
     */
    public Optional<T> findById(int id) {
        if (id <= 0) return Optional.empty();

        StatementBuilder b = new StatementBuilder(tableName);
        String sql = b.setSelect().setWhere("id").build();
        return getOptional(sql, id);
    }

    /**
     * @param id entity unique identifier
     */
    public void deleteById(int id) {
        if (id <= 0) return;

        StatementBuilder b = new StatementBuilder(tableName);
        String statement = b.setDelete().setWhere("id").build();
        logger.debug("sql: " + statement);
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(statement)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            logger.error(String.format("cannot delete row{id=%s} from table '%s'", id, tableName), ex);
        }
    }

    /**
     * @param t object to delete
     */
    public void delete(T t) {
        if (t == null) {
            logger.warn("Cannot delete null object in from table " + tableName);
            return;
        }
        deleteById(t.getId());
    }

    /**
     * retrieves limited number of objects from table and returns them as
     * Page object
     *
     * @param limit  maximum number of objects on page
     * @param index  page index. indexation starts from 1
     * @param sql    sql statement
     * @param values parameters of sql statement
     * @return Page object which contains list of retrieved objects
     */
    public Page<T> getPage(int limit, int index, String sql, Object... values) {
        if (limit < 0 || index < 1)
            throw new IllegalArgumentException();

        int offset = limit * (index - 1);
        Object[] valuesCopy = new Object[values.length + 2];
        System.arraycopy(values, 0, valuesCopy, 0, values.length);
        valuesCopy[values.length] = limit;
        valuesCopy[values.length + 1] = offset;
        List<T> entries = getList(sql, valuesCopy);
        int totalValues = getTotalNumberOfRows(sql, values);
        int totalPages = totalValues / limit + 1;
        return new Page<>(index, totalPages, entries);
    }

    /**
     * @param statement sql statement
     * @param values    parameters of statement
     * @return nullable object from table
     */
    public Optional<T> getOptional(String statement, Object... values) {
        logger.debug("sql: " + statement);
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
     * @return list of objects in table
     */
    public List<T> getList(String statement, Object... values) {
        logger.debug("sql: " + statement);
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

        logger.debug("sql: " + saveStmt);
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(saveStmt, Statement.RETURN_GENERATED_KEYS)) {
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
            logger.error(String.format("cannot insert object into table %s", tableName), ex);
        }
    }

    /**
     * @param updateStmt sql statement
     * @param values     parameters of statement
     */
    public void update(String updateStmt, Object... values) {
        if (values.length == 0) return;

        logger.debug("sql: " + updateStmt);
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(updateStmt)) {
            for (int i = 1; i <= values.length; i++) {
                ps.setObject(i, values[i - 1]);
            }
            ps.executeUpdate();

        } catch (SQLException ex) {
            logger.error(String.format("cannot update object in table %s", tableName), ex);
        }
    }

    /**
     * helper method. it is used by method which returns pages to get total number
     * of rows selected without limit and offset.
     *
     * @param sql    original statement. there is no need to remove 'limit ... offset ...' part of
     *               this statement by hand, because it is done by this method as well
     * @param values parameters of statement (not including limit and offset)
     * @return total number of rows
     */
    private int getTotalNumberOfRows(String sql, Object... values) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("select count(id) as count from %s ", tableName));

        if (sql.contains("where"))
            sb.append(sql, sql.indexOf("where"), sql.indexOf("limit"));

        String count = sb.toString();
        logger.debug("sql: " + count);
        try (Connection con = dbManager.getConnection();
             PreparedStatement ps = con.prepareStatement(count)) {
            for (int i = 1; i <= values.length; i++) {
                ps.setObject(i, values[i - 1]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt("count");
            }
        } catch (SQLException ex) {
            String msg = String.format("cannot retrieve number of objects in table %s", tableName);
            logger.error(msg, ex);
            throw new RuntimeException(msg);
        }

        String msg = String.format("unable to execute statement '%s'", count);
        logger.error(msg);
        throw new RuntimeException(msg);
    }

}
