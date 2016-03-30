package com.clouway.jdbc.info.users.persistence;

import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface ContactRepository {
    void register(Long contactId, Long userId, Long addressId);

    Contact findById(Long id);

    List<Contact> findAll();
}
