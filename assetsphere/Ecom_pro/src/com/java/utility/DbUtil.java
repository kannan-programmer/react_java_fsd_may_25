package com.java.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    private static DbUtil instance;
    private static Connection connection;

    private final String url = "jdbc:mysql://localhost:3306/Ecom_db";
    private final String user = "root";
    private final String password = "ngk123";
    private final String driver = "com.mysql.cj.jdbc.Driver";
    private DbUtil() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        connection = DriverManager.getConnection(url, user, password);
    }
    public static DbUtil getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null || connection.isClosed()) {
            instance = new DbUtil();
        }
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            System.out.println("Error closing DB connection: " + e.getMessage());
        }
    }
}
