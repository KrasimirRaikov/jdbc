package com.clouway.jdbc.customer.history.persistence;

import com.clouway.jdbc.ExecutionException;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentCustomerRepository implements CustomerRepository {
    private Connection connection;

    public PersistentCustomerRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void register(Customer customer) {
        String sqlStatement = "INSERT INTO customer VALUES(?, ?, ?, ?);";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, customer.id);
            preparedStatement.setString(2, customer.name);
            preparedStatement.setString(3, customer.lastName);
            preparedStatement.setString(4, customer.egn);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new ExecutionException("Could not register customer");
        } finally {
            closeStatement(preparedStatement);
        }
    }

    @Override
    public Customer getById(long id) {
        String selectById = "SELECT * FROM customer WHERE id=?;";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(selectById);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long customerId = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("last_name");
                String egn = resultSet.getString("egn");
                return new Customer(customerId, name, lastName, egn);
            } else {
                throw new NoSuchElementException("No users with such id.");
            }
        } catch (SQLException e) {
            throw new ExecutionException("Could not find any customer with such id" + id);
        } finally {
            closeStatement(preparedStatement);
        }
    }

    @Override
    public void update(Customer customer) {
        String sqlStatement = "UPDATE customer SET name=?, last_name=?, egn=? WHERE id=?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, customer.name);
            preparedStatement.setString(2, customer.lastName);
            preparedStatement.setString(3, customer.egn);
            preparedStatement.setLong(4, customer.id);
            if (preparedStatement.executeUpdate() == 0) {
                throw new ExecutionException("Can't update customer with id: " + customer.id);
            }
        } catch (SQLException e) {
            throw new ExecutionException("Could not update customer with id: " + customer.id);
        } finally {
            closeStatement(preparedStatement);
        }
    }

    private void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
