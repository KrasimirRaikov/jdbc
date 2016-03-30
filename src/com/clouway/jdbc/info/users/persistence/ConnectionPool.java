package com.clouway.jdbc.info.users.persistence;

import com.clouway.jdbc.ExecutionException;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.clouway.jdbc.ConnectionProvider.getConnection;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ConnectionPool {
    private static ConnectionPool ourInstance = new ConnectionPool();
    private final int maxConnections = 3;
    private ConcurrentLinkedQueue<Connection> availableConnections = new ConcurrentLinkedQueue<Connection>();
    private List<Connection> busyConnections = new ArrayList<Connection>();

    public static ConnectionPool getInstance() {
        return ourInstance;
    }


    public Connection aquire() {
        if (availableConnections.size() > 0) {
            Connection connection = availableConnections.poll();
            busyConnections.add(connection);
            return connection;
        } else if (busyConnections.size() < maxConnections) {
            Connection connection = getConnection("user_info", "postgres", "clouway.com");
            busyConnections.add(connection);
            return connection;
        } else {
            throw new ExecutionException("No available connections");
        }
    }

    public void release(Connection connection) {
        if (busyConnections.contains(connection)) {
            busyConnections.remove(connection);
            availableConnections.add(connection);
        } else {
            throw new ExecutionException("Sorry this connection is not from this pool");
        }
    }


    private ConnectionPool() {
    }
}
