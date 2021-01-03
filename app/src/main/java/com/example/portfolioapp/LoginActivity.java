package com.example.portfolioapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.prefs.Preferences;

public class LoginActivity extends AppCompatActivity {
private EditText emailid;
private EditText password;
private TextView Signup1;
private Button login;
private FirebaseAuth fAuth;
private CheckBox rememberme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailid=findViewById(R.id.emailid);
        password=findViewById(R.id.password);
        fAuth=FirebaseAuth.getInstance();
        login=findViewById(R.id.login);
        Signup1 = findViewById(R.id.Signup1);
        rememberme = findViewById(R.id.rememberme);

        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox = preferences.getString("remember me","");
        if(checkbox.equals("true")){
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);

        }else if(checkbox.equals("false")){
            Toast.makeText(this,"PLease sign in",Toast.LENGTH_SHORT).show();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_emailid=emailid.getText().toString();

                String txt_password=password.getText().toString();


                if(TextUtils.isEmpty(txt_emailid) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(LoginActivity.this, "Empty credentials", Toast.LENGTH_SHORT).show();
                }
                else{
                    loginuser(txt_emailid,txt_password);
                }


            }
        });

        rememberme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember me","true");
                    editor.apply();;
                    Toast.makeText(LoginActivity.this,"checked",Toast.LENGTH_SHORT).show();
                }else if(!compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember me","false");
                    editor.apply();
                    Toast.makeText(LoginActivity.this,"Unchecked",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Signup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,Signupactivity.class));
                finish();
            }
        });
    }
    private void loginuser(String txt_emailid,String txt_password) {
        fAuth.signInWithEmailAndPassword(txt_emailid, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Logged in Succesfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong password or Email Id", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}