package com.example.covidtracer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class DescriptionActivity extends AppCompatActivity {
    private Button btnRegister;
    private Context context;
    private static final int MULTIPLE_PERMISSIONS = 100;
    private static final String TAG = "DescriptionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
    }


    // function to check permissions
    private void checkPermission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED)) {
            Log.d(TAG, "0");

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.INTERNET) &&
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.BLUETOOTH)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.d(TAG, "1");

                    requestPermissions(
                            new String[]{Manifest.permission
                                    .INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH},
                            MULTIPLE_PERMISSIONS);
                }

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.d(TAG, "2");

                    requestPermissions(
                            new String[]{Manifest.permission
                                    .INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH},
                            MULTIPLE_PERMISSIONS);
                }
            }
        }
    }


    // Function to initiate after permissions are given by user
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {

        if (requestCode == MULTIPLE_PERMISSIONS) {
            if (grantResults.length > 0) {
                Log.d(TAG, "3");

                boolean internetPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                Log.d(TAG, String.valueOf(internetPermission));
                boolean accesFineLocationPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                Log.d(TAG, String.valueOf(accesFineLocationPermission));
                boolean bluetoothPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                if (internetPermission && accesFineLocationPermission && bluetoothPermission) {
                    Log.d(TAG, "Starting intent");
                    Intent intent = new Intent(DescriptionActivity.this, PhoneRegisterActivity.class);
                    startActivity(intent);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH},
                            MULTIPLE_PERMISSIONS);
                }
            }
        }
    }
}
