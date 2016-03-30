package com.clouway.jdbc.customer.history.persistence;

import com.clouway.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class MillionRecords {
    public static void main(String[] args) throws SQLException {
        ConnectionManager connectionManager = new ConnectionManager();
        Connection connection = connectionManager.getConnection("customer", "postgres", "clouway.com");
        String statement = "INSERT INTO customer values(?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(statement);
        connection.setAutoCommit(false);
        for (int i = 0; i <= 1000000; i++) {
            preparedStatement.setInt(1, i);
            preparedStatement.setString(2, i + "customerName");
            preparedStatement.setString(3, i + "customerLastName");
            preparedStatement.setString(4, i + "customerEGN");
            preparedStatement.execute();
        }
        connection.commit();
        preparedStatement.close();
        connection.close();
    }
}
