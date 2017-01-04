/* This file contains code that manages GPS distance calculations
*
* Author: Josh McIntyre
*
*/
package com.jmcintyre.singletrackstretch;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

/* This class defines GPS distance calculation handling functions
*
*/
public class Distance {

    /* This block defines constants for distance tracking
    *
    */
    private static final long MIN_TIME_CHANGE_MS = 5000;
    private static final long MIN_DISTANCE_CHANGE_M = 0;
    private static final String DISTANCE_STRING_UNITS = " km";

    /* This block defines variables for distance tracking
    *
    */
    private Activity distanceActivity;
    private Context context;
    private LocationManager locationManager;
    private float distanceKm;
    private Location lastLocation;

    /* This block creates a location listener to track distanceKm
    *
    */
    private LocationListener locationListener = new LocationListener()
    {
        public void onLocationChanged(Location location)
        {
            /* Update the distance
            * Distance to returns meters, so convert to km
            *
            */
            try
            {
                float distanceDiff = lastLocation.distanceTo(location);
                distanceKm += distanceDiff / 1000;
                lastLocation.set(location);
            }
            catch (NullPointerException e)
            {
                /* If lastLocation isn't yet set, wait until the next update
                *
                */
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras){}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}

    };

    /* This constructor initializes distance tracking
    *
    */
    Distance(Activity distanceActivity, Context context)
    {
        this.distanceActivity = distanceActivity;
        this.context = context;

        locationManager = (LocationManager) distanceActivity.getSystemService(Context.LOCATION_SERVICE);

        this.distanceKm = 0.0f;
    }

    /* This function starts GPS tracking
    *
    */
    public void startTracking()
    {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_CHANGE_MS, MIN_DISTANCE_CHANGE_M, locationListener);

            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    /* This function stops GPS tracking
    *
    */
    public void stopTracking()
    {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.removeUpdates(locationListener);
        }

        lastLocation = null;
    }

    /* This getter retrieves the current distanceKm traveled formatted as a string
    *
    * Return: distanceString
    *
    */
    public String getDistanceString()
    {
        return String.format("%.2f", distanceKm) + DISTANCE_STRING_UNITS;
    }

    /* This getter retrieves the current distanceKm traveled formatted as a string
    *
    * Return: distanceString
    *
    */
    public String getCurrentLocationString()
    {
        if (! (lastLocation == null))
        {
            return Double.toString(lastLocation.getLatitude()) + ", " + Double.toString(lastLocation.getLongitude());
        }
        else
        {
            return "No location data available";
        }
    }
}