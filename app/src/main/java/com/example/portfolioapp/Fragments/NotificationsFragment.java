package com.example.portfolioapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.portfolioapp.Adaptors.NotificationAdaptor;
import com.example.portfolioapp.Models.Notifications;
import com.example.portfolioapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView notify_rec;
    private FirebaseAuth fauth;
    private FirebaseFirestore fstore;
    private ArrayList<Notifications> notify;
    private Context mcontext;
    private NotificationAdaptor nadaptor;


    public NotificationsFragment()
    {

    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notifications, container, false);
        getActivity().setTitle("Notifications");

        notify_rec = v.findViewById(R.id.notification_recycler);
        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        notify = new ArrayList<>();
        String current_user = fauth.getCurrentUser().getUid();

        LinearLayoutManager ll = new LinearLayoutManager(mcontext);
        ll.setStackFromEnd(true);
        ll.setReverseLayout(true);

        notify_rec.setLayoutManager(ll);

        nadaptor = new NotificationAdaptor(mcontext,notify);

        fstore.collection("Notifications")
                .whereEqualTo("type","like").whereEqualTo("puid",current_user).orderBy("timestamp")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list)
                {
                    Notifications obj = d.toObject(Notifications.class);
                    notify.add(obj);
                }

                notify_rec.setAdapter(nadaptor);
                nadaptor.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(mcontext,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


        fstore.collection("Notifications")
                .whereEqualTo("type","post")
                .whereNotEqualTo("puid",current_user)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list)
                        {
                            Notifications obj = d.toObject(Notifications.class);
                            notify.add(obj);
                        }
                        notify_rec.setAdapter(nadaptor);
                        nadaptor.notifyDataSetChanged();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(mcontext,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });




        return v;
    }
}