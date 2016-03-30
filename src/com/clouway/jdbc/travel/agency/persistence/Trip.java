package com.clouway.jdbc.travel.agency.persistence;


import java.sql.Date;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class Trip {
    public final long id;
    public final String egn;
    public final Date arrival;
    public final Date departure;
    public final String destination;

    public Trip(long id, String egn, Date arrival, Date departure, String destination) {
        this.id = id;
        this.egn = egn;
        this.arrival = arrival;
        this.departure = departure;
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trip trip = (Trip) o;

        if (id != trip.id) return false;
        if (egn != null ? !egn.equals(trip.egn) : trip.egn != null) return false;
        if (arrival != null ? !arrival.equals(trip.arrival) : trip.arrival != null) return false;
        if (departure != null ? !departure.equals(trip.departure) : trip.departure != null) return false;
        return !(destination != null ? !destination.equals(trip.destination) : trip.destination != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (egn != null ? egn.hashCode() : 0);
        result = 31 * result + (arrival != null ? arrival.hashCode() : 0);
        result = 31 * result + (departure != null ? departure.hashCode() : 0);
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        return result;
    }
}
