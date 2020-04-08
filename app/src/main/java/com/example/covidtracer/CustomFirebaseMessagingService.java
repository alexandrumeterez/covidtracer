package com.example.covidtracer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.covidtracer.dbhelpers.FirebaseDatabaseHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.example.covidtracer.Utils.readFromStorage;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "CustomFMService";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final String strUserUID = sharedPreferences.getString(getString(R.string.UID), "None");
        String token = sharedPreferences.getString(getString(R.string.token), "None");
        FirebaseDatabaseHelper.getInstance().updateDeviceToken(strUserUID, token, new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void Success() {
                Log.d(TAG, "Updated");
            }

            @Override
            public void Fail() {
                Log.d(TAG, "Failed");
            }
        });
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Log.d(TAG, "id: " + remoteMessage.getData().get("id"));

            String metUserID = remoteMessage.getData().get("id");

            Context context = getApplicationContext();
            String message = String.format(getApplicationContext().getResources().getString(R.string.push_notification),
                    remoteMessage.getData().get("oldStatus").toLowerCase(),
                    remoteMessage.getData().get("newStatus").toLowerCase());

            sendNotification(message);

            String filePath = getApplicationContext().getFilesDir().toString() + "/meetings" + "/" + metUserID;

            String date = readFromStorage(filePath, "date.txt");
            Integer duration = readFromStorage(filePath, "duration.txt") != null ? Integer.parseInt(readFromStorage(filePath, "duration.txt")) : -1;

            Float latitude = readFromStorage(filePath, "latitude.txt") != null ? Float.parseFloat(readFromStorage(filePath, "latitude.txt")) : -1;
            Float longitude = readFromStorage(filePath, "longitude.txt") != null ? Float.parseFloat(readFromStorage(filePath, "longitude.txt")) : -1;

            Intent dialogIntent = new Intent(this, MeetingActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            dialogIntent.putExtra("DATE", date);
            dialogIntent.putExtra("MET_USER_ID", metUserID);
            dialogIntent.putExtra("DURATION", duration);
            dialogIntent.putExtra("LATITUDE", latitude);
            dialogIntent.putExtra("LONGITUDE", longitude);
            dialogIntent.putExtra("HEALTH_STATUS", remoteMessage.getData().get("newStatus"));

            startActivity(dialogIntent);
        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, LoggedInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Careful")
                        .setContentText("Change status")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
