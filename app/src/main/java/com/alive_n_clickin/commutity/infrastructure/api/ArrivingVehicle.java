package com.alive_n_clickin.commutity.infrastructure.api;

import android.util.Log;

import com.alive_n_clickin.commutity.domain.Bus;
import com.alive_n_clickin.commutity.domain.Flag;
import com.alive_n_clickin.commutity.domain.IFlag;
import com.alive_n_clickin.commutity.util.LogUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import lombok.ToString;

/**
 * Consists the data of a vehicle and it's arrival as per the Vasttrafik API. To ease further use
 * of this class it has built in methods for converting the arrival date and time to a Java Date
 * object, as well as calculating the time to arrival.
 */
@ToString
public class ArrivingVehicle{
    /**
     * Scheduled arrival time
     */
    private String time;
    /**
     * The real arrival time, including delays
     */
    private String rtTime;
    private String date;
    private long journeyid;
    private String direction = "Mot lindholmen";
    private String name = "";
    private String routeNumber = "55";
    private DateFormat dateFormatter;

    /**
     * Constructor, initializes the date formatter
     */
    public ArrivingVehicle(){
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));
    }

    //TODO Just for testing, change this when we implement another key value for the vehiclesg
    public Bus getBus() {
        List<IFlag> list = new ArrayList<>();
        list.add(new Flag(Flag.Type.OVERCROWDED, "", new Date()));
        return new Bus("", direction, name, routeNumber, list);
    }

    /**
     * Creates a new Java Date for when the vehicle is scheduled to arrive, based on the information
     * given from the API call
     * @return  The date and time the vehicle is scheduled to arrive
     */
    public Date getScheduledArrival(){
        Date arrival = new Date();
        try {
            arrival = dateFormatter.parse(date + " " + time);
        } catch (ParseException e) {
            Log.e(LogUtils.getLogTag(this), e.getStackTrace().toString());
        }
        return arrival;
    }

    /**
     * Creates a new Java Date for when the vehicle will arrive, including delays if present,
     * based on the information given from the API call
     * @return  The date and time the vehicle will arrive
     */
    public Date getRealArrival(){
        Date arrival = new Date();
        try {
            arrival = dateFormatter.parse(date + " " + rtTime);
        } catch (ParseException e) {
            Log.e(LogUtils.getLogTag(this), e.getStackTrace().toString());
        }
        return arrival;
    }

    /**
     * This method provides the differences between the real arrival time and current time.
     * @return the difference in time units (milliseconds)
     */
    public Long getRealTimeToArrival() {
        return getRealArrival().getTime() - System.currentTimeMillis();
    }

    /**
     * This method provides the differences between the scheduled arrival time and current time.
     * @return the difference in time units (milliseconds)
     */
    public Long getScheduledTimeToArrival() {
        return getScheduledArrival().getTime() - System.currentTimeMillis();
    }
}
