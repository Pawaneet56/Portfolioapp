package com.example.portfolioapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.portfolioapp.LoginActivity;
import com.example.portfolioapp.MainActivity;
import com.example.portfolioapp.R;
import com.example.portfolioapp.Signupactivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Startactivity extends AppCompatActivity {
private Button signup;
private Button login;
private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_PortfolioApp);
        setContentView(R.layout.activity_start);

        signup=findViewById(R.id.signup);
        login=findViewById(R.id.login);
        fstore=FirebaseFirestore.getInstance();
        signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                startActivity(new Intent(Startactivity.this, Signupactivity.class));

            }


        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Startactivity.this, LoginActivity.class));
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null){
            ArrayList<String> a=new ArrayList<String>();
            Map<String,Object>doc=new HashMap<>();
            doc.put("Id",user.getUid());
            fstore.collection("filter").document(user.getUid()).set(doc);
            fstore.collection("filter").document(user.getUid()).update("Id",user.getUid(),"paid_unpaid","noFilter","typeofpost","noFilter","domainitems",a);
            startActivity(new Intent(Startactivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        }
    }
}