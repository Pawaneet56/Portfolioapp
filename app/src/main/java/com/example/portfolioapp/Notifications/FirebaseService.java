package com.example.portfolioapp.Notifications;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

public class FirebaseService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String tokenrefresh = FirebaseInstanceId.getInstance().getToken();

        if(user!=null)
        {
            updateToken(tokenrefresh);
        }
    }

    private void updateToken(String tokenrefresh) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        CollectionReference ref = FirebaseFirestore.getInstance().collection("Tokens");
        Token token = new Token(tokenrefresh);

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("token",token);

        ref.document(String.valueOf(user)).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }
}
