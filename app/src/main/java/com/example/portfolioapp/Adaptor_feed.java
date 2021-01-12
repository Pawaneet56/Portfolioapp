package com.example.portfolioapp;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adaptor_feed extends RecyclerView.Adapter<Adaptor_feed.viewHolder> {

    Context context;
    ArrayList<Feed> FeedArrayList = new ArrayList<>();



    public Adaptor_feed(Context context,ArrayList<Feed> FeedArrayList){
        this.context=context;
        this.FeedArrayList=FeedArrayList;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        viewHolder vholder = new viewHolder(v);

        return vholder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final Feed feed = FeedArrayList.get(position);

        holder.uname.setText(feed.getUname());
        holder.pname.setText(feed.getPname());
        holder.description.setText(feed.getDescription());

        Picasso.get().load(feed.getUserpic()).into(holder.userpic);
        Picasso.get().load(feed.getPostpic()).into(holder.postpic);

    }

    @Override
    public int getItemCount() {
        return FeedArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView uname,pname,description;
        ImageView userpic,postpic;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            userpic = (ImageView) itemView.findViewById(R.id.pic1);
            postpic = (ImageView) itemView.findViewById(R.id.pic2);

            pname = (TextView) itemView.findViewById(R.id.t1);
            description = (TextView) itemView.findViewById(R.id.t2);
            uname = (TextView) itemView.findViewById(R.id.uname);
        }
    }
}
