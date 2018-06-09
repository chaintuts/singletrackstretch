/* This file contains code that initializes the SingletrackStretch GUI
* This file contain the main entry point for the program
*
* Author: Josh McIntyre
*/
package com.jmcintyre.singletrackstretch;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

/* This class defines the main activity for the program
* It manages the GUI for distance calculation
*/
public class SingletrackStretchActivity extends AppCompatActivity
{
    // This block defines constants for the GUI
    private static final int TEXTVIEW_REFRESH_RATE = 1000;
    private static final int GPS_PERMISSION_REQUEST_CODE = 1;

    // This block defines variables for distance tracking
    private Distance distance;
    private Handler handler;
    private TextView distanceTextView;
    private TextView currentLocationTextView;
    private RadioButton kmRadioButton;
    private RadioButton miRadioButton;

    /* This function builds the GUI when the app loads
    * It sets the view to the distance activity
    */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);

        // Initialize the distance tracker
        distance = new Distance(this, getApplicationContext());

        /* Request app permissions right away
        * Nothing in this app functions without GPS permissions so it should be
        * appropriate to ask on startup
        */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (! (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_PERMISSION_REQUEST_CODE );
            }
        }

        // Start the distance textView update loop
        handler = new Handler();
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        currentLocationTextView = (TextView) findViewById(R.id.currentLocationTextView);
        kmRadioButton = (RadioButton) findViewById(R.id.kmRadioButton);
        miRadioButton = (RadioButton) findViewById(R.id.miRadioButton);
        final Runnable refreshDistance = new Runnable()
        {
            @Override
            public void run()
            {
                int units;
                if (kmRadioButton.isChecked())
                {
                    units = Distance.UNITS_KM;
                }
                else
                {
                    units = Distance.UNITS_MI;
                }
                distanceTextView.setText(distance.getDistanceString(units));
                currentLocationTextView.setText(distance.getCurrentLocationString());
                handler.postDelayed(this, TEXTVIEW_REFRESH_RATE);
            }
        };
        handler.postDelayed(refreshDistance, TEXTVIEW_REFRESH_RATE);

    }

    // This function starts the distance tracker
    public void startTracking(View view)
    {
        // Start the distance tracking
        distance.startTracking();

        // Inform the user tracking has been started
        Toast toast = Toast.makeText(getApplicationContext(), "Tracking started", Toast.LENGTH_SHORT);
        toast.show();
    }

    /* This function overrides the normal back button functionality so that
    * it prompts the user to exit
    */
    @Override
    public void onBackPressed()
    {
        // Create the exit prompt using the AlertDialog Builder
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle("Exit?");
        dialogBuilder.setMessage("Are you sure you want to exit this tracking session? Your distance data will be lost.");

        // Set the yes button handler
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });

        // Don't do anything if the user says no
        dialogBuilder.setNegativeButton("No", null);

        // Show the dialog since the back button was pressed
        dialogBuilder.show();

    }

    // This function stops the distance tracker
    public void stopTracking(View view)
    {
        // Stop the tracking
        distance.stopTracking();

        // Inform the user tracking has been stopped
        Toast toast = Toast.makeText(getApplicationContext(), "Tracking stopped", Toast.LENGTH_SHORT);
        toast.show();
    }

    // This function resets the distance tracker
    public void resetTracking(View view)
    {
        // Reinitialize the distance tracker
        distance = new Distance(this, getApplicationContext());

        // Inform the user the tracker has been reset
        Toast toast = Toast.makeText(getApplicationContext(), "Tracking reset", Toast.LENGTH_SHORT);
        toast.show();
    }
}
