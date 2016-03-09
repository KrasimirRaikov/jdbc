package com.clouway.jdbc.database.operation.persistence;

import java.sql.*;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class DatabaseTableTool {

    private Connection connection;

    public DatabaseTableTool(Connection connection) {
        this.connection = connection;
    }

    public boolean exists(String tableName) throws SQLException {
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

    public void destroy(String tableName) throws SQLException {
        String dropTable = String.format("DROP TABLE IF EXISTS %s ;", tableName);
        Statement statement = connection.createStatement();
        statement.execute(dropTable);
        statement.close();
    }

    public void create(String tableStructure) throws SQLException {
        String createUserTable = "CREATE TABLE " + tableStructure + ";";
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
