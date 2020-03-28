package com.example.covidtracer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class DescriptionFragment extends Fragment {
    private Button btn;
    View inflatedView = null;
    private boolean isVisibleFragmentRegister = false;
    public DescriptionFragment() {
        // Required empty public constructor
    }

    public static DescriptionFragment newInstance() {
        DescriptionFragment fragment = new DescriptionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflatedView =  inflater.inflate(R.layout.fragment_description, container, false);
        return this.inflatedView;
    }
}
