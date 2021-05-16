package dao.impl;

/**
 * SQL statements builder class
 */
public class StatementBuilder {

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
     * @throws IllegalStateException if insert statement is being inserted not in
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
     * adds statement 'delete from tableName' to existing one
     *
     * @return StatementBuilder object which contains statement
     * 'delete from tableName'
     * @throws IllegalStateException if insert statement is being inserted not in
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
     * adds statement 'order by column_1, ..., column_n' to existing one
     *
     * @param columnNames - names of columns by which results should be ordered
     * @return StatementBuilder object
     */
    public StatementBuilder setOrderBy(String... columnNames) {
        sb.append(String.format("order by %s ", buildString(columnNames)));
        return this;
    }

    /**
     * adds statement 'limit=?' to existing one
     *
     * @return StatementBuilder object
     */
    public StatementBuilder setLimit() {
        sb.append("limit ? ");
        return this;
    }

    /**
     * adds statement 'offset=?' to existing one
     *
     * @return StatementBuilder object
     */
    public StatementBuilder setOffset() {
        sb.append("offset ? ");
        return this;
    }

    /**
     * adds statement 'where column_1=? and ... and column_n=?' to existing one
     *
     * @param columnNames - names of columns by which results are filtered
     * @return StatementBuilder object
     */
    public StatementBuilder setWhere(String... columnNames) {
        sb.append(String.format("where %s ", buildString(" and ", columnNames)));
        return this;
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
