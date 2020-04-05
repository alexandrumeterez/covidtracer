package com.hackathon.covidtracer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MeetingActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView meetingDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        String metUserID = getIntent().getStringExtra("MET_USER_ID");
        int duration = getIntent().getIntExtra("DURATION", -1);
        float latitude = getIntent().getFloatExtra("LATITUDE", -1);
        float longitude = getIntent().getFloatExtra("LONGITUDE", -1);

        meetingDuration = findViewById(R.id.meetingDuration);
        meetingDuration.setText(String.format(getApplicationContext().getResources().getString(R.string.meeting_duration),
               String.valueOf(duration)));


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}