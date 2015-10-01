package com.alive_n_clickin.commutity.infrastructure;

import java.util.List;


public interface IVasttrafikAdapter {

     List<Stop> getNearbyStations(double longitude,double latitude);
     List<Stop> getSearchStops(String searchString);
}
