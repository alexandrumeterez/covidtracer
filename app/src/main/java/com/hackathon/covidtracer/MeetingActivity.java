package com.hackathon.covidtracer;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MeetingActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView meetingDate;
    private TextView meetingDuration;

    private String metUserID;
    private Date date;
    private int duration;
    private LatLng coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        Bundle bundle = getIntent().getExtras();
        String date = getIntent().getStringExtra("DATE");

        metUserID = getIntent().getStringExtra("MET_USER_ID");
        duration = getIntent().getIntExtra("DURATION", -1);

        Float latitude = getIntent().getFloatExtra("LATITUDE", 59.331264f);
        Float longitude = getIntent().getFloatExtra("LONGITUDE", 18.064854f);

        coordinates = new LatLng(latitude, longitude);

        meetingDate = findViewById(R.id.meetingDate);
        meetingDuration = findViewById(R.id.meetingDuration);

        meetingDate.setText(String.format(getApplicationContext().getResources().getString(R.string.meeting_date), date));

        meetingDuration.setText(String.format(getApplicationContext().getResources().getString(R.string.meeting_duration),
               String.valueOf((int)(duration / 1000f)) + " seconds"));


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(16);

        CircleOptions options = new CircleOptions()
                .radius(20)
                .strokeWidth(3)
                .center(coordinates);

        mMap.addMarker(new MarkerOptions().position(coordinates).title("Point of Contact"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        mMap.addCircle(options);
    }

}