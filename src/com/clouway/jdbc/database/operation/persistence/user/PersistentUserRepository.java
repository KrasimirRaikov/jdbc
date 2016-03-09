package com.clouway.jdbc.database.operation.persistence.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentUserRepository {
    Connection connection;

    public PersistentUserRepository(Connection connection) {
        this.connection = connection;
    }

    public void register(User user) {
        String sqlStatement = "INSERT INTO users VALUES(?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, user.id.value);
            preparedStatement.setString(2, user.name);
            preparedStatement.setString(3, user.surname);
            preparedStatement.setString(4, user.egn.value);
            preparedStatement.setInt(5, user.age);
            preparedStatement.execute();

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findBy(ID id) throws SQLException {
        String selectById = "SELECT * FROM users WHERE id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(selectById);
        preparedStatement.setInt(1, id.value);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int userId = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");
            String egn = resultSet.getString("egn");
            int age = resultSet.getInt("age");
            preparedStatement.close();
            resultSet.close();
            return new User(new ID(userId), name, surname, new EGN(egn), age);
        } else {
            throw new NoSuchElementException("No users with such id.");
        }
    }

    public User findBy(EGN egn) throws SQLException {
        String sqlStatement = "SELECT * FROM users WHERE egn=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, egn.value);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int userId = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String surname = resultSet.getString("surname");
        String egnReturned = resultSet.getString("egn");
        int age = resultSet.getInt("age");
        preparedStatement.close();
        resultSet.close();

        return new User(new ID(userId), name, surname, new EGN(egnReturned), age);
    }

    public void update(User user) throws SQLException {
        String sqlStatement = "UPDATE users SET name=?, surname=?, egn=?, age=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, user.name);
        preparedStatement.setString(2, user.surname);
        preparedStatement.setString(3, user.egn.value);
        preparedStatement.setInt(4, user.age);
        preparedStatement.setInt(5, user.id.value);

        preparedStatement.execute();
        preparedStatement.close();
    }

    public void delete(ID id) throws SQLException {
        String deleteById = "DELETE FROM users WHERE id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteById);
        preparedStatement.setInt(1, id.value);

        preparedStatement.execute();
        preparedStatement.close();
    }

}
