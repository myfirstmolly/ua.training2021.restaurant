package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * used for mapping model.database result set rows to model.entities. implementations are
 * not supposed to move cursor of ResultSet via next() method, only extract
 * information from row with current cursor position
 *
 * @param <T>
 */
@FunctionalInterface
public interface Mapper<T> {
    T map(ResultSet rs) throws SQLException;
}
