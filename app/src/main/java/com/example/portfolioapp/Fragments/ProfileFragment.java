package com.example.portfolioapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.io.File;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.example.portfolioapp.MainActivity;
import com.example.portfolioapp.R;
import com.example.portfolioapp.Signupactivity;
import com.example.portfolioapp.Startactivity;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.Compressor;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    private View MultiSpinner;
    private Context mContext;
    private FirebaseAuth f;
    private FirebaseFirestore fstore;
    private EditText fname;
    private EditText femail;
    private Bitmap compressor;
    private String currenttime,currentdate,random;
    private NumberPicker fyear;
    private EditText fbio;
    private Button button;
    private Context mcontext;
    private Button button2;
    private ProgressBar pg;
    private ProgressDialog progressDialog;
    ImageView myimage;
    private Uri ImageUri;
    String downloadurl;
    private StorageReference storageReference;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;


    @Nullable
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext = context;
    }

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
        fyear = view.findViewById(R.id.et_year_cp);
        fyear.setMaxValue(2020);
        fyear.setMinValue(1970);
        fbio = view.findViewById(R.id.et_bio_cp);
        button2 = view.findViewById(R.id.btn_cp);
        pg = view.findViewById(R.id.progressbar_cp);
        myimage = view.findViewById(R.id.iv_cp);
        fstore = FirebaseFirestore.getInstance();
        f = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference("Users");



        myimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(mcontext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();

                    }

                } else {
                    pickImageFromGallery();

                }
            }
        });


        fstore.collection("users").document(f.getCurrentUser().getUid().toString()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            fname.setText(documentSnapshot.getString("Full Name"));
                            femail.setText(documentSnapshot.getString("Email"));


                            fyear.setValue(documentSnapshot.getLong("Year").intValue());
                            fbio.setText(documentSnapshot.getString("Bio"));
                            String fd=documentSnapshot.getString("Image");

                            Picasso.get().load(fd).into(myimage);


                            int po = documentSnapshot.getLong("college").intValue();
                            spinner.setSelection(po);

                            fname.setEnabled(false);
                            femail.setEnabled(false);
                            fyear.setEnabled(false);
                            fbio.setEnabled(false);
                            spinner.setEnabled(false);
                            button2.setEnabled(false);
                            myimage.setEnabled(false);
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
                myimage.setEnabled(true);

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pname = fname.getText().toString();

                int year = fyear.getValue();
                String bio = fbio.getText().toString();
                String image=myimage.toString();
                UpdateProfile(pname, bio, year);
                fname.setEnabled(false);
                button2.setEnabled(false);
                fyear.setEnabled(false);
                spinner.setEnabled(false);
                fbio.setEnabled(false);
                SavingPost(pname);
                myimage.setEnabled(false);

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
            String current_id = f.getCurrentUser().getUid();
            int hold = spinner.getSelectedItemPosition();
            fstore.collection("users").document(current_id).update("college", hold);


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void UpdateProfile(String name, String bio, int year) {
        String current_id = f.getCurrentUser().getUid();
        fstore.collection("users").document(current_id).update("Full Name", name);
        fstore.collection("users").document(current_id).update("Bio", bio);

        fstore.collection("users").document(current_id).update("Year", year);
        Calendar date = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");

        currentdate = currentDate.format(date.getTime());


        Calendar time = Calendar.getInstance();
        SimpleDateFormat currentime = new SimpleDateFormat("HH:mm:ss");

        currenttime = currentime.format(date.getTime());

        random = currentdate+" "+currenttime;
        StorageReference imagereference=storageReference.child(ImageUri.getLastPathSegment()+" "+random+".jpg");
        imagereference.putFile(ImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());

                        downloadurl = uriTask.getResult().toString();
                        if(uriTask.isSuccessful())
                        {
                        String id= f.getCurrentUser().getUid();
                        fstore.collection("users").document(id).update("Image",downloadurl);
                        }
                    }});




        Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
    }


    private void SavingPost(String name) {
        String current_id = f.getCurrentUser().getUid().toString();
        CollectionReference dp = fstore.collection("Posts");
        dp.whereEqualTo("Id", current_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> doc = new HashMap<>();
                        doc.put("FullName", name);
                        dp.document(document.getId()).set(doc, SetOptions.merge());
                    }

                } else {
                    Toast.makeText(getActivity(), "Error: "+task.getException().toString() , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void pickImageFromGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(mcontext, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure you want to set this image as your profile picture ?");
            builder.setCancelable(false);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ImageUri = data.getData();
                    myimage.setImageURI(ImageUri);
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();


        }
    }

}
