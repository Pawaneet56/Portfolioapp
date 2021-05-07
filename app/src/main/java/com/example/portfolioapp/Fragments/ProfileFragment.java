package com.example.portfolioapp.Fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import java.io.ByteArrayOutputStream;
import java.lang.String;
import java.net.URI;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.portfolioapp.MainActivity;
import com.example.portfolioapp.R;
import com.example.portfolioapp.Startactivity;
import com.firebase.ui.auth.ui.accountlink.WelcomeBackPasswordPrompt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
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

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    private Button Edit;
    private EditText Name;
    private NumberPicker Year;
    private Spinner spinner;
    private EditText Bio;
    private FirebaseAuth fauth;
    private FirebaseFirestore fstore;
    private ImageView myimage;
    private TextView Year1,CollegeName;
    private Uri filepath;
    private Context mcontext;
    private TextView edittext;
    private TextView Name1,Email1,Bio1;
    String name,bio,image,email;
    int year;
    String college;
     String downloadurl;
     String urli;
     private TextView headname;
     private ImageView headimage;
    private static final int Gallery_pick = 1000;
    private static final int Permission_code = 1001;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Button save,post;
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
        getActivity().setTitle("Profile");



        Edit=v.findViewById(R.id.editbutton);
        edittext=v.findViewById(R.id.edittext);
        Name=v.findViewById(R.id.name);
        Bio1=v.findViewById(R.id.bio1);
        Year=v.findViewById(R.id.year);
        Name1=v.findViewById(R.id.name1);
        Email1=v.findViewById(R.id.email1);
        Year.setMinValue(1971);
        Year1=v.findViewById(R.id.year1);
        CollegeName=v.findViewById(R.id.college);
        Year.setMaxValue(2020);
        post=v.findViewById(R.id.posts);
        spinner=v.findViewById(R.id.spinner1);
        Bio=v.findViewById(R.id.bio);
        myimage=v.findViewById(R.id.image);
        save=v.findViewById(R.id.savebutton);
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();


        storageReference = storage.getReference();
        fstore.collection("users").document(fauth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                urli=documentSnapshot.getString("Image");
            }
        });
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
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle2 = new Bundle();
                bundle2.putString("post","true");
                if(Profile.equals("true")){
                bundle2.putString("uid",uid);}
                else{
                    bundle2.putString("uid",id);
                }

                HomeFragment fragment = new HomeFragment();
                fragment.setArguments(bundle2);
                FragmentManager fragmentManager = ((MainActivity)mcontext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,fragment);
                fragmentTransaction.addToBackStack(null).commit();
            }
        });

save.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String name=Name.getText().toString();
        int year=Year.getValue();
        String collegeName=spinner.getSelectedItem().toString();
        int college=spinner.getSelectedItemPosition();
        String bio=Bio.getText().toString();
        updateuser(name,year,college,collegeName,bio);
        uploadImage();
        Name1.setText(name);
        CollegeName.setText(collegeName);
        Bio1.setText(bio);
        Year1.setText(""+year);
        post.setVisibility(View.VISIBLE);

    }
});
Edit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

                if(urli.equals("noImage")){
                    myimage.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Toast.makeText(getActivity(),"Profile Picture",Toast.LENGTH_SHORT).show();

                            return true;
                        }
                    });
                                   }
                else{
                    myimage.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Do you want to remove profile picture?");
                            builder.setCancelable(false);
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myimage.setImageResource(R.drawable.avatar);
                                    StorageReference picref = FirebaseStorage.getInstance().getReferenceFromUrl(urli);
                                    picref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(mcontext, "Image is removed succesfully", Toast.LENGTH_SHORT).show();
                                            urli = "noImage";
                                            filepath=null;
                                            String name=Name.getText().toString();
                                            int year=Year.getValue();
                                            String collegeName=spinner.getSelectedItem().toString();
                                            int college=spinner.getSelectedItemPosition();
                                            String bio=Bio.getText().toString();
                                            updateuser(name,year,college,collegeName,bio);
                                            fstore.collection("users").document(fauth.getCurrentUser().getUid()).update("Image",urli);

                                        }
                                    });

                                }
                            });
                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                            return true;
                        }
                    });
                }
