package com.clouway.jdbc.info.users.persistence;

import com.clouway.jdbc.ExecutionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentContactRepository implements ContactRepository {


    private ConnectionPool connectionPool;

    public PersistentContactRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void register(Long contactId, Long userId, Long addressId) {
        Connection connection = connectionPool.aquire();
        String sqlStatement = "INSERT INTO contact VALUES(?, ?, ?);";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, contactId);
            preparedStatement.setLong(2, userId);
            preparedStatement.setLong(3, addressId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new ExecutionException("Could not register the contact: " + userId);
        } finally {
            closeStatement(preparedStatement);
            connectionPool.release(connection);
        }

    }

    @Override
    public Contact findById(Long id) {
        Connection connection = connectionPool.aquire();
        String selectById = "SELECT contact.id as id, users.name as name, " +
                "address.residence as residence, address.street as street FROM contact " +
                "INNER JOIN users on contact.user_id = users.id " +
                "INNER JOIN address ON contact.address_id = address.id WHERE contact.id=?;";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(selectById);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long contactId = resultSet.getLong("id");
                String userName = resultSet.getString("name");
                String residence = resultSet.getString("residence");
                String street = resultSet.getString("street");
                return new Contact(contactId, userName, residence, street);
            } else {
                throw new ExecutionException("could not find contact with such id: " + id);
            }
        } catch (SQLException e) {
            throw new ExecutionException("could not find contact with such id: " + id);
        } finally {
            closeStatement(preparedStatement);
            connectionPool.release(connection);
        }

    }

    @Override
    public List<Contact> findAll() {
        Connection connection = connectionPool.aquire();
        String contactQuery = "SELECT contact.id as id, users.name as name, " +
                "address.residence as residence, address.street as street FROM contact " +
                "INNER JOIN users on contact.user_id = users.id " +
                "INNER JOIN address ON contact.address_id = address.id;";
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(contactQuery);
            List<Contact> contactList = new ArrayList<Contact>();
            while (resultSet.next()) {
                Long contactId = resultSet.getLong("id");
                String userName = resultSet.getString("name");
                String residence = resultSet.getString("residence");
                String street = resultSet.getString("street");
                contactList.add(new Contact(contactId, userName, residence, street));
            }
            return contactList;
        } catch (SQLException e) {
            throw new ExecutionException("Could not load the contact list");
        } finally {
            closeStatement(statement);
            connectionPool.release(connection);
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
