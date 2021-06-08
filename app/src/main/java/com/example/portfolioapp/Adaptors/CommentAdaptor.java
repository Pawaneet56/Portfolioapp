package com.example.portfolioapp.Adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfolioapp.Fragments.ProfileFragment;
import com.example.portfolioapp.MainActivity;
import com.example.portfolioapp.Models.Comments;
import com.example.portfolioapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CommentAdaptor extends RecyclerView.Adapter<CommentAdaptor.myViewHolder>{

    Context mcontext;
    ArrayList<Comments> comment_list;
    FirebaseFirestore fstore;
    FirebaseUser fuser;

    public CommentAdaptor(Context mcontext, ArrayList<Comments> comment_list) {
        this.mcontext = mcontext;
        this.comment_list = comment_list;
        fstore = FirebaseFirestore.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @NotNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mcontext).inflate(R.layout.comment_list,parent,false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentAdaptor.myViewHolder holder, int position) {

        String timestamp = comment_list.get(position).getTimestamp();
        String pid = comment_list.get(position).getPid();
        String uid  = comment_list.get(position).getUid();

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(Long.parseLong(timestamp));
        String ptime = DateFormat.format("dd/MM/yyyy hh:mm:aa", cal).toString();


        holder.txtcomment.setText(comment_list.get(position).getComment());
        holder.username.setText(comment_list.get(position).getUname());
        holder.time.setText(ptime);


        try {
            Picasso.get().load(comment_list.get(position).getDp()).into(holder.Userimage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(fuser.getUid().equals(uid) || fuser.getUid().equals(fuser.getUid()))
        {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mcontext);
                    dialog.setMessage("Are you sure you want to delete the comment ?");
                    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            fstore.collection("Comments").document(pid).collection("user")
                                    .document(uid+timestamp).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(mcontext,"Comment deleted successfully",Toast.LENGTH_SHORT).show();
                                            comment_list.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position,comment_list.size());
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(mcontext,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                    return false;
                }
            });

        }


        holder.Userimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("TheirProfile","true");
                bundle.putString("uid",uid);

                ProfileFragment fragment = new ProfileFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((MainActivity)mcontext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,fragment);
                fragmentTransaction.addToBackStack(null).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return comment_list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        ImageView Userimage;
        TextView txtcomment,username,time;


        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            Userimage = itemView.findViewById(R.id.comment_userpic);

            txtcomment = itemView.findViewById(R.id.comment_txt);
            username = itemView.findViewById(R.id.comment_user_name);
            time = itemView.findViewById(R.id.comment_time);


        }
    }

}
