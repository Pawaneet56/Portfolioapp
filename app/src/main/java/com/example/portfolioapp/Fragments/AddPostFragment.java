package com.example.portfolioapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.example.portfolioapp.Adaptors.PostAdaptor;
import com.example.portfolioapp.MainActivity;
import com.example.portfolioapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
    private Uri Imageuri;
    private TextView text1;
    private Button remove;

    private MultiSpinnerSearch domainselection;
    private final List<String> itemsindomain=new ArrayList<>();
    private List<String> item;

    private String Post_name,Post_detail;
    private StorageReference imagereference;
    private ProgressDialog loadingbar;

    private static final int Gallery_pick = 1000;
    private static final int Permission_code = 1001;

    private String timestamp,downloadurl;

    private String edittitle,editdetails,editimage;

    private String key = "noedit",editpostid = "noId";

    private RadioGroup paymentmode,typeofproject;
    private RadioButton typeofmode,projecttype;

    private TextInputLayout nameerror,detailerror;
    private Boolean isposttitle=false,ispostdetail=false,ispaymentmode=false,istypeofpost=false,isdomain=false;
    private TextView errorpaymentmode,errortypeofproject,errordomain;

    public AddPostFragment() {
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mcontext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_add_post, container, false);

        postpic = v.findViewById(R.id.fProjectpic);
        posttitle = v.findViewById(R.id.fprojectname);
        postdetail = v.findViewById(R.id.fprojectdetail);
        update = v.findViewById(R.id.post);
        text1 = v.findViewById(R.id.ftext);
        remove = v.findViewById(R.id.remimage);
        paymentmode=v.findViewById(R.id.typeofmode);
        typeofproject=v.findViewById(R.id.Type_of_post);
        domainselection=v.findViewById(R.id.domainselection);
        errorpaymentmode=v.findViewById(R.id.errortypeofpayment);
        errortypeofproject=v.findViewById(R.id.errortypeofproject);
        errordomain=v.findViewById(R.id.errordomain);
        nameerror=v.findViewById(R.id.errorprojectname);
        detailerror=v.findViewById(R.id.errordescription);
        fstore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        imagereference = FirebaseStorage.getInstance().getReference("Posts");
        loadingbar = new ProgressDialog(mcontext);
        timestamp = String.valueOf(System.currentTimeMillis());

        item=new ArrayList<>();




        //if user come from edit for that key values
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            key = bundle.getString("key");
            editpostid = bundle.getString("editPostid");
        }

        if(key.equals("editPost"))
        {
            getActivity().setTitle("Update Project");
            update.setText("Update");
            text1.setText("Update Project");
            loadPostData(editpostid);
        }
        else
        {
            text1.setText("Add Project");
            getActivity().setTitle("Add Project");
            update.setText("Upload");
        }

        //clicking on the imageview
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


        //to remove image
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    postpic.setImageDrawable(null);
                    StorageReference picref = FirebaseStorage.getInstance().getReferenceFromUrl(editimage);
                    picref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(mcontext, "Image is removed succesfully", Toast.LENGTH_SHORT).show();
                            editimage = "noImage";
                            remove.setVisibility(View.GONE);
                        }
                    });
                }catch (Exception e){}
            }
        });

        //clicking on upload button
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Post_name = posttitle.getText().toString();
                 Post_detail = postdetail.getText().toString();
                if(TextUtils.isEmpty(Post_name))
                {
                    nameerror.setError("Enter Post Title");
                    isposttitle=false;
                }
                else
                {
                    nameerror.setErrorEnabled(false);
                    isposttitle=true;
                }


                if(TextUtils.isEmpty(Post_detail))
                {
                    detailerror.setError("Enter Project detail");
                    ispostdetail=false;
                }
                else
                {
                    detailerror.setErrorEnabled(false);
                    ispostdetail=true;
                }

                if(paymentmode.getCheckedRadioButtonId()==-1)
                {
                    errorpaymentmode.setVisibility(View.VISIBLE);
                    ispaymentmode=false;
                }
                else
                {
                    errorpaymentmode.setVisibility(View.GONE);
                    ispaymentmode=true;
                }

                if(typeofproject.getCheckedRadioButtonId()==-1)
                {
                    errortypeofproject.setVisibility(View.VISIBLE);
                    istypeofpost=false;
                }
                else
                {
                    errortypeofproject.setVisibility(View.GONE);
                    istypeofpost=true;
                }

                if(itemsindomain.isEmpty())
                {
                    isdomain=false;
                    errordomain.setVisibility(View.VISIBLE);
                }
                else
                {
                    isdomain=true;
                    errordomain.setVisibility(View.GONE);
                }



                if(key.equals("editPost"))
                {

                    loadingbar.setTitle("Updating Project");
                    loadingbar.setMessage("Please wait, while we update your project");
                    loadingbar.setCancelable(false);
                    loadingbar.show();
                    beginupdate(editpostid);
                }

                else if(Imageuri!=null && ispostdetail && isposttitle && ispaymentmode && istypeofpost && isdomain)
                {

                    loadingbar.setTitle("Adding New Project");
                    loadingbar.setMessage("Please wait, while we upload your project");
                    loadingbar.setCancelable(false);
                    loadingbar.show();
                    storingimage();
                }
                else if(ispostdetail && isposttitle && ispaymentmode && istypeofpost && isdomain)
                {
                    loadingbar.setTitle("Adding New Project");
                    loadingbar.setMessage("Please wait, while we upload your project");
                    loadingbar.setCancelable(false);
                    loadingbar.show();
                    SavingPost("noImage");
                }

            }
        });

        //selecting domain
        final List<KeyPairBoolData> isselected = new ArrayList<>();
        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.Domains));

        if(!key.equals("editPost"))
        {
            for(int i=0;i<list.size();i++)
            {
                KeyPairBoolData k = new KeyPairBoolData();
                k.setId(i+1);
                k.setName(list.get(i));
                k.setSelected(false);
                isselected.add(k);
            }

        }
        else
        {

            for(int i=0;i<list.size();i++)
            {
                KeyPairBoolData k = new KeyPairBoolData();
                k.setId(i+1);
                k.setName(list.get(i));

                if(item.contains(list.get(i)))
                {
                    k.setSelected(true);
                }
                else
                {
                    k.setSelected(false);
                }

                isselected.add(k);
            }

        }

        domainselection.setSearchEnabled(true);
        domainselection.setSearchHint("Domain");
        domainselection.setClearText("Close & Clear");
        domainselection.setEmptyTitle("No data found");

        domainselection.setItems(isselected, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                itemsindomain.clear();
                for(int i=0;i<selectedItems.size();i++)
                {
                    if(selectedItems.get(i).isSelected())
                    {
                        itemsindomain.add(selectedItems.get(i).getName());
                    }
                }
            }
        });

        return v;
    }









