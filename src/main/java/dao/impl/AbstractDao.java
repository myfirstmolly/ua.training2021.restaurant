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
    private final String selectStmt = "select * from %s";
    private final String deleteStmt = "delete from %s where id=?";
    protected String saveStmt;
    protected String updateStmt;
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
        String statement = createStatement(String.format(selectStmt, tableName), parameters);

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
        if (parameters.length == 0)
            return Optional.empty();

        String statement = createStatement(String.format(selectStmt, tableName), parameters);
        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(statement)) {
            for (int i = 1; i <= parameters.length; i++)
                ps.setObject(i, parameters[i - 1].value);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapper.map(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void save(T t) {
        if (t == null) return;

        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(saveStmt, Statement.RETURN_GENERATED_KEYS)) {
            mapper.setSaveStatementParams(t, ps);
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

    @Override
    public void update(T t) {
        if (t == null) return;

        try (PreparedStatement ps = dbManager.getConnection()
                .prepareStatement(updateStmt)) {
            mapper.setUpdateStatementParams(t, ps);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        if (id <= 0) return;

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

    private String createStatement(String sql, Param... params) {
        if (params.length == 0)
            return sql;

        StringBuilder sb = new StringBuilder();
        sb.append(sql).append(" where ").append(params[0].name).append("=?");

        if (params.length == 1) return sb.toString();

        for (Param p : params)
            sb.append(" and ").append(p.name).append("=?");

        return sb.toString();
    }

    protected interface Mapper<T> {
        void setSaveStatementParams(T t, PreparedStatement ps) throws SQLException;

        void setUpdateStatementParams(T t, PreparedStatement ps) throws SQLException;

        T map(ResultSet rs) throws SQLException;
    }

}
