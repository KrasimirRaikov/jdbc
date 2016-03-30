package com.clouway.jdbc.customer.history.persistence;

import com.clouway.jdbc.ExecutionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentCustomerHistoryRepository implements CustomerHistoryRepository {
    private Connection connection;

    public PersistentCustomerHistoryRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public CustomerRecord getLastUpdate(long id) {
        String selectById = "SELECT * FROM customer_history WHERE customer_id=?;";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(selectById);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long recordId = resultSet.getLong("id");
            Timestamp changeDate = resultSet.getTimestamp("change_date");
            long customerId = resultSet.getLong("customer_id");
            String name = resultSet.getString("name");
            String lastName = resultSet.getString("last_name");
            String egn = resultSet.getString("egn");
            return new CustomerRecord(recordId, changeDate, customerId, name, lastName, egn);
        } catch (SQLException e) {
            throw new ExecutionException("Could not load last update history of Customer with id: " + id);
        } finally {
            closeStatement(preparedStatement);
        }
    }

    @Override
    public List<CustomerRecord> getByCustomerId(long id) {
        String selectById = "SELECT * FROM customer_history WHERE customer_id=?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(selectById);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return getRecords(resultSet);
        } catch (SQLException e) {
            throw new ExecutionException("Could not find update record with id: " + id);
        } finally {
            closeStatement(preparedStatement);
        }
    }

    @Override
    public List<CustomerRecord> getUpdates() {
        String selectById = "SELECT * FROM customer_history";

        Statement statement = null;
        try {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(selectById);
            return getRecords(resultSet);
        } catch (SQLException e) {
            throw new ExecutionException("Could not load the update history");
        } finally {
            closeStatement(statement);
        }
    }

    private List<CustomerRecord> getRecords(ResultSet resultSet) throws SQLException {
        List<CustomerRecord> customerRecords = new ArrayList<>();
        try {
            while (resultSet.next()) {
                long recordId = resultSet.getLong("id");
                Timestamp changeDate = resultSet.getTimestamp("change_date");
                long customerId = resultSet.getLong("customer_id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("last_name");
                String egn = resultSet.getString("egn");
                customerRecords.add(new CustomerRecord(recordId, changeDate, customerId, name, lastName, egn));
            }
            return customerRecords;
        } finally {
            resultSet.close();
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
