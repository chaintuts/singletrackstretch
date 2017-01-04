/* This file contains code that initializes the SingletrackStretch GUI
* This file contain the main entry point for the program
*
* Author: Josh McIntyre
*
*/
package com.jmcintyre.singletrackstretch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

/* This class defines the main activity for the program
* It manages the GUI for distance calculation
*
*/
public class SingletrackStretchActivity extends AppCompatActivity
{
    /* This block defines constants for the GUI
    *
    */
    private static final int TEXTVIEW_REFRESH_RATE = 1000;

    /* This block defines variables for distance tracking
    *
    */
    private Distance distance;
    private Handler handler;
    private TextView distanceTextView;
    private TextView currentLocationTextView;

    /* This function builds the GUI when the app loads
    * It sets the view to the distance activity
    *
    */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        /* Initialize the distance tracker
        *
        */
        distance = new Distance(this, getApplicationContext());

        /* Start the distance textView update loop
        *
        */
        handler = new Handler();
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        currentLocationTextView = (TextView) findViewById(R.id.currentLocationTextView);
        final Runnable refreshDistance = new Runnable()
        {
            @Override
            public void run()
            {
                distanceTextView.setText(distance.getDistanceString());
                currentLocationTextView.setText(distance.getCurrentLocationString());
                handler.postDelayed(this, TEXTVIEW_REFRESH_RATE);
            }
        };
        handler.postDelayed(refreshDistance, TEXTVIEW_REFRESH_RATE);

    }

    /* This function starts the distance tracker
    *
    */
    public void startTracking(View view)
    {
        /* Start the distance tracking
        *
        */
        distance.startTracking();

        /* Inform the user tracking has been started
        *
        */
        Toast toast = Toast.makeText(getApplicationContext(), "Tracking started", Toast.LENGTH_SHORT);
        toast.show();
    }

    /* This function stops the distance tracker
    *
    */
    public void stopTracking(View view)
    {
        /* Stop the tracking
        *
        */
        distance.stopTracking();

        /* Inform the user tracking has been stopped
        *
        */
        Toast toast = Toast.makeText(getApplicationContext(), "Tracking stopped", Toast.LENGTH_SHORT);
        toast.show();
    }

    /* This function resets the distance tracker
    *
    */
    public void resetTracking(View view)
    {
        /* Reinitialize the distance tracker
        *
        */
        distance = new Distance(this, getApplicationContext());

        /* Inform the user the tracker has been reset
        *
        */
        Toast toast = Toast.makeText(getApplicationContext(), "Tracking reset", Toast.LENGTH_SHORT);
        toast.show();
    }


}
