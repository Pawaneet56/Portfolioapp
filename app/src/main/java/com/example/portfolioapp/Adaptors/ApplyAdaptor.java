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
import android.widget.Toast;

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
import java.util.Date;
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
if(Applylist.size()>0) {
    String id= Applylist.get(position).getId();
    String Fullname = Applylist.get(position).getFullName();
    String dp = Applylist.get(position).getdp();
    String pid = Applylist.get(position).getPid();
    String currentTime = Applylist.get(position).gettime();
    Toast.makeText(mcontext,currentTime,Toast.LENGTH_SHORT).show();
    Picasso.get().load(dp).fit().centerCrop().into(holder.dp);
    holder.Skills.setText(Applylist.get(position).getSkills());
    holder.username.setText(Fullname);
    holder.time.setText(currentTime);
     holder.dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("TheirProfile","true");
                bundle.putString("uid",id);

                ProfileFragment f = new ProfileFragment();
                f.setArguments(bundle);
                FragmentManager fragmentManager = ((MainActivity)mcontext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,f);
                fragmentTransaction.addToBackStack(null).commit();
            }
       });
}
    }

    @Override
    public int getItemCount() {
        return Applylist.size();
    }

    public class ApplyViewHolder extends RecyclerView.ViewHolder{

        private TextView username;
        private ImageView dp;
private TextView time;
private TextView Skills;

        public ApplyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.luname);
            dp = itemView.findViewById(R.id.luserpic);
            time=itemView.findViewById(R.id.ltime);
            Skills=itemView.findViewById(R.id.lskills);
        }
    }
}


