package com.example.covidtracer;

import android.content.Context;
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

import java.util.UUID;

public class SubmitFragment extends Fragment {
    private static final String TAG = "SubmitFragment";
    private String surname;
    private String familyName;
    private String email;
    private String phone;
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
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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


        textSurname.setText(surname, TextView.BufferType.EDITABLE);
        textFamilyName.setText(familyName, TextView.BufferType.EDITABLE);
        textEmail.setText(email, TextView.BufferType.EDITABLE);
        textPhone.setText(phone, TextView.BufferType.EDITABLE);

        btnSubmit = view.findViewById(R.id.btnFinish);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(familyName, surname, email, phone);
                FirebaseDatabaseHelper.getInstance().addUser(id(getContext()), user, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void InsertSuccess() {
                        Log.d(TAG, "Success");
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void InsertFail() {
                        Log.d(TAG, "Failed");
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


}
