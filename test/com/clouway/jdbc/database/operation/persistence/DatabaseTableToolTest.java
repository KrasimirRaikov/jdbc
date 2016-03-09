package com.clouway.jdbc.database.operation.persistence;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class DatabaseTableToolTest {
    Connection connection = null;
    DatabaseTableTool tableTool = null;

    @Before
    public void setUp() throws Exception {
        ConnectionManager connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("users", "postgres", "clouway.com");
        tableTool = new DatabaseTableTool(connection);
    }

    @Test
    public void tableExists() throws SQLException {
        boolean usersTableExists = tableTool.exists("users");
        assertThat(usersTableExists, is(equalTo(true)));
    }

    @Test
    public void tableDoesNotExists() throws SQLException {
        boolean clientsDoesNotExist = tableTool.exists("clients");
        assertThat(clientsDoesNotExist, is(equalTo(false)));
    }

    @Test
    public void renameTable() throws SQLException {
        tableTool.renameTable("users", "renamed_users");
        boolean renamed = tableTool.exists("renamed_users");

        tableTool.renameTable("renamed_users", "users");
        boolean returnedOldName = tableTool.exists("users");

        assertThat(renamed, is(equalTo(true)));
        assertThat(returnedOldName, is(equalTo(true)));
    }

    @Test
    public void destroyTable() throws SQLException {
        tableTool.destroy("users");
        boolean tableDestroyed = !tableTool.exists("users");
        tableTool.create("users(id INT NOT NULL, name TEXT NOT NULL, surname TEXT, egn TEXT, age INTEGER)");
        assertThat(tableDestroyed, is(equalTo(true)));
    }

}
