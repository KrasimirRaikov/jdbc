package com.clouway.jdbc.customer.history.persistence;

import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface CustomerHistoryRepository {

    CustomerRecord getLastUpdate(long id);

    List<CustomerRecord> getByCustomerId(long id);

    List<CustomerRecord> getUpdates();
}
