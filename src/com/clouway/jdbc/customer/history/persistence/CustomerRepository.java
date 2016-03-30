package com.clouway.jdbc.customer.history.persistence;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface CustomerRepository {
    void register(Customer customer);

    Customer getById(long id);

    void update(Customer customer);
}
