package com.clouway.jdbc.database.operation;

import java.sql.*;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class UserRepositoryManager {

    private Connection connection;

    public UserRepositoryManager(Connection connection) {
        this.connection = connection;
    }

    public boolean tableExists(String tableName) throws SQLException {
        String tableExistsQuery = "SELECT exists ( SELECT 1 FROM information_schema.tables" +
                " WHERE table_name = ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(tableExistsQuery);
        preparedStatement.setString(1, tableName);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        boolean tableExists = resultSet.getBoolean(1);
        preparedStatement.close();
        resultSet.close();
        return tableExists;
    }

    public void destroyTable(String tableName) throws SQLException {
        String dropTable = String.format("DROP TABLE IF EXISTS %s ;", tableName);
        Statement statement = connection.createStatement();
        statement.execute(dropTable);
        statement.close();
    }

    public void createRepository() throws SQLException {
        String createUserTable = "CREATE TABLE users(id INT NOT NULL, name TEXT NOT NULL, surname TEXT, egn TEXT, age INTEGER);";
        Statement statement = connection.createStatement();
        statement.execute(createUserTable);
        statement.close();
    }

    public void renameTable(String tableName, String newTableName) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("ALTER TABLE " + tableName + " RENAME TO  " + newTableName + ";");
        statement.close();
    }
}
