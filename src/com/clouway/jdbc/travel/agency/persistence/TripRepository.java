package com.clouway.jdbc.travel.agency.persistence;

import java.sql.Date;
import java.util.List;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public interface TripRepository {
    void register(Trip trip);

    Trip getById(long id);

    void update(Trip trip);

    List<Trip> getAll();

    List<String> citiesByPopularity();

    List<Client> clientTripsOverlapBetween(Date startDate, Date endDate, String city);
}
