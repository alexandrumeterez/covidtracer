package com.example.covidtracer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class SubmitFragment extends Fragment {
    private String surname;
    private String familyName;
    private String email;
    private String phone;
    private View view;

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
        Log.d(getTag(), "before bundle");
        if (bundle != null) {
            surname = bundle.getString("surname");
            familyName = bundle.getString("familyName");
            email = bundle.getString("email");
            phone = bundle.getString("phone");

            Log.d("SubmitFragment", surname);
            Log.d("SubmitFragment", familyName);
            Log.d("SubmitFragment", email);
            Log.d("SubmitFragment", phone);
        }
        Log.d(getTag(), "after bundle");

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
    }
}
