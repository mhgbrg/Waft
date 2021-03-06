package com.alive_n_clickin.waft.application;

import android.content.Context;

import com.alive_n_clickin.waft.infrastructure.WifiBroadcastReceiver;
import com.alive_n_clickin.waft.infrastructure.WifiHelper;
import com.alive_n_clickin.waft.util.event.IEvent;
import com.alive_n_clickin.waft.util.event.IObservable;
import com.alive_n_clickin.waft.util.event.IObservableHelper;
import com.alive_n_clickin.waft.util.event.IObserver;
import com.alive_n_clickin.waft.infrastructure.NewWifiScanAvailableEvent;
import com.alive_n_clickin.waft.util.event.ObservableHelper;
import com.alive_n_clickin.waft.infrastructure.WifiStateChangeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class listens to NewWifiScanAvailableEvents and checks if any nearby BSSID belongs to a known bus.
 * If it finds a match, it sends a NewBusNearbyEvent with the DGW of the found bus. If no match is
 * found, it sends a NewBusNearbyEvent with the DGW parameter set to null.
 *
 * @since 0.2
 */

/* Note: The current implementation of this class relies solely on wifi, but the interface must not reveal that fact.
If, in the future, another method of detecting nearby vehicles is implemented, the interface should remain the same.
Anyone using this class can trust *that* it can detect nearby vehicles, not *how*
 */
public class NearbyBusScanner implements IObserver, IObservable {

    private static final String TEST_BUS_DGW = "Ericsson$Vin_Num_001";
    private IObservableHelper observableHelper = new ObservableHelper();
    private WifiBroadcastReceiver wifiBroadcastReceiver;
    private Context context;

    private static Map<String, String> buses = new HashMap<>();

    /**
     * Initiates a NearbyBusScanner and registers it as an observer to the specified WifiBroadcastReceiver.
     *
     * The scanner uses the Android system, and thus needs the application context
     *
     * @param wifiBroadcastReceiver the WifiBroadcastReceiver to listen to for WifiBSSIDChangeEvents.
     * @param applicationContext the context for the application
     */
    public NearbyBusScanner(WifiBroadcastReceiver wifiBroadcastReceiver, Context applicationContext) {
        this.wifiBroadcastReceiver = wifiBroadcastReceiver;
        this.wifiBroadcastReceiver.addObserver(this);
        this.context = applicationContext;

        buses.put("04:f0:21:10:0a:07", "Ericsson$100020"); // EPO 131
        buses.put("04:f0:21:10:09:df", "Ericsson$100021"); // EPO 136
        buses.put("04:f0:21:10:09:e8", "Ericsson$100022"); // EPO 143
        buses.put("04:f0:21:10:09:b8", "Ericsson$171164"); // EOG 604
        buses.put("04:f0:21:10:09:e7", "Ericsson$171234"); // EOG 606
        buses.put("04:f0:21:10:09:5b", "Ericsson$171235"); // EOG 616
        buses.put("04:f0:21:10:09:53", "Ericsson$171327"); // EOG 622
        buses.put("04:f0:21:10:09:b9", "Ericsson$171164"); // EOG 627
        buses.put("04:f0:21:10:09:b7", "Ericsson$171330"); // EOG 634
    }

    /**
     * {@inheritDoc}<br><br>
     *
     * This implementation of onEvent gets the BSSID:s from the NewWifiScanAvailableEvent and checks if
     * the BSSID of any known bus is in the list. If the list contains a known bus, a NewBusNearbyEvent
     * is sent to all observers with the DGW of the bus. If the list doesn't contain a known bus,
     * a NewBusNearbyEvent is sent to all observers with the DGW parameter set to null.
     *
     * @param event
     */
    @Override
    public void onEvent(IEvent event) {
        if (event instanceof NewWifiScanAvailableEvent) {
            handleWifiBSSIDChangeEvent((NewWifiScanAvailableEvent) event);
        } else if (event instanceof WifiStateChangeEvent) {
            handleWifiStateChangeEvent((WifiStateChangeEvent) event);
        }
    }

    private void handleWifiBSSIDChangeEvent(NewWifiScanAvailableEvent event) {
        //If we don't find the bus in our list, we always return the test bus, for testing purposes
        String DGW = TEST_BUS_DGW;

        List<String> BSSIDs = event.getBSSIDs();
        for (String BSSID : BSSIDs) {
            if (buses.containsKey(BSSID)) {
                DGW = buses.get(BSSID);
                break;
            }
        }

        observableHelper.notifyObservers(new NewBusNearbyEvent(DGW));
    }

    private void handleWifiStateChangeEvent(WifiStateChangeEvent event) {
        if (!event.isWifiEnabled()) {
            observableHelper.notifyObservers(new CantSearchForVehiclesEvent());
        }
    }

    @Override
    public void addObserver(IObserver observer) {
        observableHelper.addObserver(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        observableHelper.removeObserver(observer);
    }

    /**
     * Enable searching and perform a new search. This might change android system settings, like
     * location services and wifi state.
     */
    public void enableSearchingAndSearch() {
        WifiHelper wifiHelper = new WifiHelper(context);
        wifiHelper.enableWifi();
        wifiHelper.initiateWifiScan();
    }

    /**
     * Says whether the scanner can perfomr searches or not.
     * @return
     */
    public boolean canSearch() {
        return new WifiHelper(context).isWifiEnabled();
    }
}
