package com.clouway.jdbc.info.users.persistence;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class User {
    public final long id;
    public final String name;

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
