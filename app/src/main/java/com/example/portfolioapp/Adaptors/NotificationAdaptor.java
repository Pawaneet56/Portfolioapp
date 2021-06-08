package com.example.portfolioapp.Adaptors;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfolioapp.Fragments.HomeFragment;
import com.example.portfolioapp.Fragments.NotificationsFragment;
import com.example.portfolioapp.Fragments.PostDetailFragment;
import com.example.portfolioapp.Fragments.ProfileFragment;
import com.example.portfolioapp.MainActivity;
import com.example.portfolioapp.Models.Notifications;
import com.example.portfolioapp.R;
import com.example.portfolioapp.Startactivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class NotificationAdaptor extends RecyclerView.Adapter<NotificationAdaptor.myViewHolder>{

    private Context context;
    private ArrayList<Notifications> notifications;
    private FirebaseFirestore fstore;

    public NotificationAdaptor(Context context, ArrayList<Notifications> notifications) {
        this.context = context;
        this.notifications = notifications;
        fstore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @NotNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.notification_list,parent,false);


        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotificationAdaptor.myViewHolder holder, int position) {

        String timestamp = notifications.get(position).getTimestamp();
        String senderuid  = notifications.get(position).getSuid();

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(Long.parseLong(timestamp));
        String ptime = DateFormat.format("dd/MM/yyyy hh:mm:aa", cal).toString();


        fstore.collection("users").document(senderuid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                        if(error!=null)
                        {
                            return;
                        }
                        if(value.exists())
                        {
                            String name = Objects.requireNonNull(value.get("Full Name")).toString();
                            String image = value.getString("Image");
                            String email = value.getString("Email");


                            holder.sname.setText(name);
                            holder.snotification.setText(notifications.get(position).getNotification());

                            if(!image.equals("noImage"))

                            {

                                try{

                                    Picasso.get().load(image).into(holder.simage);

                                } catch (Exception e) {
                                }
                            }
                            else
                            {

                                try{

                                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/portfolio-app-6f30e.appspot.com/o/Users%2Favatar.jpg?alt=media&token=f342a8f2-bae3-4e23-a87e-401d533bcee8").into(holder.simage);

                                } catch (Exception e) {
                                }
                            }


                        }
                    }
                });





        holder.time.setText(ptime);




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putString("pid",notifications.get(position).getPid());
                bundle.putString("puid",notifications.get(position).getPuid());
                bundle.putString("uname",notifications.get(position).getSname());
                bundle.putString("uimage",notifications.get(position).getSimage());

                fstore.collection("Posts").document(notifications.get(position).getPid()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                long plikes = (long) documentSnapshot.get("pLike");

                                bundle.putLong("tot_likes",plikes);
                            }
                        });

                PostDetailFragment f = new PostDetailFragment();
                f.setArguments(bundle);
                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,f);
                fragmentTransaction.addToBackStack(null).commit();

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete ?");
                builder.setCancelable(false);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        fstore.collection("Notifications").document(notifications.get(position).getPid()
                                +notifications.get(position).getType()+timestamp)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context,"Notification is deleted",Toast.LENGTH_SHORT).show();
                                notifications.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,notifications.size());
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });



                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();


                return false;
            }
        });


        holder.simage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("TheirProfile","true");
                bundle.putString("uid",senderuid);

                ProfileFragment fragment = new ProfileFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,fragment);
                fragmentTransaction.addToBackStack(null).commit();

            }
        });



    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        ImageView simage;
        TextView sname,snotification,time;

        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            simage = itemView.findViewById(R.id.notify_userpic);

            sname = itemView.findViewById(R.id.user_name);
            snotification = itemView.findViewById(R.id.post_title);
            time = itemView.findViewById(R.id.post_time);

        }
    }

}
