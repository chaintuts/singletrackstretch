/* This file contains code that manages GPS distance calculations
*
* Author: Josh McIntyre
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

// This class defines GPS distance calculation handling functions
public class Distance {

    // This block defines public constants for distance tracking
    public static final int UNITS_KM = 0;
    public static final int UNITS_MI = 1;

    // This block defines private constants for distance tracking
    private static final long MIN_TIME_CHANGE_MS = 5000;
    private static final long MIN_DISTANCE_CHANGE_M = 0;
    private static final String DISTANCE_STRING_UNITS_KM = " km";
    private static final String DISTANCE_STRING_UNITS_MI = " mi";
    private static final float KM_TO_MI = 0.621371f;

    // This block defines variables for distance tracking
    private Activity distanceActivity;
    private LocationManager locationManager;
    private float distanceKm;
    private Location lastLocation;

    // This block creates a location listener to track distanceKm
    private LocationListener locationListener = new LocationListener()
    {
        public void onLocationChanged(Location location)
        {
            /* Update the distance
            * Distance to returns meters, so convert to km
            */
            try
            {
                float distanceDiff = lastLocation.distanceTo(location);
                distanceKm += distanceDiff / 1000;
                lastLocation.set(location);
            }
            catch (NullPointerException e)
            {
                // If lastLocation isn't yet set, try again to retrieve the last known location
                try
                {
                    lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                catch (SecurityException se)
                {

                }
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras){}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}

    };

    /* The constructor initializes the distance to 0
    * This object is a singleton that will only be created at app startup
    * This prevents accidental resets of the distance tracked when the Activity's
    * onCreate method is re-fired for various reasons
    */
    Distance()
    {
        this.distanceKm = 0.0f;
    }

    /* This function initializes distance tracking
    * It must be called from within an activity
    */
    public void initTracking(Activity distanceActivity)
    {
        this.distanceActivity = distanceActivity;
        locationManager = (LocationManager) distanceActivity.getSystemService(Context.LOCATION_SERVICE);
    }

    // This function starts GPS tracking
    public void startTracking()
    {
        try
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_CHANGE_MS, MIN_DISTANCE_CHANGE_M, locationListener);

            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        catch (SecurityException e)
        {

        }
    }

    // This function stops GPS tracking
    public void stopTracking()
    {
        try
        {
            locationManager.removeUpdates(locationListener);
        }
        catch (SecurityException e)
        {

        }

        lastLocation = null;
    }

    // This function resets GPS tracking without reinitializing the whole distance object
    public void resetTracking()
    {
        distanceKm = 0.0f;
        stopTracking();
        startTracking();
    }

    // This getter retrieves the current distanceKm traveled formatted as a string
    public String getDistanceString(int units)
    {
        if (units == UNITS_KM)
        {
            return String.format("%.2f", distanceKm) + DISTANCE_STRING_UNITS_KM;
        }
        else
        {
            return String.format("%.2f", distanceKm * KM_TO_MI) + DISTANCE_STRING_UNITS_MI;
        }
    }

    // This getter retrieves the current distanceKm traveled formatted as a string
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
