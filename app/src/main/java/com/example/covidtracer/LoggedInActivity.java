package com.example.covidtracer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracer.dbhelpers.FirebaseDatabaseHelper;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

public class LoggedInActivity extends AppCompatActivity {
    private TextView currentFamilyName;
    private TextView currentSurname;
    private TextView currentStatus;
    private MessageListener mMessageListener;
    private String TAG = "LoggedInActivity";
    private Message mMessage;
    private final String[] statuses = {"Sanatos", "Autoizolat", "Diagnosticat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        currentStatus = findViewById(R.id.textCurrentStatus);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String strUserUID = sharedPreferences.getString(getString(R.string.UID), "None");
        String token = sharedPreferences.getString(getString(R.string.token), "None");
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
        currentStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoggedInActivity.this);
                builder.setTitle("Ce status aveti?");
                builder.setItems(statuses, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!currentStatus.getText().equals(statuses[which])) {
                            currentStatus.setText(statuses[which]);
                            FirebaseDatabaseHelper.getInstance().updateUserStatus(strUserUID, statuses[which], new FirebaseDatabaseHelper.DataStatus() {
                                @Override
                                public void Success() {
                                    Toast.makeText(LoggedInActivity.this, "Updated status", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void Fail() {
                                    Toast.makeText(LoggedInActivity.this, "Failed to update status", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
                builder.show();
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
}
