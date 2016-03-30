package com.clouway.jdbc.info.users.persistence;

import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface UserRepository {
    void register(User user);

    User findById(long id);

    List<User> findAll();
}
