package com.clouway.jdbc.task2.util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Slavi Dichkov (slavidichkof@gmail.com)
 */
public class DatabaseCleaner {

    private final DataSource dataSource;
    private final String[] tables;

    public DatabaseCleaner(DataSource dataSource, String... users) {
        this.dataSource = dataSource;
        this.tables = users;
    }

    public void cleanUp() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (String table : tables) {
            try {
                connection.createStatement().execute("delete from " + table);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}