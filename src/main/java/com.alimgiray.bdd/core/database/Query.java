package com.alimgiray.bdd.core.database;

/**
 * @author aaytar
 * @since 30.05.2018 09:31
 */
public class Query {

    private String queryType;
    private String tableName;
    private String columnName;

    private boolean whereClause;
    private String whereField;
    private String whereValue;

    public Query() {

    }

    public Query select(String columnName) {
        queryType = "SELECT";
        this.columnName = columnName;
        return this;
    }

    public Query from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public Query where(String fieldName) {
        this.whereField = fieldName;
        whereClause = true;
        return this;
    }

    public Query is(String fieldValue) {
        this.whereValue = fieldValue;
        return this;
    }

    public String getResult() {
        return Database.selectQuery(buildQuery(), columnName);
    }

    private String buildQuery() {
        String sql = queryType + " " + columnName + " FROM " + tableName;
        if (whereClause) sql = sql + " WHERE " + whereField + " = " + whereValue;
        return sql;
    }
}
