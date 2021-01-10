package com.example.portfolioapp;

import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
private EditText emailid;
private EditText password;
private TextView Signup1;
private Button login;
private FirebaseAuth fAuth;
private TextView forgotTextLink;
private ImageView Show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailid=findViewById(R.id.emailid);
        password=findViewById(R.id.password);
        fAuth=FirebaseAuth.getInstance();
        login=findViewById(R.id.login);
        Signup1 = findViewById(R.id.Signup1);
        forgotTextLink = findViewById(R.id.forgotpassword);
        Show=findViewById(R.id.show);
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

//login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_emailid=emailid.getText().toString();

                String txt_password=password.getText().toString();


                if(TextUtils.isEmpty(txt_emailid)){
                    emailid.setError("Enter Email Id");
                }
                if(TextUtils.isEmpty(txt_password)) {
                    password.setError("Enter Password");
                }
                else{
                    loginuser(txt_emailid,txt_password);
                }


            }
        });

//forgot password
        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setCancelable(false);
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter Your Email To Receive Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this,"Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this,"Error! Reset Link Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                passwordResetDialog.create().show();
            }
        });


//to direct to signup
        Signup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,Signupactivity.class));
                finish();
            }
        });


    }




//firebase verification
    private void loginuser(String txt_emailid,String txt_password) {
        fAuth.signInWithEmailAndPassword(txt_emailid, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, " You are Logged in Succesfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                } else {
                    Toast.makeText(LoginActivity.this,"Error "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
