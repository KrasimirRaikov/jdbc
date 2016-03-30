package com.clouway.jdbc.info.users.persistence;

import com.clouway.jdbc.ExecutionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentUserRepository implements UserRepository {
    private ConnectionPool connectionPool;

    public PersistentUserRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void register(User user) {
        Connection connection = connectionPool.aquire();
        String sqlStatement = "INSERT INTO users VALUES(?, ?);";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setLong(1, user.id);
            preparedStatement.setString(2, user.name);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new ExecutionException("Could not register user: " + user.id);
        } finally {
            closeStatement(preparedStatement);
            connectionPool.release(connection);
        }
    }

    @Override
    public User findById(long id) {
        Connection connection = connectionPool.aquire();
        String selectById = "SELECT * FROM users WHERE id=?;";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(selectById);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            long userId = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new User(userId, name);

        } catch (SQLException e) {
            throw new ExecutionException("Could not find street with that id.");
        } finally {
            closeStatement(preparedStatement);
            connectionPool.release(connection);
        }
    }

    @Override
    public List<User> findAll() {
        Connection connection = connectionPool.aquire();
        String usersQuery = "SELECT * FROM users";
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(usersQuery);
            List<User> userList = new ArrayList<User>();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                userList.add(new User(id, name));
            }
            return userList;
        } catch (SQLException e) {
            throw new ExecutionException("Could not getConnection the list of addresses");
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
