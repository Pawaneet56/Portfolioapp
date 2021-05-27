package com.example.portfolioapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.portfolioapp.Fragments.AddPostFragment;
import com.example.portfolioapp.Fragments.BottomFilter;
import com.example.portfolioapp.Fragments.HomeFragment;
import com.example.portfolioapp.Fragments.NotificationsFragment;
import com.example.portfolioapp.Fragments.ProfileFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomFilter.BottomSheetListner {
private DrawerLayout drawerLayout;
private FirebaseAuth f;
private TextView fname,femail;
private FirebaseFirestore fstore;
private ImageView fimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigationView navigationView = findViewById(R.id.nav_view);


        View v = navigationView.getHeaderView(0);

        f = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        fname = v.findViewById(R.id.headerfname);
        fimage=v.findViewById(R.id.headerpic);
        femail=v.findViewById(R.id.headeremail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);


        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open,R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                    new HomeFragment()).commit();// change to whichever id should be default
        }
fstore.collection("users").document(f.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
    @Override
    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
        assert value != null;

            fname.setText(value.getString("Full Name"));
            femail.setText(value.getString("Email"));
            if(value.getString("Image").equals("noImage")){
                fimage.setImageResource(R.drawable.avatar);
            }
            else{
                Picasso.get().load(value.getString("Image")).fit().centerCrop(-10).into(fimage);
            }
        }

});

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.home:
                
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                        new HomeFragment()).commit();
                break;

            case R.id.profile:
                Toast.makeText(this, "YOUR PROFILE", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                        new ProfileFragment()).commit();
                break;

            case R.id.addpost:
                Toast.makeText(this, "Add Project", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                        new AddPostFragment()).commit();
                break;


            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to logout ?");
                builder.setCancelable(false);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        f.signOut();
                        Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,Startactivity.class));
                        finish();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;

            case R.id.notification:
                Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                        new NotificationsFragment()).commit();
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=f.getCurrentUser();
        if(user==null)
        {
            startActivity(new Intent(MainActivity.this,Startactivity.class));
            finish();
        }
        else
        {
            SharedPreferences sp = getSharedPreferences("Sp_USER",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("current_uid",user.getUid());
            editor.apply();
        }
    }

    @Override
    public void onButtonClicked(String text) {
        //Toast.makeText(this,text+" filter was clicked",Toast.LENGTH_SHORT).show();
    }




}

