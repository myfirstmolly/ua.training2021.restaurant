package dao.impl;

public class StatementBuilder {
    
    private final String tableName;
    private StringBuilder sb;

    public StatementBuilder(String tableName) {
        this.tableName = tableName;
        sb = new StringBuilder();
    }
    
    public StatementBuilder select() {
        sb.append(String.format("select * from %s ", tableName));
        return this;
    }

    public StatementBuilder insert(String... columnNames) {
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

    public StatementBuilder update(String... columnNames) {
        sb.append(String.format("update %s set %s ", tableName,
                buildStringHelperWithEquals(", ", columnNames)));
        return this;
    }

    public StatementBuilder delete() {
        sb.append(String.format("delete from %s ", tableName));
        return this;
    }

    public StatementBuilder orderBy(String... columnNames) {
        sb.append(String.format("order by %s ", buildStringHelper(columnNames)));
        return this;
    }

    public StatementBuilder limit() {
        sb.append("limit ? ");
        return this;
    }

    public StatementBuilder offset() {
        sb.append("offset ? ");
        return this;
    }
    
    public StatementBuilder where(String... columnNames) {
        sb.append(String.format("where %s ", buildStringHelperWithEquals(" and ", columnNames)));
        return this;
    }

    public StatementBuilder clear() {
        sb = new StringBuilder(tableName);
        return this;
    }

    public String build() {
        return sb.toString();
    }

    private String buildStringHelper(String... columnNames) {
        if (columnNames.length == 0)
            return "";

        StringBuilder builder = new StringBuilder();
        builder.append(columnNames[0]);

        for (int i = 1; i < columnNames.length; i++)
            builder.append(", ").append(columnNames[i]);

        return builder.toString();
    }

    private String buildStringHelperWithEquals(String sep, String... columnNames) {
        if (columnNames.length == 0)
            return "";

        StringBuilder builder = new StringBuilder();
        builder.append(columnNames[0]).append("=?");

        for (int i = 1; i < columnNames.length; i++)
            builder.append(sep).append(columnNames[i]).append("=?");

        return builder.toString();
    }
}
