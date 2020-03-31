package com.example.covidtracer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.covidtracer.dbhelpers.FirebaseDatabaseHelper;
import com.example.covidtracer.models.Meet;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.firebase.firestore.FieldValue;

public class NearbyTrackingService extends Service {
    private final String TAG = "MessageService";
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private MessageListener messageListener;
    private Message myUserUIDMessage;
    private String myUserUID;
    private Context context = this;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        myUserUID = prefs.getString(getString(R.string.UID), "None");

        myUserUIDMessage = new Message(myUserUID.getBytes());
        messageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                String metUserUID = new String(message.getContent());
                Log.d(TAG, "Found user: " + metUserUID);
                Meet meet = new Meet(FieldValue.serverTimestamp(), FieldValue.serverTimestamp(), "ongoing");
                FirebaseDatabaseHelper.getInstance().addMeeting(myUserUID, metUserUID, meet, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void Success() {
                        Log.d(TAG, "Inserted user");
                        Toast.makeText(context, "Inserted meeting", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void Fail() {
                        Log.d(TAG, "Failed inserting user");
                        Toast.makeText(context, "Failed to insert meeting", Toast.LENGTH_LONG).show();

                    }
                });
            }

            @Override
            public void onLost(Message message) {
                Log.d(TAG, "Closed application");
                String metUserUID = new String(message.getContent());
                Log.d(TAG, "Lost sight of user: " + metUserUID);
                FirebaseDatabaseHelper.getInstance().updateMeetingEnding(myUserUID, metUserUID, FieldValue.serverTimestamp(), new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void Success() {
                        Log.d(TAG, "Updated meeting");
                        Toast.makeText(context, "Updated meeting", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void Fail() {
                        Log.d(TAG, "Failed to update meeting");
                        Toast.makeText(context, "Failed to update meeting", Toast.LENGTH_LONG).show();

                    }
                });
            }
        };

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Starting tracking", Toast.LENGTH_LONG).show();

        Nearby.getMessagesClient(this).publish(myUserUIDMessage);
        Nearby.getMessagesClient(this).subscribe(messageListener);

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, LoggedInActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Inregistrare persoane din apropiere")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Nearby.getMessagesClient(this).unpublish(myUserUIDMessage);
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


}
