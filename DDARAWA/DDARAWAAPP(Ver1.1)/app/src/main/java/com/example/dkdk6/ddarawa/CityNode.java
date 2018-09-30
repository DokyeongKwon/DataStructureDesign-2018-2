package com.example.dkdk6.ddarawa;

import android.location.Location;

import java.util.Locale;

/**
 * Created by dkdk6 on 2018-09-30.
 */

public class CityNode {
    City originCity;
    City computCity;
    Float distance;
    Double population;
    Float maxDistance;
    Double maxPopulation;
    Double weight;

    public CityNode(City o, City c){
        this.originCity = o;
        this.computCity = c;
        this.population = c.population;
        computeDistance();
    }

    public void computeDistance(){
        Location locationA = new Location("Origin");
        locationA.setLatitude(originCity.x_axis);
        locationA.setLongitude(originCity.y_axis);
        Location locationB = new Location("Computing");
        locationB.setLatitude(computCity.x_axis);
        locationB.setLongitude(computCity.y_axis);
        distance = locationA.distanceTo(locationB);
    }

}
