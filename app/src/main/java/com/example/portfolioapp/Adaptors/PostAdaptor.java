package com.example.portfolioapp.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfolioapp.Models.Posts;
import com.example.portfolioapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdaptor extends RecyclerView.Adapter<PostAdaptor.myViewHolder> {

    ArrayList<Posts> detalist;
    Context mcontext;

    public PostAdaptor(ArrayList<Posts> detalist, Context mcontext) {
        this.detalist = detalist;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        holder.username.setText(detalist.get(position).getFullName());
        holder.projectname.setText(detalist.get(position).getProjectName());
        holder.details.setText(detalist.get(position).getDetail());

        Picasso.get().load(detalist.get(position).getPostImage()).into(holder.projectpic);

        if(detalist.get(position).getUserImage()==null)
        {
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/portfolio-app-6f30e.appspot.com/o/Users%2Favatar.jpg?alt=media&token=e2d687e6-1ed9-464b-932e-9cb42497c394").into(holder.userpic);
        }
        else
        {
            Picasso.get().load(detalist.get(position).getUserImage()).into(holder.userpic);
        }
    }

    @Override
    public int getItemCount() {
        return detalist.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView username,projectname,details;
        ImageView userpic,projectpic;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.luname);
            projectname = itemView.findViewById(R.id.lproject_name);
            details = itemView.findViewById(R.id.ldeatils);
            userpic = itemView.findViewById(R.id.luserpic);
            projectpic = itemView.findViewById(R.id.lprojectpic);
        }
    }
}
