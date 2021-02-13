package com.example.portfolioapp.Fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
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
    private TextView edittext;
    String name,bio,image,email;
    int year,college;
     String downloadurl;
     String urli;
    private static final int Gallery_pick = 1000;
    private static final int Permission_code = 1001;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Button save;
    String Profile="myProfile",uid;

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
edittext=v.findViewById(R.id.edittext);
Name=v.findViewById(R.id.name);
Email=v.findViewById(R.id.email);
Year=v.findViewById(R.id.year);
Year.setMinValue(1971);
Year.setMaxValue(2020);
spinner=v.findViewById(R.id.spinner1);
Bio=v.findViewById(R.id.bio);
myimage=v.findViewById(R.id.image);
save=v.findViewById(R.id.savebutton);
fauth=FirebaseAuth.getInstance();
fstore=FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        String id=fauth.getCurrentUser().getUid().toString();
        Bundle bundle=getArguments();
        if(bundle!=null){
        Profile=bundle.getString("TheirProfile");
        uid=bundle.getString("uid");}
        if(Profile.equals("true")){

            showUser(uid);
            if(!uid.equals(id)){
            Edit.setVisibility(View.GONE);
            edittext.setVisibility(View.GONE);
            save.setVisibility(View.GONE);}
            else{
                save.setVisibility(View.GONE);
            }
        }
        else{
        showUser(id);
            save.setVisibility(View.GONE);}

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
        save.setVisibility(View.GONE);
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
        save.setVisibility(View.VISIBLE);
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
        myimage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(),"Profile picture",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.colleges,android.R.layout.simple_spinner_item);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


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

    private void showUser(String id ){

        fstore.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    name = documentSnapshot.getString("Full Name");
                    email = documentSnapshot.getString("Email");
                    year=documentSnapshot.getLong("Year").intValue();
                    college = documentSnapshot.getLong("college").intValue();
                    bio=documentSnapshot.getString("Bio");
                    Year.setValue(year);
                    Name.setText(name);
                    Email.setText(email);
                    spinner.setSelection(college);
                    Bio.setText(bio);
                    urli = documentSnapshot.getString("Image");
                    Email.setEnabled(false);
                    Name.setEnabled(false);
                    Year.setEnabled(false);
                    spinner.setEnabled(false);
                    Bio.setEnabled(false);
                    myimage.setEnabled(false);
                    if (urli.equals("noImage")) {
                        myimage.setImageResource(R.drawable.avatar);
                    } else {
                        Picasso.get().load(urli).into(myimage);
                    }

                }
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