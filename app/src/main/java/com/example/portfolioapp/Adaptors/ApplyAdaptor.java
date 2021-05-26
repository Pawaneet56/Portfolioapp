package com.example.portfolioapp.Adaptors;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfolioapp.Fragments.PostDetailFragment;
import com.example.portfolioapp.Fragments.ProfileFragment;
import com.example.portfolioapp.MainActivity;
import com.example.portfolioapp.Models.Apply;
import com.example.portfolioapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ApplyAdaptor extends RecyclerView.Adapter<ApplyAdaptor.ApplyViewHolder> {

    ArrayList<Apply> Applylist;
    Context mcontext;
    String myuid;
    String dp = "noImage";
    ProgressDialog pd;
    FirebaseFirestore fstore;
    FirebaseUser fuser;

    public ApplyAdaptor(ArrayList<Apply> Applylist, Context mcontext){
        this.Applylist = Applylist;
        this.mcontext = mcontext;
        pd = new ProgressDialog(mcontext);
        fstore = FirebaseFirestore.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ApplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.all_bids_list,parent,false);
        return new ApplyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplyViewHolder holder, int position) {

        String applierid = Applylist.get(position).getId();
        String Fullname = Applylist.get(position).getFullName();
        String dp = Applylist.get(position).getUserImage();
        String pid = Applylist.get(position).getPid();


        holder.username.setText(Applylist.get(position).getFullName());

        try {
            Picasso.get().load(Applylist.get(position).getUserImage()).into(holder.dp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dp.equals("noImage")) {
            try {
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/portfolio-app-6f30e.appspot.com/o/Users%2Favatar.jpg?alt=media&token=f342a8f2-bae3-4e23-a87e-401d533bcee8").into(holder.dp);

            } catch (Exception e) {
            }
        }


//        holder.dp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Bundle bundle = new Bundle();
//                bundle.putString("TheirProfile","true");
////                bundle.putString("applierid",applierid);
//
//                PostDetailFragment f = new PostDetailFragment();
//                f.setArguments(bundle);
//                FragmentManager fragmentManager = ((MainActivity)mcontext).getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment,f);
//                fragmentTransaction.addToBackStack(null).commit();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ApplyViewHolder extends RecyclerView.ViewHolder{

        private TextView username;
        private ImageView dp;


        public ApplyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.luname);
            dp = itemView.findViewById(R.id.luserpic);
        }
    }
}


