package com.example.portfolioapp.Adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfolioapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class experienceadaptor extends RecyclerView.Adapter<experienceadaptor.experienceViewHolder> {
    @NonNull
    @NotNull
    Context context;
    ArrayList<String >data;
    public experienceadaptor(ArrayList<String> data,Context context){
        this.data=data;
        this.context=context;
    }
    public experienceViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.experienceitems,parent,false);
        return new experienceViewHolder(v);

    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull experienceViewHolder holder, int position) {
holder.t1.setText(data.get(position).toString());
holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View v) {
        AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
        builder.setTitle("Do you want to delete it ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("experience", FieldValue.arrayRemove(data.get(position)));
                data.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,data.size());

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
dialog.dismiss();
            }
        });
        builder.create();

        // create the alert dialog with the
        // alert dialog builder instance
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return false;
    }
});
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class experienceViewHolder extends RecyclerView.ViewHolder{
        TextView t1;
        public experienceViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.ex);
        }
    }
}
