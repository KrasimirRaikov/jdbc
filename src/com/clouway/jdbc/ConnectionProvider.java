package com.clouway.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ConnectionProvider {
    public static Connection getConnection(String database, String username, String password) {
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost/" + database, username, password);
        } catch (SQLException e) {
            throw new ExecutionException("can't connect");
        }
    }
}