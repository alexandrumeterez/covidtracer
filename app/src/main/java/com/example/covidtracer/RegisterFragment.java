package com.example.covidtracer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    private View view;
    private Button btnNext;
    private EditText editSurname;
    private EditText editFamilyName;
    private EditText editEmail;
    private EditText editPhone;
    private Bundle bundle = new Bundle();

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_register, container, false);

        return this.view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnNext = getView().findViewById(R.id.btnNext);

        editSurname = getView().findViewById(R.id.editSurname);
        editFamilyName = getView().findViewById(R.id.editFamilyName);
        editEmail = getView().findViewById(R.id.editEmail);
        editPhone = getView().findViewById(R.id.editPhone);



        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bundle.putString("surname", editSurname.getText().toString());
                bundle.putString("familyName", editFamilyName.getText().toString());
                bundle.putString("email", editEmail.getText().toString());
                bundle.putString("phone", editPhone.getText().toString());

                SubmitFragment submitFragment = SubmitFragment.newInstance();
                submitFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, submitFragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}
