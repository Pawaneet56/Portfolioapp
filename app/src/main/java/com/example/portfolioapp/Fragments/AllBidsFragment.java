package com.example.portfolioapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfolioapp.Adaptors.ApplyAdaptor;
import com.example.portfolioapp.Models.Apply;
import com.example.portfolioapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllBidsFragment extends Fragment {

    private ArrayList<Apply> Applylist;
    private RecyclerView bidsRecycle;
    private ApplyAdaptor mAdapter;

    String pid,applierid;

    private RecyclerView.LayoutManager mLayoutManager;

    private FirebaseAuth fauth;
    FirebaseUser fuser;
    FirebaseFirestore fstore;
    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_all_bids_page, container, false);
        requireActivity().setTitle("Who Applied");
        Applylist = new ArrayList<>();
        fstore = FirebaseFirestore.getInstance();
        bidsRecycle = v.findViewById(R.id.bidsRecycle);
//        fuser = fauth.getCurrentUser();
//        applierid  = fuser.getUid();

        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            pid = bundle.getString("pid");
        }
        buildRecyclerView();
        return v;
    }


    public void buildRecyclerView(){
        bidsRecycle.setHasFixedSize(true);

        bidsRecycle.setLayoutManager(new LinearLayoutManager(mContext));
       /* LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);*/


//        bidsRecycle.setAdapter(mAdapter);
        mAdapter=new ApplyAdaptor(Applylist,mContext);

        fstore.collection("Posts").document(pid).collection("Apply")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                Applylist.clear();
                for (DocumentSnapshot d : list) {
                    Apply obj = d.toObject(Apply.class);
                    Applylist.add(obj);
                }
                bidsRecycle.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });


    }

}