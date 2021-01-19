package com.example.portfolioapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.text.BreakIterator;
import java.util.ArrayList;
import com.example.portfolioapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Collections;
import java.util.List;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    private View MultiSpinner;
    private Context mContext;
    private FirebaseAuth f;
    private FirebaseFirestore fstore;
    private EditText fname;
    private EditText femail;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Profile Fragment");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        spinner = view.findViewById(R.id.spinner1);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.colleges,android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        fname=view.findViewById(R.id.et_name_cp);
        femail=view.findViewById(R.id.et_Email_cp);
        fstore=FirebaseFirestore.getInstance();
        f=FirebaseAuth.getInstance();
        fstore.collection("users").document(f.getCurrentUser().getUid().toString()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            fname.setText(documentSnapshot.getString("Full Name"));
                            femail.setText(documentSnapshot.getString("Email"));
                            fname.setEnabled(false);
                            femail.setEnabled(false);
                        }
                    }
                });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0){
            Toast.makeText(getContext(),"Please Select your College",Toast.LENGTH_SHORT).show();

        }else{
            String sNumber = parent.getItemAtPosition(position).toString();


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
