package com.pixplaze.sync.database.sql;

import com.pixplaze.plugin.PixplazeLoginSync;

import java.sql.*;

public class ConnectionManager {
    private final PixplazeLoginSync plugin = PixplazeLoginSync.getInstance();

    private String url;
    private String address;
    private String port;
    private String name;
    private String user;
    private String password;

    private Connection connection;

    private static ConnectionManager instance;

    public ConnectionManager() {
        address = plugin.getConfig().getString("mysql.address");
        port = plugin.getConfig().getString("mysql.port");
        name = plugin.getConfig().getString("mysql.name");
        user = plugin.getConfig().getString("mysql.user");
        password = plugin.getConfig().getString("mysql.password");
        url = "jdbc:mysql://" + address + ":" + port + "/" + name + "?autoReconnect=true&useUnicode=yes";
    }

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException sqlException) {
            PixplazeLoginSync.getInstance().getLogger().warning("Cannot connect database.");
            sqlException.printStackTrace();
        }
    }

    protected ResultSet executeSelect(String query) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return resultSet;
    }

    protected int executeUpdate(String query) {
        int rows = 0;
        try {
            Statement statement = connection.createStatement();
            rows = statement.executeUpdate(query);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return rows;
    }
}