//condition for update
    private void beginupdate(String editpostid) {

        if(editimage.equals("noImage"))
        {
            if(Imageuri!=null)
            {
                updatewithnowimage(editpostid);
            }
            else
            {
                UpdatingPost("noImage");
            }
        }
        else if(!editimage.equals("noImage") && Imageuri==null)
        {
            UpdatingPost(editimage);
        }
        else
        {
            if(Imageuri!=null)
            {
                updatewaswithimage(editpostid);
            }
            else
            {
                UpdatingPost("noImage");
            }

        }
    }


    private void updatewithnowimage(String editpostid) {

        Bitmap bitmap = ((BitmapDrawable)postpic.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] data = baos.toByteArray();

        StorageReference filePath = imagereference.child(Imageuri.getLastPathSegment()+"_"+timestamp+".jpeg");
        filePath.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());

                        downloadurl = uriTask.getResult().toString();
                        if(uriTask.isSuccessful())
                        {
                            UpdatingPost(downloadurl);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingbar.dismiss();
                        Toast.makeText(mcontext,"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void updatewaswithimage(String editpostid) {
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(editimage);
        ref.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Bitmap bitmap = ((BitmapDrawable)postpic.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                        byte[] data = baos.toByteArray();

                        StorageReference filePath = imagereference.child(Imageuri.getLastPathSegment()+"_"+timestamp+".jpeg");
                        filePath.putBytes(data)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while(!uriTask.isSuccessful());

                                downloadurl = uriTask.getResult().toString();
                                if(uriTask.isSuccessful())
                                {
                                    UpdatingPost(downloadurl);
                                }
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        loadingbar.dismiss();
                                        Toast.makeText(mcontext,"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingbar.dismiss();
                        Toast.makeText(mcontext,"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void loadPostData(String editpostid) {
        fstore.collection("Posts").document(editpostid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            edittitle = documentSnapshot.getString("ProjectName");
                            editdetails = documentSnapshot.getString("Detail");
                            editimage = documentSnapshot.getString("PostImage");

                            String oldtypeofpayment=documentSnapshot.getString("Paid_Unpaid");
                            if(oldtypeofpayment.equals("Paid"))
                            {
                                paymentmode.check(R.id.paid);
                            }
                            else
                            {
                                paymentmode.check(R.id.unpaid);
                            }

                            String oldtypeofproject=documentSnapshot.getString("Type_of_post");
                            if(oldtypeofproject.equals("Project"))
                            {
                                typeofproject.check(R.id.project);
                            }
                            else if(oldtypeofproject.equals("Part Time Jobs"))
                            {
                                typeofproject.check(R.id.part_time_job);
                            }
                            else
                            {
                                typeofproject.check(R.id.internship);
                            }

                            item = (List<String>) Collections.singletonList(documentSnapshot.get("Domain").toString());



                            posttitle.setText(edittitle);
                            postdetail.setText(editdetails);

                            if(!editimage.equals("noImage"))
                            {
                                try{
                                    Picasso.get().load(editimage).into(postpic);
                                    remove.setVisibility(View.VISIBLE);
                                }catch(Exception e){}
                            }
                        }
                    }
                });
    }


    private void storingimage() {
            Bitmap bitmap = ((BitmapDrawable)postpic.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] data = baos.toByteArray();

            StorageReference filePath = imagereference.child(Imageuri.getLastPathSegment()+"_"+timestamp+".jpeg");


            filePath.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());

                            downloadurl = uriTask.getResult().toString();
                            if(uriTask.isSuccessful())
                            {
                                SavingPost(downloadurl);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingbar.dismiss();
                            Toast.makeText(mcontext,"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


    }



    private void UpdatingPost(String url) {

        int radioid1 = typeofproject.getCheckedRadioButtonId();
        int radioid2 = paymentmode.getCheckedRadioButtonId();

        typeofmode = getView().findViewById(radioid2);
        projecttype = getView().findViewById(radioid1);

        String current_id = fauth.getCurrentUser().getUid();
        fstore.collection("users").document(current_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String usname = documentSnapshot.getString("Full Name");
                        String upic = documentSnapshot.getString("Image");

                        if(url.equals("noImage"))
                        {
                            fstore.collection("Posts").document(editpostid).update(
                                    "Detail",Post_detail,
                                    "ProjectName",Post_name,
                                    "PostImage","noImage",
                                    "pid",editpostid,
                                    "pTime",timestamp,
                                    "Paid_Unpaid",typeofmode.getText().toString(),
                                    "Type_of_post",projecttype.getText().toString(),
                                    "Domain",itemsindomain

                            )
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                                                    new HomeFragment()).commit();
                                            Toast.makeText(mcontext,"Project Updated Successfully",Toast.LENGTH_SHORT).show();
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
                        else
                        {
                            fstore.collection("Posts").document(editpostid).update(
                                    "Detail",Post_detail,
                                    "ProjectName",Post_name,
                                    "PostImage",url,
                                    "pid",editpostid,
                                    "pTime",timestamp,
                                    "Paid_Unpaid",typeofmode.getText().toString(),
                                    "Type_of_post",projecttype.getText().toString(),
                                    "Domain",itemsindomain
                            )
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                                                    new HomeFragment()).commit();
                                            Toast.makeText(mcontext,"Project Updated Successfully",Toast.LENGTH_SHORT).show();
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
                    }
                });

    }

    private void SavingPost(String url) {
        String current_id = fauth.getCurrentUser().getUid();

        int radioid1 = typeofproject.getCheckedRadioButtonId();
        int radioid2 = paymentmode.getCheckedRadioButtonId();

        typeofmode = getView().findViewById(radioid2);
        projecttype = getView().findViewById(radioid1);


        fstore.collection("users").document(current_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String usname = documentSnapshot.getString("Full Name");
                        String upic = documentSnapshot.getString("Image");
                        HashMap<String,Object> doc = new HashMap<>();

                        doc.put("FullName",usname);
                        doc.put("Id",current_id);
                        doc.put("Detail",Post_detail);
                        doc.put("ProjectName",Post_name);
                        if(url.equals("noImage"))
                        {doc.put("PostImage","noImage");}
                        else
                        {doc.put("PostImage",downloadurl);}
                        doc.put("UserImage",upic);
                        doc.put("pid",current_id + timestamp);
                        doc.put("pTime",timestamp);
                        doc.put("pLike",0);
                        doc.put("Paid_Unpaid",typeofmode.getText().toString());
                        String[] value = {"noId"};
                        doc.put("Likes", Arrays.asList(value));
                        doc.put("Type_of_post",projecttype.getText().toString());
                        doc.put("Domain",itemsindomain);

                        fstore.collection("Posts").document(current_id + timestamp).set(doc)
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