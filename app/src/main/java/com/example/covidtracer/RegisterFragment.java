//package com.example.covidtracer;
//
//import android.content.DialogInterface;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.google.firebase.FirebaseException;
//import com.google.firebase.FirebaseTooManyRequestsException;
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
//import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthProvider;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link RegisterFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class RegisterFragment extends Fragment {
//    private static final String TAG = "RegisterFragment";
//    private View view;
//    private Button btnNext;
//    private TextView textStatus;
//    private Bundle bundle = new Bundle();
//    private final String[] statuses = {"Sanatos", "Autoizolat", "Diagnosticat"};
//
//    public RegisterFragment() {
//        // Required empty public constructor
//    }
//
//    public static RegisterFragment newInstance() {
//        RegisterFragment fragment = new RegisterFragment();
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        this.view = inflater.inflate(R.layout.fragment_register, container, false);
//
//        return this.view;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        btnNext = getView().findViewById(R.id.btnNext);
//        textStatus = getView().findViewById(R.id.textStatus);
//
//        textStatus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Ce status aveti?");
//                builder.setItems(statuses, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        textStatus.setText(statuses[which]);
//                    }
//                });
//                builder.show();
//            }
//        });
//
//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bundle.putString("status", textStatus.getText().toString());
//                SubmitFragment submitFragment = SubmitFragment.newInstance();
//                submitFragment.setArguments(bundle);
//
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container, submitFragment);
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });
//    }
//}
