package com.example.portfolioapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    Context mContext;
    RecyclerView recyclerView;
    ArrayList<Feed> feedArrayList = new ArrayList<>();
    Adaptor_feed adaptor_feed;

    //to get context of the fragment
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().setTitle("Home");

        recyclerView = (RecyclerView) v.findViewById(R.id.recview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        adaptor_feed = new Adaptor_feed(mContext,feedArrayList);
        recyclerView.setAdapter(adaptor_feed);

        uploadfeed();
        return v;
    }

    private void uploadfeed() {
        Feed feed = new Feed("Particle","Project 1","This is first project",R.drawable.avatar,R.drawable.pic2);
        feedArrayList.add(feed);

        feed = new Feed("Particle","Project 2","This is second project",R.drawable.avatar,R.drawable.pic1);
        feedArrayList.add(feed);

        feed = new Feed("Particle","Project 3","This is third project",R.drawable.pic2,R.drawable.pic2);
        feedArrayList.add(feed);

        feed = new Feed("Particle","Project 4","This is fourth project",R.drawable.pic1,R.drawable.avatar);
        feedArrayList.add(feed);
    }
}