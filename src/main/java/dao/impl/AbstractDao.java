package dao.impl;

import dao.CrudDao;
import database.DBManager;
import entities.Entity;
import util.Page;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public abstract class AbstractDao<T extends Entity> implements CrudDao<T> {

    private final String tableName;
    protected final DBManager dbManager;
    private final Mapper<T> mapper;

    public AbstractDao(DBManager dbManager, String tableName, Mapper<T> mapper) {
        this.dbManager = Objects.requireNonNull(dbManager);
        this.tableName = tableName;
        this.mapper = mapper;
    }

    @Override
    public List<T> findAll() {
        StatementBuilder b = new StatementBuilder(tableName);
        return getList(b.select().build());
    }

    @Override
    public Optional<T> findById(int id) {
        if (id <= 0) return Optional.empty();

        StatementBuilder b = new StatementBuilder(tableName);
        return getOptional(b.select().where("id").build(), id);
    }

    @Override
    public void deleteById(int id) {
        if (id <= 0) return;

        StatementBuilder b = new StatementBuilder(tableName);
        String statement = b.delete().where("id").build();
        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(statement)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(T t) {
        if (t == null) return;
        deleteById(t.getId());
    }

    protected interface Mapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    protected Page<T> getPage(int limit, int index, String sql, String where, Object... values) {
        if (limit < 0 || index < 1)
            throw new IllegalArgumentException();

        int offset = limit * (index - 1);
        Object [] valuesCopy = new Object[values.length + 2];
        System.arraycopy(values, 0, valuesCopy, 0, values.length);
        valuesCopy[values.length] = limit;
        valuesCopy[values.length + 1] = offset;
        List<T> entries = getList(sql, valuesCopy);
        int total = getTotal(where, values);
        return new Page<>(index, total, entries);
    }

    protected Optional<T> getOptional(String statement, Object... values) {
        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(statement)) {
            for (int i = 1; i <= values.length; i++)
                ps.setObject(i, values[i - 1]);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapper.map(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

    protected List<T> getList(String statement, Object... values) {
        List<T> entries = new ArrayList<>();

        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(statement)) {
            for (int i = 1; i <= values.length; i++)
                ps.setObject(i, values[i - 1]);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    entries.add(mapper.map(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return entries;
    }

    protected void save(T t, String saveStmt, Object... values) {
        if (values.length == 0) return;

        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(saveStmt, Statement.RETURN_GENERATED_KEYS)) {
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
            ex.printStackTrace();
        }
    }

    protected void update(int id, String updateStmt, Object... values) {
        if (values.length == 0) return;

        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(updateStmt)) {
            for (int i = 1; i <= values.length; i++) {
                ps.setObject(i, values[i - 1]);
            }
            ps.setInt(values.length + 1, id);
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private int getTotal(String where, Object... values) {
        String count = String.format("select count(id) as count from %s ", tableName) + where;
        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(count)) {
            for (int i = 1; i <= values.length; i++) {
                ps.setObject(i, values[i - 1]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt("count");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

}
