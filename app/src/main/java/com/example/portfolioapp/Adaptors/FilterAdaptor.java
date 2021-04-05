package com.example.portfolioapp.Adaptors;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfolioapp.Models.Filters;
import com.example.portfolioapp.R;

import java.util.ArrayList;
import java.util.logging.Filter;

public class FilterAdaptor extends RecyclerView.Adapter<FilterAdaptor.myViewHolder> {

    ArrayList<Filters> Filterlist;
    Context mcontext;


    public FilterAdaptor(ArrayList<Filters> filterlist, Context mcontext) {

        this.Filterlist = filterlist;
        this.mcontext = mcontext;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_view_option,parent,false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        holder.filter.setText(Filterlist.get(position).getFilters());
    }

    @Override
    public int getItemCount() {
        return Filterlist.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        Button filter;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            filter = itemView.findViewById(R.id.filter_button);

        }
    }
}
