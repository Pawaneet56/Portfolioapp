package com.example.portfolioapp.Fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.lang.String;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfolioapp.Adaptors.experienceadaptor;
import com.example.portfolioapp.MainActivity;
import com.example.portfolioapp.R;
import com.example.portfolioapp.Startactivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    private ImageView Edit;
    private TextView Posts,Skills,ExtraCurricular;
    private EditText Experience,extracurricular;
    private ImageView skills1,experience1,extracurricular1;
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
    private TextView edittext,postbyme;
    private TextView Name1,Email1,Bio1,profile_title;
    private LinearLayout mLayout;
    experienceadaptor experienceadaptor;
    ArrayList<String>data=new ArrayList<>();
    ArrayList <String> exp=new ArrayList<>();
    String completestring;
     String downloadurl;
     String urli,todelete;

     RecyclerView rec;
    private static final int Gallery_pick = 1000;
    private static final int Permission_code = 1001;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Button save;
    private ImageView post;
    String Profile="myProfile",uid;
    final String[] listItems = new String[]{"C", "C++", "JAVA", "PYTHON","Ruby"};
    final boolean[] checkedItems = new boolean[listItems.length];

    // copy the items from the main list to the selected item list
    // for the preview if the item is checked then only the item
    // should be displayed for the user
    final List<String> selectedItems = Arrays.asList(listItems);

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
        requireActivity().setTitle("Profile");

        Posts=v.findViewById(R.id.viewpostbyme);
        Edit=v.findViewById(R.id.editbutton);
        edittext=v.findViewById(R.id.edittext);
        postbyme=v.findViewById(R.id.viewpostbyme);
        Name=v.findViewById(R.id.name);
        Bio1=v.findViewById(R.id.bio1);
        Year=v.findViewById(R.id.year);
        Name1=v.findViewById(R.id.name1);
        Email1=v.findViewById(R.id.email1);
        Year.setMinValue(1971);
        rec=v.findViewById(R.id.recview);
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
        mLayout=v.findViewById(R.id.linearlayout);
        Skills=v.findViewById(R.id.skills1);
        Experience=v.findViewById(R.id.experience1);
        ExtraCurricular=v.findViewById(R.id.extraCurricular1);
        skills1=v.findViewById(R.id.editskills);
        experience1=v.findViewById(R.id.editexperience);
        extracurricular1=v.findViewById(R.id.editExtraCurriculars);
        extracurricular=v.findViewById(R.id.extraCurricular);
        profile_title=v.findViewById(R.id.profile_title);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        rec.setLayoutManager(linearLayoutManager);
        rec.setHasFixedSize(true);

        skills1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initially set the null for the text preview
                Skills.setText(null);

                // initialise the alert dialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // set the title for the alert dialog
                builder.setTitle("Choose Items");

                // set the icon for the alert dialog

                // now this is the function which sets the alert dialog for multiple item selection ready
                builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked;
                        String currentItem = selectedItems.get(which);
                    }
                });

                // alert dialog shouldn't be cancellable
                builder.setCancelable(false);

                // handle the positive button of the dialog
                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        completestring="";
                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i] ) {
                                completestring+=Skills.getText()+selectedItems.get(i)+",";
                                //Skills.setText(Skills.getText() + selectedItems.get(i) + ", ");
                            }

                        }
                        if(completestring.isEmpty()){
                            Skills.setText(" ");
                            fstore.collection("users").document(fauth.getCurrentUser().getUid()).update("skills",completestring);
                        }
                        else{
                            completestring=completestring.substring(0,completestring.length()-1);

                            Skills.setText(completestring);}
                        fstore.collection("users").document(fauth.getCurrentUser().getUid()).update("skills",completestring);
                    }
                });

                // handle the negative button of the alert dialog
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                // handle the neutral button of the dialog to clear
                // the selected items boolean checkedItem
                builder.setNeutralButton("CLEAR ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                        }
                    }
                });

                // create the builder
                builder.create();

                // create the alert dialog with the
                // alert dialog builder instance
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });
        experienceadaptor=new experienceadaptor(data,getContext());


