package com.example.portfolioapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

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
    private EditText snumber;
    private FirebaseFirestore fstore;
    private String userID;
    private CountryCodePicker sccp;


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
        snumber=findViewById(R.id.snumber);
        fstore=FirebaseFirestore.getInstance();
        sccp=findViewById(R.id.sccp);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_emailid=emailid.getText().toString();
                String txt_password=password.getText().toString();
                String Name=name.getText().toString();
                String txt_cpassword=cpassword.getText().toString();
                String txt_snumber="+"+sccp.getSelectedCountryCode()+snumber.getText().toString();

                if(TextUtils.isEmpty(txt_emailid) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(Name) || TextUtils.isEmpty(txt_snumber)){
                    Toast.makeText(Signupactivity.this,"Empty credentials",Toast.LENGTH_SHORT).show();
                }
                else if(txt_password.length()<6){
                    Toast.makeText(Signupactivity.this,"Password is too Short",Toast.LENGTH_SHORT).show();
                }
                else if(txt_snumber.length()<10){
                    Toast.makeText(Signupactivity.this,"Enter Valid Phone Number",Toast.LENGTH_SHORT).show();
                }
                else if(!txt_password.equals(txt_cpassword)){
                    Toast.makeText(Signupactivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                }
                else{
                    registeruser(txt_emailid,txt_password,Name);
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
    auth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            Toast.makeText(Signupactivity.this,"Thank you "+Name+", you are  succesfully registered",Toast.LENGTH_SHORT).show();

            userID = auth.getCurrentUser().getUid();
            DocumentReference documentReference = fstore.collection("users").document(userID);

            Map<String,Object> user = new HashMap<>();
            user.put("Full Name",name);
            user.put("Email",emailid);
            user.put("Phone Number",snumber);

            documentReference.set(user);

            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }
        else{
            Toast.makeText(Signupactivity.this,"sorry "+Name+", try again",Toast.LENGTH_SHORT).show();
        }
    }
});
    }


}