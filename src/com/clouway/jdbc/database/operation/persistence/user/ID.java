package com.clouway.jdbc.database.operation.persistence.user;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class ID {

    public final int value;

    public ID(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ID id = (ID) o;

        return value == id.value;

    }

    @Override
    public int hashCode() {
        return value;
    }
}