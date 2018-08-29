package com.jmcintyre.singletrackstretch;

import android.app.Application;

public class SingletrackStretchApplication extends Application
{
    /* Declare an instance of the Distance class that can be passed to the tracking
    * Activity. By initializing the distance tracker here (at the application scope),
    * we can avoid an accidental reset of the tracking state if the Activity's
    * onCreate is re-fired while we're running
    */
    Distance distance;

    /* This function initializes items like the application's distance tracker
    * It will only be called when the application starts (from a cold start)
    */
    @Override
    public void onCreate()
    {
        super.onCreate();

        /* Initialize the distance tracker object
        * This constructor call sets the initial distance to zero
        * This object must then be retrieved by the Activity and the initDistance() method
        * called to fully initialize tracking capability - an Activity is needed to get an
        * instance of a Location Manager
        */
        distance = new Distance();
    }

    /* This method returns a reference to the Application's distance tracker
    * It should be called from within an activity to retrieve the distance tracker
    * and call initDistance()
    */
    public Distance getDistanceTracker()
    {
        return distance;
    }

}
