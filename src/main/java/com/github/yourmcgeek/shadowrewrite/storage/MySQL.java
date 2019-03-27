package com.github.yourmcgeek.shadowrewrite.storage;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL extends Database {

    private Connection conn;
    private String hostname;
    private int port;
    private String database;
    private String username;
    private String password;
    private ShadowRewrite main;

    public MySQL(String hostname, int port, String database, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection openConnection() throws SQLException, ClassNotFoundException {
        if (checkConnection()) {
            return this.conn;
        }
        try {
            String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database  + "?user=" + username + "&password=" + password;
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.conn;
    }

    @Override
    public boolean checkConnection() throws SQLException {
        return (this.conn != null) && !this.conn.isClosed();
    }

    @Override
    public Connection getConnection() {
        return this.conn;
    }

    @Override
    public boolean closeConnection() throws SQLException {
        if (this.conn == null) {
            return false;
        }
        this.conn.close();
        this.conn = null;
        return true;
    }

    @Override
    public int updateSQL(String query) throws SQLException, ClassNotFoundException {
        if (checkConnection()) {
            openConnection();
        }
        try (Statement stmt = this.conn.createStatement()) {
            return stmt.executeUpdate(query);
        }
    }
}
