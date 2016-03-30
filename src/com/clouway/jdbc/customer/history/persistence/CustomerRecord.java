package com.clouway.jdbc.customer.history.persistence;

import java.sql.Timestamp;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class CustomerRecord {
    public final long recordId;
    public final Timestamp changeDate;
    public final long customerId;
    public final String name;
    public final String lastName;
    public final String egn;

    public CustomerRecord(long recordId, Timestamp changeDate, long customerId, String name, String lastName, String egn) {
        this.recordId = recordId;
        this.changeDate = changeDate;
        this.customerId = customerId;
        this.name = name;
        this.lastName = lastName;
        this.egn = egn;
    }
}
