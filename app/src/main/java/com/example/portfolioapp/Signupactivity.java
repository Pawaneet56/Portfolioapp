package com.example.portfolioapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.HashMap;
import java.util.Map;

public class Signupactivity<Updated> extends AppCompatActivity {
    private EditText emailid;
    private EditText password;
    private Button signup;
    private EditText cpassword;
    private FirebaseAuth auth;
    private EditText name;
    private TextView login;
    private ImageView Show;
    private ImageView Hide;
    int score = 0;
    boolean upper = false;
    boolean lower = false;
    boolean digit = false;
    boolean specialChar = false;
    private FirebaseFirestore fstore;
    private FirebaseStorage fstorage;
    private ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupactivity);
        emailid=findViewById(R.id.emailid);
        password=findViewById(R.id.password);
        signup=findViewById(R.id.signup);
        auth=FirebaseAuth.getInstance();
        cpassword=findViewById(R.id.cpassword);
        name=findViewById(R.id.name);
        login=findViewById(R.id.login);
        Show=findViewById(R.id.show);
        Hide=findViewById(R.id.hide);
        fstore=FirebaseFirestore.getInstance();
        fstorage=FirebaseStorage.getInstance();
        pd = new ProgressDialog(this);


        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    Show.setImageResource(R.drawable.hideeye);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.getText().length());
                } else {
                    Show.setImageResource(R.drawable.showeye);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.getText().length());
                }

            }
        });
        Hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    Hide.setImageResource(R.drawable.hideeye);
                    cpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    cpassword.setSelection(cpassword.getText().length());
                } else {
                    Hide.setImageResource(R.drawable.showeye);
                    cpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    cpassword.setSelection(cpassword.getText().length());
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_emailid = emailid.getText().toString();
                String txt_password = password.getText().toString();
                String Name = name.getText().toString();
                String txt_cpassword = cpassword.getText().toString();


                for (int i = 0; i < txt_password.length(); i++) {
                    char c = txt_password.charAt(i);

                    if (!specialChar && !Character.isLetterOrDigit(c)) {
                        score++;
                        specialChar = true;
                    } else {
                        if (!digit && Character.isDigit(c)) {
                            score++;
                            digit = true;
                        } else {
                            if (!upper || !lower) {
                                if (Character.isUpperCase(c)) {
                                    upper = true;
                                } else {
                                    lower = true;
                                }

                                if (upper && lower) {
                                    score++;
                                }
                            }
                        }
                    }
                }

                if(name.getText().toString().isEmpty()){
                    name.setError("Enter Name");
                }
                if (TextUtils.isEmpty(txt_emailid)){
                    emailid.setError("Enter Email Id");
                }
                if(TextUtils.isEmpty(txt_password)) {
                    password.setError("Enter Password");
                }
                if(TextUtils.isEmpty(txt_cpassword)){
                    cpassword.setError("Enter Confirm Password");
                }
                else if (txt_password.length() < 6) {
                    password.setError("Password is short");
                }
                else if (!txt_password.equals(txt_cpassword)) {
                    cpassword.setError("Passwords do not match");
                }
                else if (score <= 1 && txt_password.length() > 6) {
                    password.setError("Password is weak try another,add uppercase or special character");
                }
                else if(txt_password.equals(txt_cpassword)){
                    if (score == 2) {

                        Toast.makeText(getApplicationContext(), "Medium password", Toast.LENGTH_SHORT).show();
                        registeruser(txt_emailid, txt_password, Name);
                    } else if (score == 3) {

                        Toast.makeText(getApplicationContext(), "Strong password", Toast.LENGTH_SHORT).show();
                        registeruser(txt_emailid, txt_password, Name);
                    }
                    else if(score==4){
                        Toast.makeText(getApplicationContext(), "Very Strong password", Toast.LENGTH_SHORT).show();
                        registeruser(txt_emailid, txt_password, Name);
                    }
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signupactivity.this,LoginActivity.class));
            }
        });
    }

    private void registeruser(String emailid,String password,String Name){
        pd.setMessage("Registering.Please wait!");
        pd.setCancelable(false);
        pd.show();
    auth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){

            Toast.makeText(Signupactivity.this,"Thank you "+Name+", you are  succesfully registered",Toast.LENGTH_SHORT).show();
            adduser(Name,emailid);
            }
        else{
            pd.dismiss();
            Toast.makeText(Signupactivity.this,"Error "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    });
    }

    private void adduser(String name, String emailid) {
        String id = auth.getCurrentUser().getUid().toString();

        Map<String,Object> doc = new HashMap<>();
        doc.put("ID",id);
        doc.put("Full Name",name);
        doc.put("Email",emailid);
        doc.put("Year",0000);
        doc.put("Bio","noBio");
        doc.put("Image","noImage");

        fstore.collection("users").document(id).set(doc)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Signupactivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}