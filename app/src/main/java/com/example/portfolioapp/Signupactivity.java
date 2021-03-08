package com.example.portfolioapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.portfolioapp.Enum.PasswordStrength;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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
    private FirebaseFirestore fstore;
    private FirebaseStorage fstorage;
    private ProgressDialog pd;

    private ProgressBar pb;
    private TextView strength;
    private TextInputLayout errorname,erroremail,errorpassword,errorcpassword;




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
        fstore=FirebaseFirestore.getInstance();
        fstorage=FirebaseStorage.getInstance();
        pd = new ProgressDialog(this);


        strength = findViewById(R.id.password_strength);
        pb = findViewById(R.id.progressBar);
        errorname = findViewById(R.id.errorname);
        erroremail = findViewById(R.id.erroremail);
        errorpassword = findViewById(R.id.errorpassword);
        errorcpassword = findViewById(R.id.errorcpassword);



        signup.setOnClickListener(new View.OnClickListener() {


            Boolean isName=false,isEmail=false,ispassword=false,iscpassword=false;

            @Override
            public void onClick(View v) {

                String txt_emailid = emailid.getText().toString();
                String txt_password = password.getText().toString();
                String Name = name.getText().toString();
                String txt_cpassword = cpassword.getText().toString();


                if (txt_emailid.isEmpty()) {
                    erroremail.setError("Enter Email Id");
                    isEmail=false;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(txt_emailid).matches())
                {
                    erroremail.setError("Enter Valid Email Address");
                    isEmail=false;
                }
                else
                {
                    erroremail.setErrorEnabled(false);
                    isEmail=true;
                }

                if (Name.isEmpty()) {
                    isName=false;
                    errorname.setError("Enter Your Name");
                }
                else
                {
                    errorname.setErrorEnabled(false);
                    isName=true;
                }

                if(txt_password.isEmpty())
                {
                    ispassword=false;
                    errorpassword.setError("Password is Empty");
                }
                else if(strength.getText().equals("Weak"))
                {
                    errorpassword.setError("Password is weak please try again");
                    ispassword=false;
                }
                else
                {
                    errorpassword.setErrorEnabled(false);
                    ispassword=true;
                }

                if(!txt_cpassword.equals(txt_password))
                {
                    iscpassword=false;
                    errorcpassword.setError("Password Dosen't Match");
                }
                else
                {
                    errorcpassword.setErrorEnabled(false);
                    iscpassword=true;
                }


                if(isEmail && isName && ispassword && iscpassword)
                {
                    registeruser(txt_emailid,txt_password,Name);
                }

            }
        });

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(name.getText().length()!=0)
                {
                    errorname.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!Patterns.EMAIL_ADDRESS.matcher(emailid.getText().toString()).matches())
                {
                    erroremail.setError("Enter Valid Email Address");
                }
                else
                {
                    erroremail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                PasswordStrength passwordStrength = PasswordStrength.calculate(s.toString());
                if(password.getText().toString().isEmpty())
                {
                    strength.setVisibility(View.GONE);
                    pb.setVisibility(View.GONE);

                }
                else
                {
                    strength.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.VISIBLE);

                }
                strength.setText(passwordStrength.msg);
                strength.setTextColor(passwordStrength.color);
                pb.getProgressDrawable().setColorFilter(passwordStrength.color, PorterDuff.Mode.SRC_IN);
                if(strength.getText().equals("Weak")){
                    pb.setProgress(25);
                    errorpassword.setError("Password is weak please try again");
                }else if(strength.getText().equals("Mediue")){
                    pb.setProgress(50);
                    errorpassword.setErrorEnabled(false);
                }else if(strength.getText().equals("Strong")){
                    pb.setProgress(75);
                    errorpassword.setErrorEnabled(false);
                }else if(strength.getText().equals("Very Strong")){
                    pb.setProgress(100);
                    errorpassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!cpassword.getText().toString().equals(password.getText().toString()))
                {
                    errorcpassword.setError("Password Dosent Match");
                }
                else
                {
                    errorcpassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        doc.put("college",0);
        doc.put("collegeName","Select your college");

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