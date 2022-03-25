package com.pixplaze.sync.database.sql;

import com.pixplaze.plugin.PixplazeLoginSync;

import java.sql.*;
import java.util.Properties;

public class ConnectionManager {
    private final static String DB_URL = "jdbc:mysql://localhost:3306/test";
    private final static String DB_USER = "root";
    private final static String DB_PASS = "root";

    private Connection connection;
    private Statement statement;

    private static ConnectionManager instance;

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public void connect() {
        try {
            Properties properties = new Properties();
            properties.setProperty("autoReconnect", "true");
            properties.setProperty("connectTimeout", "1000");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            statement = connection.createStatement();
        } catch (SQLException sqlException) {
            PixplazeLoginSync.getInstance().getLogger().warning("Cannot connect database.");
            sqlException.printStackTrace();
        }
    }

    protected ResultSet executeSelect(String query) {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return resultSet;
    }

    protected int executeUpdate(String query) {
        int rows = 0;
        try {
            rows = statement.executeUpdate(query);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return rows;
    }
}
