package dao.impl;

import dao.CrudDao;
import database.DBManager;
import entities.Entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        return findAllByParameter();
    }

    protected List<T> findAllByParameter(Param... parameters) {
        List<T> entries = new ArrayList<>();
        String statement = createSelectStatement(parameters);

        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(statement)) {
            for (int i = 1; i <= parameters.length; i++)
                ps.setObject(i, parameters[i - 1].value);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    entries.add(mapper.map(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return entries;
    }

    @Override
    public Optional<T> findById(int param) {
        if (param <= 0) return Optional.empty();

        return findOneByParameter(new Param(param, "id"));
    }

    protected Optional<T> findOneByParameter(Param... parameters) {
        String statement = createSelectStatement(parameters);
        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(statement)) {
            for (int i = 1; i <= parameters.length; i++)
                ps.setObject(i, parameters[i-1].value);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapper.map(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    protected void save(T t, Param... parameters) {
        if (parameters.length == 0) return;

        String saveStmt = createInsertStatement(parameters);
        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(saveStmt, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 1; i <= parameters.length; i++) {
                ps.setObject(i, parameters[i-1].value);
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

    protected void update(int id, Param... parameters) {
        if (parameters.length == 0) return;
        String updateStmt = createUpdateStatement(parameters);

        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(updateStmt)) {
            for (int i = 1; i <= parameters.length; i++) {
                ps.setObject(i, parameters[i-1].value);
            }
            ps.setInt(parameters.length + 1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        if (id <= 0) return;

        String deleteStmt = "delete from %s where id=?";
        String statement = String.format(deleteStmt, tableName);
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

    protected static class Param {
        Object value;
        String name;

        public Param(Object value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    protected interface Mapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    private String createInsertStatement(Param... params) {
        StringBuilder valuesNames = new StringBuilder();
        StringBuilder questionMarks = new StringBuilder();
        valuesNames.append(params[0].name);
        questionMarks.append("?");

        for (int i = 1; i < params.length; i++) {
            valuesNames.append(", ").append(params[i].name);
            questionMarks.append(", ").append("?");
        }

        String saveStmt = "insert into %s (%s) values (%s)";
        return String.format(saveStmt, tableName, valuesNames.toString(), questionMarks);
    }

    private String createSelectStatement(Param... params) {
        String selectStmt = "select * from %s";
        String sql = String.format(selectStmt, tableName);

        if (params.length == 0)
            return sql;

        StringBuilder sb = new StringBuilder();
        sb.append(sql).append(" where ").append(params[0].name).append("=?");

        if (params.length == 1) return sb.toString();

        for (int i = 1; i < params.length; i++)
            sb.append(" and ").append(params[i].name).append("=?");

        return sb.toString();
    }

    private String createUpdateStatement(Param... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(params[0].name).append("=?");

        for (int i = 1; i < params.length; i++)
            sb.append(", ").append(params[i].name).append("=?");

        String updateStmt = "update %s set %s where id=?";
        return String.format(updateStmt, tableName, sb.toString());
    }

}
