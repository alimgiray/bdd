package com.alimgiray.bdd.core.database;

public enum JdbcDrivers {

    ORACLE("oracle.jdbc.driver.OracleDriver");

    private String className;

    JdbcDrivers(String className) {
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }
}
