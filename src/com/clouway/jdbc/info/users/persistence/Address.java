package com.clouway.jdbc.info.users.persistence;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Address {
    public final Long id;
    public final String residence;
    public final String street;

    public Address(Long id, String residence, String street) {
        this.id = id;
        this.residence = residence;
        this.street = street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (id != null ? !id.equals(address.id) : address.id != null) return false;
        if (residence != null ? !residence.equals(address.residence) : address.residence != null) return false;
        return !(street != null ? !street.equals(address.street) : address.street != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (residence != null ? residence.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        return result;
    }
}
