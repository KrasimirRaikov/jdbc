package com.clouway.jdbc.database.operation.persistence.user;

import com.clouway.jdbc.ConnectionManager;
import com.clouway.jdbc.ExecutionException;
import com.clouway.jdbc.database.operation.persistence.Validator;

import java.sql.*;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentUserRepository implements UserRepository {
    private ConnectionManager connectionManager;
    private Validator validator;

    public PersistentUserRepository(ConnectionManager connectionManager, Validator validator) {
        this.connectionManager = connectionManager;
        this.validator = validator;
    }

    @Override
    public void register(User user) {
        Connection connection = connectionManager.getConnection("users", "postgres", "clouway.com");
        if (!validator.isValid(user)) {
            throw new ExecutionException("unvalid user");
        }
        String sqlStatement = "INSERT INTO users VALUES(?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sqlStatement);

            preparedStatement.setLong(1, user.id);
            preparedStatement.setString(2, user.name);
            preparedStatement.setString(3, user.lastName);
            preparedStatement.setString(4, user.egn);
            preparedStatement.setInt(5, user.age);

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new ExecutionException("Could not register user with id: " + user.id);
        } finally {
            closeStatement(preparedStatement);
            closeConnection(connection);
        }
    }

    @Override
    public User findById(Long id) {
        Connection connection = connectionManager.getConnection("users", "postgres", "clouway.com");

        String selectById = "SELECT * FROM users WHERE id=?;";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(selectById);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
            resultSet = preparedStatement.executeQuery();

            resultSet.next();
            Long userId = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("last_name");
            String egn = resultSet.getString("egn");
            int age = resultSet.getInt("age");

            return new User(userId, name, surname, egn, age);
        } catch (SQLException e) {
            throw new ExecutionException("Could not find user with such id: " + id);
        } finally {
            closeStatement(preparedStatement);
            closeConnection(connection);
        }
    }

    @Override
    public User findByEgn(String egn) {
        Connection connection = connectionManager.getConnection("users", "postgres", "clouway.com");

        String sqlStatement = "SELECT * FROM users WHERE egn=?;";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, egn);

            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            long userId = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("last_name");
            String egnReturned = resultSet.getString("egn");
            int age = resultSet.getInt("age");

            return new User(userId, name, surname, egnReturned, age);
        } catch (SQLException e) {
            throw new ExecutionException("No users with such EGN: " + egn);
        } finally {
            closeStatement(preparedStatement);
            closeConnection(connection);
        }


    }

    @Override
    public void update(User user) {
        Connection connection = connectionManager.getConnection("users", "postgres", "clouway.com");
        if (!validator.isValid(user)) {
            throw new ExecutionException("unvalid user");
        }
        String sqlStatement = "UPDATE users SET name=?, last_name=?, egn=?, age=? WHERE id=?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlStatement);

            preparedStatement.setString(1, user.name);
            preparedStatement.setString(2, user.lastName);
            preparedStatement.setString(3, user.egn);
            preparedStatement.setInt(4, user.age);
            preparedStatement.setLong(5, user.id);
            if (preparedStatement.executeUpdate() == 0) {
                throw new ExecutionException("could not update user with id: " + user.id);
            }
        } catch (SQLException e) {
            throw new ExecutionException("could not update user with id: " + user.id);
        } finally {
            closeStatement(preparedStatement);
            closeConnection(connection);
        }
    }

    @Override
    public void delete(Long id) {
        Connection connection = connectionManager.getConnection("users", "postgres", "clouway.com");

        String deleteById = "DELETE FROM users WHERE id=?;";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(deleteById);
            preparedStatement.setLong(1, id);

            if (preparedStatement.executeUpdate() == 0) {
                throw new ExecutionException("Could not delete user with id: " + id);
            }
        } catch (SQLException e) {
            throw new ExecutionException("Could not delete user with id: " + id);
        } finally {
            closeStatement(preparedStatement);
            closeConnection(connection);
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

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new ExecutionException("Could not close the repository connection");
        }
    }

}
