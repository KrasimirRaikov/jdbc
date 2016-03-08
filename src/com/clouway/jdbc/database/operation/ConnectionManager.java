package com.clouway.jdbc.database.operation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ConnectionManager {

    public Connection getConnection(String database, String username, String password) throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost/" + database, username, password);
    }

}
