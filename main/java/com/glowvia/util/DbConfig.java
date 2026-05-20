package com.glowvia.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// This class handles the database connection
public class DbConfig {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/glowvia"
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // This method opens and returns a connection to the database
    public static Connection getDbConnection() throws SQLException, ClassNotFoundException {
        // Load the MySQL driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        // Return a new connection
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}