Edit.setVisibility(View.GONE);
                edittext.setVisibility(View.GONE);
        Name1.setVisibility(View.GONE);
        Name.setVisibility(View.VISIBLE);
        Year.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        Year1.setVisibility(View.GONE);
        Bio.setVisibility(View.VISIBLE);
        post.setVisibility(View.GONE);
        Bio1.setVisibility(View.GONE);
        myimage.setEnabled(true);
        save.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        CollegeName.setVisibility(View.GONE);
    }
});
        myimage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to change profile picture?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();


    }
});

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.colleges,android.R.layout.simple_dropdown_item_1line);


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
    private void updateuser(String name,int year,int college,String collegeName,String bio){
        String id = fauth.getCurrentUser().getUid();
        fstore.collection("Posts").whereEqualTo("Id",id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<Object, String> doc = new HashMap<>();
                        doc.put("FullName",name);
                        fstore.collection("Posts").document(document.getId()).set(doc, SetOptions.merge());
                    }
                }
            }
        });
        SharedPreferences shared = getActivity().getSharedPreferences("MyPREFERENCES",MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("Key","Key");
        editor.putString("Name",name);
        editor.commit();

fstore.collection("users").document(id).update("Full Name",name);
        fstore.collection("users").document(id).update("Bio",bio);
        fstore.collection("users").document(id).update("college",college);
        fstore.collection("users").document(id).update("collegeName",collegeName);
        fstore.collection("users").document(id).update("Year",year);
        Year1.setVisibility(View.VISIBLE);
        Name.setVisibility(View.GONE);
        Name1.setVisibility(View.VISIBLE);
        CollegeName.setVisibility(View.VISIBLE);
        Year.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        Bio.setVisibility(View.GONE);
        myimage.setEnabled(false);
        save.setVisibility(View.GONE);
        Bio1.setVisibility(View.VISIBLE);
        Edit.setVisibility(View.VISIBLE);
        edittext.setVisibility(View.VISIBLE);

          }

    private void showUser(String id ){

        fstore.collection("users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    name = documentSnapshot.getString("Full Name");
                    email = documentSnapshot.getString("Email");
                    year=documentSnapshot.getLong("Year").intValue();
                    college = documentSnapshot.getString("collegeName");
                    int college1=documentSnapshot.getLong("college").intValue();
                    bio=documentSnapshot.getString("Bio");
                    spinner.setSelection(college1);
                    Email1.setText(email);
                    Year1.setText(""+year);
                    Name1.setText(name);
                    Year.setValue(year);
                    Name.setText(name);
                    Bio1.setText(bio);
                    CollegeName.setText(college);
                    Bio.setText(bio);
                    Bio.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);
                    Year.setVisibility(View.GONE);
                    Name.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);
                    myimage.setEnabled(false);
                    if ( documentSnapshot.getString("Image").equals("noImage")) {
                        myimage.setImageResource(R.drawable.avatar);
                    } else {
                        //Picasso.get().load(documentSnapshot.getString("Image")).into(myimage);
                        Picasso.get().load(documentSnapshot.getString("Image")).fit().centerCrop(-10).into(myimage);
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
            Picasso.get().load(filepath).fit().centerCrop(-10).into(myimage);

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
            Bitmap bitmap = ((BitmapDrawable)myimage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] data = baos.toByteArray();


            // adding listeners on upload
            // or failure of image
            ref.putBytes(data)
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
                                        urli=downloadurl;



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
        /*Intent i =new Intent(getActivity().getBaseContext(),MainActivity.class);
        i.putExtra("Image",url);*/
        String id=fauth.getCurrentUser().getUid();
        fstore.collection("users").document(id).update("Image",url);
    fstore.collection("Posts").whereEqualTo("Id",id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<Object, String> doc = new HashMap<>();
                    doc.put("UserImage",url);
                    fstore.collection("Posts").document(document.getId()).set(doc, SetOptions.merge());
                }
            }
        }
    });
}

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        return false;
    }
}