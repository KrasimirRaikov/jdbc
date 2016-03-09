package com.clouway.jdbc.database.operation.persistence;

import com.clouway.jdbc.database.operation.ConnectionManager;
import com.clouway.jdbc.database.operation.UserRepositoryManager;
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
public class UserRepositoryManagerTest {
    Connection connection = null;
    UserRepositoryManager repositoryManager = null;

    @Before
    public void setUp() throws Exception {
        ConnectionManager connectionManager = new ConnectionManager();
        connection = connectionManager.getConnection("users", "postgres", "clouway.com");
        repositoryManager = new UserRepositoryManager(connection);
    }

    @Test
    public void tableExists() throws SQLException {
        boolean usersTableExists = repositoryManager.tableExists("users");
        assertThat(usersTableExists, is(equalTo(true)));
    }

    @Test
    public void tableDoesNotExists() throws SQLException {
        boolean clientsDosntExist = repositoryManager.tableExists("clients");
        assertThat(clientsDosntExist, is(equalTo(false)));
    }

    @Test
    public void renameTable() throws SQLException {
        repositoryManager.renameTable("users", "renamed_users");
        boolean renamed = repositoryManager.tableExists("renamed_users");

        repositoryManager.renameTable("renamed_users", "users");
        boolean returnedOldName = repositoryManager.tableExists("users");

        assertThat(renamed, is(equalTo(true)));
        assertThat(returnedOldName, is(equalTo(true)));
    }

    @Test
    public void destroyTable() throws SQLException {
        repositoryManager.destroyTable("users");
        boolean tableDestroyed = !repositoryManager.tableExists("users");
        repositoryManager.createRepository();
        assertThat(tableDestroyed, is(equalTo(true)));
    }
}