//experience1.setOnClickListener(onClick());
experience1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
     if(!Experience.getText().toString().isEmpty()){


         fstore.collection("users").document(fauth.getCurrentUser().getUid()).update("experience", FieldValue.arrayUnion(Experience.getText().toString()));



     }
    }
});
extracurricular1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ExtraCurricular.setText(extracurricular.getText().toString());
        fstore.collection("users").document(fauth.getCurrentUser().getUid()).update("ExtraCurricular",extracurricular.getText().toString());
    }
});
        storageReference = storage.getReference();
        fstore.collection("users").document(fauth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                urli=documentSnapshot.getString("Image");
            }
        });
        String id=fauth.getCurrentUser().getUid();
        Bundle bundle=getArguments();
        if(bundle!=null){
        Profile=bundle.getString("TheirProfile");
        uid=bundle.getString("uid");}
        if(Profile.equals("true")){

            showUser(uid);
            if(!uid.equals(id)){
            Edit.setVisibility(View.GONE);
            Posts.setText("Posts by them");
            profile_title.setText("Profile");
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
        Posts.setVisibility(View.VISIBLE);
        postbyme.setVisibility(View.VISIBLE);
        Experience.setVisibility(View.GONE);
        experience1.setVisibility(View.GONE);
        extracurricular.setVisibility(View.GONE);
        skills1.setVisibility(View.GONE);
        extracurricular1.setVisibility(View.GONE);
        ExtraCurricular.setVisibility(View.VISIBLE);
        myimage.setEnabled(false);
        }
    });

    Edit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
    post.setVisibility(View.GONE);
    postbyme.setVisibility(View.GONE);
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
        extracurricular.setVisibility(View.VISIBLE);
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
        postbyme.setVisibility(View.GONE);
        Experience.setVisibility(View.VISIBLE);
        experience1.setVisibility(View.VISIBLE);
        skills1.setVisibility(View.VISIBLE);
        extracurricular1.setVisibility(View.VISIBLE);
        ExtraCurricular.setVisibility(View.GONE);

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
                if(!urli.equals("noImage")){
                    StorageReference picref = FirebaseStorage.getInstance().getReferenceFromUrl(urli);
                    picref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(),"thanks ",Toast.LENGTH_SHORT).show();
                        }
                    });
                }


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
        postbyme.setVisibility(View.VISIBLE);
        extracurricular1.setVisibility(View.GONE);
        Experience.setVisibility(View.GONE);
        skills1.setVisibility(View.GONE);
        experience1.setVisibility(View.GONE);
        extracurricular.setVisibility(View.GONE);
        ExtraCurricular.setVisibility(View.VISIBLE);

          }

    private void showUser(String id ){
fstore.collection("users").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
    @Override
    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
        if(error!=null)
            return;
        if(value.exists()){
    Email1.setText(value.getString("Email"));
    Name1.setText(value.getString("Full Name"));
    Name.setText(value.getString("Full Name"));
spinner.setSelection(value.getLong("college").intValue());
    Year1.setText(""+value.getLong("Year").intValue());
    CollegeName.setText(value.getString("collegeName"));
    Bio.setText(value.getString("Bio"));
    Bio1.setText(value.getString("Bio"));
    Year.setValue(value.getLong("Year").intValue());
    if(value.getString("Image").equals("noImage")){
        todelete=value.getString("Image");
        myimage.setImageResource(R.drawable.avatar);
    }
    else{
        Picasso.get().load(value.getString("Image")).fit().centerCrop(-10).into(myimage);
    }
}
    }
});
        fstore.collection("users").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot documentSnapshot, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                data.clear();
                assert documentSnapshot != null;
                if(documentSnapshot.exists()){
                    exp=(ArrayList<String>)documentSnapshot.get("experience");
                    Skills.setText(documentSnapshot.getString("skills"));
                    extracurricular.setText(documentSnapshot.getString("ExtraCurricular"));
                    ExtraCurricular.setText(documentSnapshot.getString("ExtraCurricular"));
                    for(String d:exp){
                        String obj=d.toString();
                        data.add(obj);
                    }
                    rec.setAdapter(experienceadaptor);
                    experienceadaptor.notifyDataSetChanged();
                }
            }
        });


        Name.setVisibility(View.GONE);
        Year.setVisibility(View.GONE);
        Bio.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        myimage.setEnabled(false);
        Experience.setVisibility(View.GONE);
        extracurricular.setVisibility(View.GONE);
        experience1.setVisibility(View.GONE);
        extracurricular1.setVisibility(View.GONE);
        skills1.setVisibility(View.GONE);
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
    private View.OnClickListener onClick() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLayout.addView(createNewTextView(Experience.getText().toString()));
            }
        };
    }

    private TextView createNewTextView(String text) {
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(getContext());
        textView.setLayoutParams(lparams);
        exp.add(text);
        fstore.collection("users").document(fauth.getCurrentUser().getUid()).update("experience",exp);
        textView.setText( text);
        return textView;
    }
}