package com.hackathon.covidtracer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DescriptionActivity extends AppCompatActivity {
    private Button btnRegister;
    private Activity activity;
    private Context context;
    private static final String TAG = "DescriptionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        context = this;
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.checkPermission(activity);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
        context = null;
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
                            Utils.MULTIPLE_PERMISSIONS);
                }
            }
        }
    }
}
