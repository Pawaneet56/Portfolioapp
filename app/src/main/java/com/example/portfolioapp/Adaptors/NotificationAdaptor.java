package com.example.portfolioapp.Adaptors;

import android.app.Notification;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfolioapp.Models.Notifications;
import com.example.portfolioapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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


        fstore.collection("users").document(senderuid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = Objects.requireNonNull(documentSnapshot.get("Full Name")).toString();
                        String image = documentSnapshot.getString("Image");
                        String email = documentSnapshot.getString("Email");


                        notifications.get(position).setSname(name);
                        notifications.get(position).setSimage(image);
                        notifications.get(position).setSimage(email);

                    }
                });



        holder.sname.setText(notifications.get(position).getSname());
        holder.snotification.setText(notifications.get(position).getNotification());
        holder.time.setText(ptime);


        try{

            Picasso.get().load(notifications.get(position).getSimage()).into(holder.simage);

        } catch (Exception e) {
            e.printStackTrace();
        }

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
