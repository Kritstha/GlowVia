package com.glowvia.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centralized JDBC connection helper for the Glowvia database.
 * Loads the MySQL driver once and hands out fresh connections on demand.
 */
public final class DbConnection {

    private static final String JDBC_URL =
            "jdbc:mysql://localhost:3306/glowvia"
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("MySQL JDBC driver not on classpath", ex);
        }
    }

    private DbConnection() {
        // Utility class - no instances
    }

    /**
     * Open a new connection to the Glowvia database.
     */
    public static Connection open() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
    }
}
