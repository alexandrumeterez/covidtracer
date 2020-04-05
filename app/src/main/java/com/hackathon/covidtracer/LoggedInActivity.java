package com.hackathon.covidtracer;

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

import com.hackathon.covidtracer.dbhelpers.FirebaseDatabaseHelper;

import org.w3c.dom.Text;

public class LoggedInActivity extends AppCompatActivity {
    private String TAG = "LoggedInActivity";
    private final String[] statuses = {"Healthy", "Diagnosed with COVID-19"};
    private final String[] statusDescription = {"I am healthy", "I have been diagnosed with COVID-19"};
    private NumberPicker picker;
    private Button button;
    private ProgressBar progressBar;
    private TextView textTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        Utils.checkPermission(this);

        picker = findViewById(R.id.pickerHealthStatus);
        button = findViewById(R.id.btnUpdateHealthStatus);
        progressBar = findViewById(R.id.progressBar);
        textTracking = findViewById(R.id.textTracking);
        picker.setMinValue(0);
        picker.setMaxValue(statuses.length - 1);
        picker.setDisplayedValues(statusDescription);

        final SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String strUserUID = sharedPreferences.getString(getString(R.string.UID), "None");
        String token = sharedPreferences.getString(getString(R.string.token), "None");
        String currentStatus = sharedPreferences.getString(getString(R.string.status), "None");
        if (currentStatus.equals(statuses[picker.getValue()])) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
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

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                textTracking.setVisibility(View.GONE);
                FirebaseDatabaseHelper.getInstance().updateUserStatus(strUserUID, statuses[picker.getValue()], new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void Success() {
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
                        Toast.makeText(LoggedInActivity.this, "Failed to update status", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        textTracking.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        startService(new Intent(this, NearbyTrackingService.class));
        startService(new Intent(this, CustomFirebaseMessagingService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, NearbyTrackingService.class));
        stopService(new Intent(this, CustomFirebaseMessagingService.class));
    }

    // Function to initiate after permissions are given by user
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {

        if (requestCode == Utils.MULTIPLE_PERMISSIONS) {
            if (grantResults.length > 0) {
                Log.d(TAG, "3");

                boolean internetPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                Log.d(TAG, String.valueOf(internetPermission));
                boolean accesFineLocationPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                Log.d(TAG, String.valueOf(accesFineLocationPermission));
                boolean bluetoothPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
            } else {
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
