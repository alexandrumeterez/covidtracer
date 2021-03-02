package com.example.covidtracer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracer.dbhelpers.FirebaseDatabaseHelper;

/* Use this activity when the user is logged in */
public class LoggedInActivity extends AppCompatActivity {
    private String TAG = "LoggedInActivity";
    private final String[] statuses = {"Healthy", "Diagnosed with COVID-19"};
    private final String[] statusDescription = {"I am healthy", "I have been diagnosed with COVID-19"};
    private NumberPicker picker;
    private Button button;
    private ProgressBar progressBar;
    private TextView textTracking;

    // Run this method when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        // check if user has permission to access Internet, location and Bluetooth
        Utils.checkPermission(this);

        // picker UI element to select your health status -> "Healthy" or "Diagnosed with COVID-19"
        picker = findViewById(R.id.pickerHealthStatus);

        // button to submit your health status selection
        button = findViewById(R.id.btnUpdateHealthStatus);

        progressBar = findViewById(R.id.progressBar);
        textTracking = findViewById(R.id.textTracking);

        // Initialize picker UI element with statuses
        picker.setMinValue(0);
        picker.setMaxValue(statuses.length - 1);
        picker.setDisplayedValues(statusDescription);

        // get current token and current health status of the user
        final SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String strUserUID = sharedPreferences.getString(getString(R.string.UID), "None");
        String token = sharedPreferences.getString(getString(R.string.token), "None");
        String currentStatus = sharedPreferences.getString(getString(R.string.status), "None");

        // if user's current health status matches newly selected status, disable submit button else enable it
        if (currentStatus.equals(statuses[picker.getValue()])) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }

        // update user's token in Firebase
        FirebaseDatabaseHelper.getInstance().updateDeviceToken(strUserUID, token, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void Success() {
                Log.d(TAG, "Saved token");
            }

            @Override
            public void Fail() {
                Log.d(TAG, "Failed to save token");
            }
        });

        // attach an event listener that runs the below method when user selects a new health status
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // if user's current health status matches newly selected status, disable submit button else enable it
                String oldStatus = statuses[oldVal];
                String newStatus = statuses[newVal];
                String currentStatus = sharedPreferences.getString(getString(R.string.status), "None");
                if (currentStatus.equals(newStatus)) {
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                }
            }
        });

        // run the below method when the user clicks on submit new health status button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display progress bar unless the new health status is updated in firebase
                progressBar.setVisibility(View.VISIBLE);
                textTracking.setVisibility(View.GONE);

                // Update user's new health status in firebase
                FirebaseDatabaseHelper.getInstance().updateUserStatus(strUserUID, statuses[picker.getValue()], new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void Success() {
                        // Run the following operations after the Firebase update event is completed
                        Toast.makeText(LoggedInActivity.this, "Updated status", Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.status), statuses[picker.getValue()]);
                        editor.commit();
                        button.setEnabled(false);
                        progressBar.setVisibility(View.GONE);
                        textTracking.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void Fail() {
                        // If update event fails, remove the progress bar and display failed message
                        Toast.makeText(LoggedInActivity.this, "Failed to update status", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        textTracking.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        // start service that tracks nearby users and firebase messaging service
        startService(new Intent(this, NearbyTrackingService.class));
        startService(new Intent(this, CustomFirebaseMessagingService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, NearbyTrackingService.class));
        stopService(new Intent(this, CustomFirebaseMessagingService.class));
    }

    /* Function to initiate after permissions are given by user.
       This function is called when user accept or decline the permission. 
       Request Code is used to check which permission called this function. 
       This request code is provided when user is prompt for permission. */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {

        if (requestCode == Utils.MULTIPLE_PERMISSIONS) {

            // check if user has replied to permission requests or not
            if (grantResults.length > 0) {
                Log.d(TAG, "3");

                // log the permission results
                boolean internetPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                Log.d(TAG, String.valueOf(internetPermission));
                boolean accesFineLocationPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                Log.d(TAG, String.valueOf(accesFineLocationPermission));
                boolean bluetoothPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
            } else {
                // ask for permisssions again if user has not replied to previous permission requests
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH},
                            Utils.MULTIPLE_PERMISSIONS);
                }
            }
        }
    }
}
