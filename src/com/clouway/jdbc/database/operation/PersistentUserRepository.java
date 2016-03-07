package com.clouway.jdbc.database.operation;

import java.sql.*;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class PersistentUserRepository {
    Connection connection;

    public PersistentUserRepository(Connection connection) {
        this.connection = connection;
    }

    public void registerUser(User user) {
        String sqlStatement = "INSERT INTO users values(?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sqlStatement);


        preparedStatement.setInt(1, user.getId());
        preparedStatement.setString(2, user.getName());
        preparedStatement.setString(3, user.getSurname());
        preparedStatement.setString(4, user.getEgn());
        preparedStatement.setInt(5, user.getAge());

        preparedStatement.execute();
        preparedStatement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    public User getUser(int id) throws SQLException {
        String selectById = "select * from users where id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(selectById);
        preparedStatement.setInt(1, id);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            int userId = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String surname = resultSet.getString("surname");
            String egn = resultSet.getString("egn");
            int age = resultSet.getInt("age");
            preparedStatement.close();
            resultSet.close();
            return new User(userId, name, surname, egn, age);
        }else {
            return null;
        }
    }

    public void clear() throws SQLException {
        Statement statement = connection.createStatement();

        statement.execute("truncate table users;");
        statement.close();
    }

    public User getByEgn(String egn) throws SQLException {
        String sqlStatement = "Select * from users where egn=?;";
        PreparedStatement  preparedStatement= connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, egn);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int userId = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String surname = resultSet.getString("surname");
        String egnReturned = resultSet.getString("egn");
        int age = resultSet.getInt("age");
        preparedStatement.close();
        resultSet.close();

        return new User(userId, name, surname, egnReturned, age);
    }

    public void updateUser(User user) throws SQLException {
        String sqlStatement = "UPDATE users SET name=?, surname=?, egn=?, age=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getSurname());
        preparedStatement.setString(3, user.getEgn());
        preparedStatement.setInt(4, user.getAge());
        preparedStatement.setInt(5, user.getId());

        preparedStatement.execute();
        preparedStatement.close();
    }

    public void deleteUser(int id) throws SQLException {
        String deleteById = "Delete from users where id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteById);
        preparedStatement.setInt(1, id);

        preparedStatement.execute();
        preparedStatement.close();
    }


    public void renameTable(String tableName, String newTableName) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("ALTER TABLE " + tableName + " RENAME TO  " + newTableName + ";");
        statement.close();
    }

    public boolean tableExists(String tableName) throws SQLException {
        String tableExists = "select exists ( select 1 from information_schema.tables" +
                " where table_name = ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(tableExists);
        preparedStatement.setString(1, tableName);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getBoolean(1);
    }
}
