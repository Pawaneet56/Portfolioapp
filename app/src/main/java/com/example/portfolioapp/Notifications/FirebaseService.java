package com.example.portfolioapp.Notifications;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FirebaseService extends FirebaseMessaging {

    @Override
    public void onNewToken(@NonNull @NotNull String s) {
        super.onNewToken(s);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null)
        {
            updateToken(s);
        }
    }

    private void updateToken(String tokenrefresh) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        CollectionReference ref = FirebaseFirestore.getInstance().collection("Tokens");
        Token token = new Token(tokenrefresh);

        ref.document(String.valueOf(user)).update("token",token).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }
}
