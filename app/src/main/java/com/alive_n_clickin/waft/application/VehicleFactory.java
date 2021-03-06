package com.alive_n_clickin.waft.application;

import android.util.Log;

import com.alive_n_clickin.waft.application.api.ApiAdapterFactory;
import com.alive_n_clickin.waft.application.api.IElectriCityAdapter;
import com.alive_n_clickin.waft.application.api.IWaftAdapter;
import com.alive_n_clickin.waft.domain.ElectriCityBus;
import com.alive_n_clickin.waft.domain.IElectriCityBus;
import com.alive_n_clickin.waft.domain.IFlag;
import com.alive_n_clickin.waft.domain.IJourney;
import com.alive_n_clickin.waft.domain.Journey;
import com.alive_n_clickin.waft.infrastructure.api.ConnectionException;
import com.alive_n_clickin.waft.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a factory for creating ready-to-use vehicles. The idea is that you ask this factory
 * to build a vehicle from existing data (for example an ID or a response object received from
 * an external API), and that the factory simply responds with a vehicle. The factory
 * is responsible for fetching all the data required for that vehicle from whatever sources it needs.
 *
 * @since 0.2
 */
public class VehicleFactory {
    private static final String LOG_TAG = LogUtils.getLogTag(VehicleFactory.class);

    private static final IWaftAdapter waftAdapter = ApiAdapterFactory.createWaftAdapter();
    private static final IElectriCityAdapter electriCityAdapter = ApiAdapterFactory.createElectricityAdapter();

    /**
     * Takes a dgw and returns a new bus object with all the data for the bus with that DGW.
     *
     * This method may not be called from the UI thread.
     *
     * @param dgw the dgw id for the bus you want to have.
     * @return a new bus object. Null if anything goes wrong when fetching data for the bus.
     */
    public static IElectriCityBus getElectriCityBus(String dgw) {
        IJourney journey;
        try {
            journey = electriCityAdapter.getCurrentJourney(dgw);
        } catch (ConnectionException e) {
            Log.e(LOG_TAG, "Error fetching current journey for bus", e);
            journey = new Journey("Okänd destination", "NO_JOURNEY_ID");
        }

        String destination = journey.getDestination();
        String journeyId = journey.getJourneyId();

        List<IFlag> flags;
        try {
            flags = waftAdapter.getFlagsForVehicle(journeyId);
        } catch (ConnectionException e) {
            Log.e(LOG_TAG, "Error fetching flags for vehicle", e);
            flags = new ArrayList<>();
        }

        return new ElectriCityBus(destination, journeyId, dgw, flags);
    }
}
