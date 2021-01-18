package com.example.portfolioapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.portfolioapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class AddPostFragment extends Fragment {

    private ImageButton postpic;
private EditText posttitle;
private EditText postdetail;
private Button update;
private FirebaseFirestore fstore;
private Context mcontext;
private FirebaseAuth fauth;
private Uri Imageuri,donwuri;

private String Post_name,Post_detail;
private StorageReference imagereference;
private ProgressDialog loadingbar;

private static final int Gallery_pick = 1000;
private static final int Permission_code = 1001;

private String currentdate,currenttime,random,downloadurl;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Add Project");
        View v = inflater.inflate(R.layout.fragment_add_post, container, false);

        postpic = v.findViewById(R.id.fProjectpic);
        posttitle = v.findViewById(R.id.fprojectname);
        postdetail = v.findViewById(R.id.fprojectdetail);
        update = v.findViewById(R.id.post);
        fstore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        imagereference = FirebaseStorage.getInstance().getReference("Posts");
        loadingbar = new ProgressDialog(mcontext);

        postpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkSelfPermission(mcontext,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
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


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Post_name = posttitle.getText().toString();
                 Post_detail = postdetail.getText().toString();
                if(TextUtils.isEmpty(Post_name))
                {
                    posttitle.setError("Enter Project Title");
                }
                else if(TextUtils.isEmpty(Post_detail))
                {
                    postdetail.setError("Enter Project detail");
                }
                else if(Imageuri==null)
                {
                    Toast.makeText(mcontext,"Please add image to the project",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingbar.setTitle("Add New Project");
                    loadingbar.setMessage("Please wait, while we upload your project");
                    loadingbar.show();
                    loadingbar.setCanceledOnTouchOutside(true);
                    storingimage();
                }
            }
        });

        return v;
    }


    private void storingimage() {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");

        currentdate = currentDate.format(date.getTime());


        Calendar time = Calendar.getInstance();
        SimpleDateFormat currentime = new SimpleDateFormat("HH:mm:ss");

        currenttime = currentime.format(date.getTime());

        random = currentdate+" "+currenttime;



        StorageReference filePath = imagereference.child(Imageuri.getLastPathSegment()+" "+random+".jpg");

        filePath.putFile(Imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());

                        downloadurl = uriTask.getResult().toString();
                        if(uriTask.isSuccessful())
                        {
                            SavingPost();
                        }
                    }
                });

    }




    private void SavingPost() {
        String current_id = fauth.getCurrentUser().getUid();
        fstore.collection("users").document(current_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String usname = documentSnapshot.getString("Full Name");
                        String upic = documentSnapshot.getString("Image");
                        HashMap<String,Object> doc = new HashMap<>();

                        doc.put("FullName",usname);
                        //doc.put("Id",current_id);
                        doc.put("Detail",Post_detail);
                        doc.put("ProjectName",Post_name);
                        doc.put("PostImage",downloadurl);
                        doc.put("UserImage",upic);

                        fstore.collection("Posts").document(current_id + random).set(doc)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                                                new HomeFragment()).commit();
                                        Toast.makeText(mcontext,"Project Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(mcontext,"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                    }
                                });

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
        if(resultCode==Activity.RESULT_OK && requestCode == Gallery_pick && data!=null)
        {
            Imageuri=data.getData();
            postpic.setImageURI(Imageuri);
        }
    }
}