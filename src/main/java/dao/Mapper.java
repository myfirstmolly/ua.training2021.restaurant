package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * used for mapping database result set rows to entities. implementations are
 * not supposed to move cursor of ResultSet via next() method, only extract
 * information from row with current cursor position
 *
 * @param <T>
 */
@FunctionalInterface
public interface Mapper<T> {
    T map(ResultSet rs) throws SQLException;
}
