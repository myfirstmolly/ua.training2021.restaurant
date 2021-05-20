package dao.impl;

/**
 * SQL statements builder class
 */
public final class StatementBuilder {

    private final String tableName;
    private final StringBuilder sb;

    public StatementBuilder(String tableName) {
        this.tableName = tableName;
        sb = new StringBuilder();
    }

    /**
     * @return StatementBuilder object which contains select statement
     * 'select * from tableName'
     * @throws IllegalStateException if insert statement is being inserted not in
     *                               the beginning of sql statement
     */
    public StatementBuilder setSelect() {
        if (sb.length() != 0)
            throw new IllegalStateException("select statement should be in the beginning " +
                    "of sql statement");

        sb.append(String.format("select * from %s ", tableName));
        return this;
    }


    /**
     * @param columnNames - names of columns to which values are inserted
     * @return StatementBuilder object which contains statement
     * 'insert into tableName (column_1, ..., column_n) values (?, ..., ?)'
     * @throws IllegalStateException if insert statement is being inserted not in
     *                               the beginning of sql statement
     */
    public StatementBuilder setInsert(String... columnNames) {
        if (sb.length() != 0)
            throw new IllegalStateException("insert statement should be in the beginning " +
                    "of sql statement");

        if (columnNames.length == 0)
            return this;

        StringBuilder valuesNames = new StringBuilder();
        StringBuilder questionMarks = new StringBuilder();
        valuesNames.append(columnNames[0]);
        questionMarks.append("?");

        for (int i = 1; i < columnNames.length; i++) {
            valuesNames.append(", ").append(columnNames[i]);
            questionMarks.append(", ").append("?");
        }

        sb.append(String.format("insert into %s (%s) values(%s) ", tableName,
                valuesNames.toString(), questionMarks.toString()));
        return this;
    }

    /**
     * @param columnNames - names of columns to which values are inserted
     * @return StatementBuilder object which contains statement
     * 'update tableName set column_1=?, ..., column_n=?'
     * @throws IllegalStateException if update statement is being inserted not in
     *                               the beginning of sql statement
     */
    public StatementBuilder setUpdate(String... columnNames) {
        if (sb.length() != 0)
            throw new IllegalStateException("update statement should be in the beginning " +
                    "of sql statement");

        sb.append(String.format("update %s set %s ", tableName,
                buildString(", ", columnNames)));
        return this;
    }

    /**
     * @return StatementBuilder object which contains statement
     * 'delete from tableName'
     * @throws IllegalStateException if delete statement is being inserted not in
     *                               the beginning of sql statement
     */
    public StatementBuilder setDelete() {
        if (sb.length() != 0)
            throw new IllegalStateException("delete statement should be in the beginning " +
                    "of sql statement");

        sb.append(String.format("delete from %s ", tableName));
        return this;
    }


    /**
     * adds statement 'order by column_1, ..., column_n' to the existing statement
     *
     * @param columnNames - names of columns by which results should be ordered
     * @return StatementBuilder object
     */
    public StatementBuilder setOrderBy(String... columnNames) {
        sb.append(String.format("order by %s ", buildString(columnNames)));
        return this;
    }

    /**
     * adds statement 'limit=?' to the existing statement
     *
     * @return StatementBuilder object
     */
    public StatementBuilder setLimit() {
        sb.append("limit ? ");
        return this;
    }

    /**
     * adds statement 'offset=?' to the existing statement
     *
     * @return StatementBuilder object
     */
    public StatementBuilder setOffset() {
        sb.append("offset ? ");
        return this;
    }

    /**
     * adds statement 'where column_1=? and ... and column_n=?' to the existing statement
     *
     * @param columnNames - names of columns by which results are filtered
     * @return StatementBuilder object
     */
    public StatementBuilder setWhere(String... columnNames) {
        sb.append(String.format("where %s ", buildString(" and ", columnNames)));
        return this;
    }

    /**
     * returns SQL statement to count total number of rows in the table.
     * NOTE: if SQL statement has limit and offset parameters, they will be deleted
     * by this method
     *
     * @param sql       original SQL statement. if this statement has 'where' parameters,
     *                  they will not be deleted.
     * @param tableName name of table
     * @return SQL count statement
     */
    public static String getCountStatement(String sql, String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("select count(id) as count from %s ", tableName));

        if (sql.contains("where"))
            sb.append(sql, sql.indexOf("where"), sql.indexOf("limit"));

        return sb.toString();
    }

    /**
     * @return SQL statement as string
     */
    public String build() {
        return sb.toString();
    }

    private String buildString(String... columnNames) {
        if (columnNames.length == 0)
            return "";

        StringBuilder builder = new StringBuilder();
        builder.append(columnNames[0]);

        for (int i = 1; i < columnNames.length; i++)
            builder.append(", ").append(columnNames[i]);

        return builder.toString();
    }

    private String buildString(String sep, String... columnNames) {
        if (columnNames.length == 0)
            return "";

        StringBuilder builder = new StringBuilder();
        builder.append(columnNames[0]).append("=?");

        for (int i = 1; i < columnNames.length; i++)
            builder.append(sep).append(columnNames[i]).append("=?");

        return builder.toString();
    }
}
