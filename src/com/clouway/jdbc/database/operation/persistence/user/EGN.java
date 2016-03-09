package com.clouway.jdbc.database.operation.persistence.user;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class EGN {

    public final String value;

    public EGN(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EGN egn = (EGN) o;

        return !(value != null ? !value.equals(egn.value) : egn.value != null);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
