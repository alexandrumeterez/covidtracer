package com.example.covidtracer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracer.dbhelpers.FirebaseDatabaseHelper;
import com.example.covidtracer.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.UUID;

public class SubmitFragment extends Fragment {
    private static final String TAG = "SubmitFragment";

    private String surname;
    private String familyName;
    private String email;
    private String phone;
    private String status;
    private View view;
    private Button btnSubmit;

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

    public SubmitFragment() {
    }

    public static SubmitFragment newInstance() {
        SubmitFragment fragment = new SubmitFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            surname = bundle.getString("surname");
            familyName = bundle.getString("familyName");
            email = bundle.getString("email");
            phone = bundle.getString("phone");
            status = bundle.getString("status");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_submit, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textSurname = view.findViewById(R.id.textPreviewSurname);
        TextView textFamilyName = view.findViewById(R.id.textPreviewFamilyName);
        TextView textEmail = view.findViewById(R.id.textPreviewEmail);
        TextView textPhone = view.findViewById(R.id.textPreviewPhone);
        TextView textStatus = view.findViewById(R.id.textPreviewStatus);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d(TAG, token);
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.token), token);
                        editor.commit();
                    }
                });

        textSurname.setText(surname, TextView.BufferType.EDITABLE);
        textFamilyName.setText(familyName, TextView.BufferType.EDITABLE);
        textEmail.setText(email, TextView.BufferType.EDITABLE);
        textPhone.setText(phone, TextView.BufferType.EDITABLE);
        textStatus.setText(status, TextView.BufferType.EDITABLE);
        btnSubmit = view.findViewById(R.id.btnFinish);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(familyName, surname, email, phone, status);
                FirebaseDatabaseHelper.getInstance().addUser(id(getContext()), user, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void Success() {
                        Log.d(TAG, "Success");
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(getString(R.string.registered), true);
                        editor.putString(getString(R.string.familyName), familyName);
                        editor.putString(getString(R.string.surname), surname);
                        editor.putString(getString(R.string.email), email);
                        editor.putString(getString(R.string.phone), phone);
                        editor.putString(getString(R.string.status), status);
                        editor.putString(getString(R.string.UID), id(getContext()));
                        editor.commit();


                        Intent intent = new Intent(getContext(), LoggedInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void Fail() {
                        Log.d(TAG, "Failed");
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


}
