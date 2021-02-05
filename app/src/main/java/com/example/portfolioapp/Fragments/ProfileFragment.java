package com.example.portfolioapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Spinner;
import android.widget.Toast;
import java.lang.String;
import java.net.URI;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.portfolioapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.lang.String;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Button Edit;
    private EditText Name;
    private EditText Email;
    private NumberPicker Year;
    private Spinner spinner;
    private EditText Bio;
    private FirebaseAuth fauth;
    private FirebaseFirestore fstore;
    private ImageView myimage;
    private Uri filepath;
    private Context mcontext;
     String downloadurl;
     String urli;
    private static final int Gallery_pick = 1000;
    private static final int Permission_code = 1001;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Button save;

    int pos;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext = context;
    }
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile,container,false);
Edit=v.findViewById(R.id.editbutton);
Name=v.findViewById(R.id.name);
Email=v.findViewById(R.id.email);
Year=v.findViewById(R.id.year);
Year.setMinValue(1971);
Year.setMaxValue(2020);
spinner=v.findViewById(R.id.spinner1);
Bio=v.findViewById(R.id.bio);
myimage=v.findViewById(R.id.image);
save=v.findViewById(R.id.savebutton);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

save.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String name=Name.getText().toString();
        int year=Year.getValue();
String email=Email.getText().toString();
        int college=pos;
        String bio=Bio.getText().toString();
        updateuser(email,name,year,college,bio);
        uploadImage();
        Email.setEnabled(false);
        Name.setEnabled(false);
        Year.setEnabled(false);
        spinner.setEnabled(false);
        Bio.setEnabled(false);
        myimage.setEnabled(false);
        save.setEnabled(false);
    }
});
Edit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Name.setEnabled(true);
        Year.setEnabled(true);
        spinner.setEnabled(true);
        Bio.setEnabled(true);
        myimage.setEnabled(true);
        save.setEnabled(true);
    }
});
        myimage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(mcontext, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions,Permission_code);
            }else{
                pickImageFromGallery();
            }

        }else{
            pickImageFromGallery();
        }

    }
});
fauth=FirebaseAuth.getInstance();
fstore=FirebaseFirestore.getInstance();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.colleges,android.R.layout.simple_spinner_item);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

fstore.collection("users").document(fauth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
    @Override
    public void onSuccess(DocumentSnapshot documentSnapshot) {
        Name.setText(documentSnapshot.getString("Full Name"));
        Email.setText(documentSnapshot.getString("Email"));
        Year.setValue(documentSnapshot.getLong("Year").intValue());
        int fd=documentSnapshot.getLong("college").intValue();
        spinner.setSelection(fd);
        urli=documentSnapshot.getString("Image");
        Bio.setText(documentSnapshot.getString("Bio"));
        Email.setEnabled(false);
        Name.setEnabled(false);
        Year.setEnabled(false);
        save.setEnabled(false);
        spinner.setEnabled(false);
        Bio.setEnabled(false);
        myimage.setEnabled(false);
        if(documentSnapshot.getString("Image").equals("noImage")){
           myimage.setImageResource(R.drawable.avatar);
        }
        else{
Picasso.get().load(documentSnapshot.getString("Image")).into(myimage);
        }

    }
});




    return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        pos=position;
}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void updateuser(String email,String name,int year,int college,String bio){
        String id = fauth.getCurrentUser().getUid();


        Map<String,Object> doc = new HashMap<>();
        doc.put("Full Name",name);
        doc.put("Year",year);
        doc.put("Bio",bio);
        doc.put("college",college);
        doc.put("Image",urli);
        doc.put("Email",email);
        doc.put("Id",id);

        fstore.collection("users").document(id).set(doc).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(),"Updated",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getActivity(),"Sorry",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pickImageFromGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery,Gallery_pick);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Permission_code:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    pickImageFromGallery();
                }
                else
                {
                    Toast.makeText(mcontext,"Permission denied",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode== Activity.RESULT_OK && requestCode == Gallery_pick && data!=null)
        {
            filepath=data.getData();
            myimage.setImageURI(filepath);

        }
    }
    private void uploadImage()
    {
        if (filepath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filepath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while(!uriTask.isSuccessful());

                                    downloadurl = uriTask.getResult().toString();
                                    if(uriTask.isSuccessful())
                                    {
                                        Savingimage(downloadurl);
                                    }
                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(getActivity(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getActivity(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }
private void Savingimage(String url){
        String id=fauth.getCurrentUser().getUid();
        fstore.collection("users").document(id).update("Image",url);
}

}