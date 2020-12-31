package com.example.portfolioapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = findViewById(R.id.drawer);
    }

    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }
    static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }


    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }
    static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    public void ClickHome(View view) {
        Toast.makeText(ProfileActivity.this, "Home",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ProfileActivity.this,HomeActivity.class));
        recreate();
    }



    public void ClickProfile(View view){
        Toast.makeText(ProfileActivity.this, "Your Profile",Toast.LENGTH_SHORT).show();
        recreate();
    }



    public  void ClickLogout(View view){
        logout(this);
    }
    private void logout(Activity activity) {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle("LOGOUT");
        builder.setMessage("Are you sure you want to logout ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ProfileActivity.this, "Logged out Succesfully", Toast.LENGTH_SHORT).show();
                activity.startActivity(new Intent( ProfileActivity.this,Startactivity.class));
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
}