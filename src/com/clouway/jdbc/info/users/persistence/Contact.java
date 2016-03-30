package com.clouway.jdbc.info.users.persistence;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Contact {

    public final Long id;
    public final String userName;
    public final String residence;
    public final String street;

    public Contact(Long id, String userName, String residence, String street) {

        this.id = id;
        this.userName = userName;
        this.residence = residence;
        this.street = street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (id != null ? !id.equals(contact.id) : contact.id != null) return false;
        if (userName != null ? !userName.equals(contact.userName) : contact.userName != null) return false;
        if (residence != null ? !residence.equals(contact.residence) : contact.residence != null) return false;
        return !(street != null ? !street.equals(contact.street) : contact.street != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (residence != null ? residence.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        return result;
    }
}
