package com.example.portfolioapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.text.BreakIterator;
import java.util.ArrayList;

import com.example.portfolioapp.MainActivity;
import com.example.portfolioapp.R;
import com.example.portfolioapp.Signupactivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;


import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    private View MultiSpinner;
    private Context mContext;
    private FirebaseAuth f;
    private FirebaseFirestore fstore;
    private EditText fname;
    private EditText femail;
    private NumberPicker fyear;
    private EditText fbio;
    private Button button;
    private Button button2;
    private ProgressBar pg;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Profile Fragment");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        spinner = view.findViewById(R.id.spinner1);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.colleges, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        fname = view.findViewById(R.id.et_name_cp);
        button = view.findViewById(R.id.btn);
        femail = view.findViewById(R.id.et_Email_cp);
        fyear=view.findViewById(R.id.et_year_cp);
        fyear.setMaxValue(2020);
        fyear.setMinValue(1970);
        fbio=view.findViewById(R.id.et_bio_cp);
        button2 = view.findViewById(R.id.btn_cp);
        pg = view.findViewById(R.id.progressbar_cp);
        fstore = FirebaseFirestore.getInstance();
        f = FirebaseAuth.getInstance();
        fstore.collection("users").document(f.getCurrentUser().getUid().toString()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            fname.setText(documentSnapshot.getString("Full Name"));
                            femail.setText(documentSnapshot.getString("Email"));

                            fyear.setValue(documentSnapshot.getLong("Year").intValue());
                            fbio.setText(documentSnapshot.getString("Bio"));

                            fname.setEnabled(false);
                            femail.setEnabled(false);
                            fyear.setEnabled(false);
                            fbio.setEnabled(false);
                            spinner.setEnabled(false);
                            button2.setEnabled(false);
                        }
                    }
                });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname.setEnabled(true);
                button2.setEnabled(true);
                fyear.setEnabled(true);
                fbio.setEnabled(true);
                spinner.setEnabled(true);

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pname = fname.getText().toString();
                String email = femail.getText().toString();
                int year = fyear.getValue();
                String bio = fbio.getText().toString();

                UpdateProfile(pname, bio, year);
                fname.setEnabled(false);
                button2.setEnabled(false);
                fyear.setEnabled(false);
                fbio.setEnabled(false);
                SavingPost(pname);

                pg.setVisibility(View.GONE);
            }
        });

        return view;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            Toast.makeText(getContext(), "Please Select your College", Toast.LENGTH_SHORT).show();

        } else {
            String sNumber = parent.getItemAtPosition(position).toString();



        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void UpdateProfile(String name,String bio,int year) {
        String current_id=f.getCurrentUser().getUid();
        fstore.collection("users").document(current_id).update("Full Name",name);
        fstore.collection("users").document(current_id).update("Bio",bio);

        fstore.collection("users").document(current_id).update("Year",year);
        Toast.makeText(getActivity(),"Updated",Toast.LENGTH_SHORT).show();
    }


    private void SavingPost(String name) {
        String current_id = f.getCurrentUser().getUid().toString();
        CollectionReference dp=fstore.collection("Posts");
        dp.whereEqualTo("Id",current_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> doc = new HashMap<>();
                        doc.put("FullName", name);
                        dp.document(document.getId()).set(doc, SetOptions.merge());
                    }

            }
                else{
                    Toast.makeText(getActivity(),"sorry",Toast.LENGTH_SHORT).show();
                }
        }});
    }
}

