package com.example.portfolioapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Startactivity extends AppCompatActivity {
private Button signup;
private Button login;
private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        signup=findViewById(R.id.signup);
        login=findViewById(R.id.login);
        fstore=FirebaseFirestore.getInstance();
        signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                startActivity(new Intent(Startactivity.this,Signupactivity.class));

            }


        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Startactivity.this,LoginActivity.class));
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){
            ArrayList<String> a=new ArrayList<String>();
            fstore.collection("filter").document(user.getUid()).update("Id",user.getUid(),"paid_unpaid","noFilter","typeofpost","noFilter","domainitems",a);
            startActivity(new Intent(Startactivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        }
    }
}