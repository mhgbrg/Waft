package com.alive_n_clickin.commutity.domain;

import lombok.Getter;
import lombok.NonNull;

/**
 * An abstract class all other vehicles should extend. Holds a destination, a line number and
 * a journey number
 */
public abstract class Vehicle {
    @Getter private String destination;
    @Getter private String shortName;
    @Getter private long journeyID;

    /**
     * Constructor
     * @param destination Where the vehicle is headed, e.g. "Sahlgrenska"
     * @param shortRouteName    The short version of the route name, usually simply a line number
     *                          (e.g. "55"), but can also be longer, e.g. "Grön Express"
     * @param journeyID from Vasttrafik. This is the unique identification number for a certain
     *                  route. It gets changed any time the vehicle arrives to the end stop and
     *                  continues in the opposite direction.
     * @throws NullPointerException if any parameter is null
     */
    public Vehicle(@NonNull String destination, @NonNull String shortRouteName, long journeyID){
        this.destination = destination;
        this.shortName = shortRouteName;
        this.journeyID = journeyID;
    }
}