package com.example.portfolioapp.Adaptors;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfolioapp.Fragments.PostDetailFragment;
import com.example.portfolioapp.Fragments.ProfileFragment;
import com.example.portfolioapp.MainActivity;
import com.example.portfolioapp.Models.Apply;
import com.example.portfolioapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class ApplyAdaptor extends RecyclerView.Adapter<ApplyAdaptor.ApplyViewHolder> {

    ArrayList<Apply> Applylist;
    Context mcontext;
    String myuid;
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


        String id= Applylist.get(position).getId();
        String Fullname = Applylist.get(position).getFullName();
        String dp = Applylist.get(position).getdp();
        String pid = Applylist.get(position).getPid();
        String currentTime = Applylist.get(position).gettime();

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(Long.parseLong(currentTime));
        String ptime = DateFormat.format("dd/MM/yyyy hh:mm:aa", cal).toString();

        try{
            Picasso.get().load(dp).fit().centerCrop().into(holder.userpic);
        }catch(Exception e){}

        holder.Skills.setText(Applylist.get(position).getSkills());
        holder.username.setText(Fullname);
        holder.time.setText(ptime);

        holder.userpic.setOnClickListener(new View.OnClickListener() {
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

        if(Applylist.get(position).getStatus().equals("Accepted"))
        {
            holder.Accept.setTextColor(Color.parseColor("#006400"));
            holder.Accept.setText("Accepted");
            holder.Reject.setVisibility(View.GONE);
        }

        else
        {
            holder.Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    fstore.collection("Posts").document(pid).collection("Apply")
                            .document(id)
                            .update("status","Accepted")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //holder.Accept.setBackgroundColor(0xFF7CFC00);
                                    holder.Accept.setTextColor(Color.parseColor("#006400"));
                                    holder.Accept.setText("Accepted");

                                    addtoNotification(pid,id);
                                    holder.Reject.setVisibility(View.GONE);
                                }
                            });


                }
            });

            if(holder.Accept.getText().equals("Accept"))
            {
                holder.Reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fstore.collection("Posts").document(pid).collection("Apply")
                                .document(id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Applylist.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position,Applylist.size());
                                    }
                                });
                    }
                });

            }
            else
            {
                holder.Reject.setVisibility(View.GONE);
            }

        }



    }

    private void addtoNotification(String pid, String id) {

        HashMap<String,Object> map=new HashMap<>();

        String timestamp = ""+System.currentTimeMillis();



        fstore.collection("Posts").document(pid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        if(error!=null)
                        {
                            return;
                        }
                        if(value.exists())
                        {
                            map.put("pid",pid);
                            map.put("suid",id);
                            map.put("timestamp",timestamp);
                            map.put("notification","Your proposal is accepted !");
                            map.put("type","accept");
                            map.put("sname",value.getString("FullName"));
                            map.put("simage",value.getString("UserImage"));
                            map.put("puid",value.getString("Id"));

                            fstore.collection("Notifications")
                                    .document(pid+"accept"+timestamp)
                                    .set(map);

                        }
                    }
                });




    }

    @Override
    public int getItemCount() {
        return Applylist.size();
    }

    public class ApplyViewHolder extends RecyclerView.ViewHolder{

        private TextView username;
        private ImageView userpic;
        private TextView time;
        private TextView Skills;
        private Button Accept,Reject;

        public ApplyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.luname);
            userpic = itemView.findViewById(R.id.luserpic);
            time=itemView.findViewById(R.id.ltime);
            Skills=itemView.findViewById(R.id.lskills);
            Accept = itemView.findViewById(R.id.AcceptBtn);
            Reject = itemView.findViewById(R.id.DeclineBtn);
        }
    }
}


