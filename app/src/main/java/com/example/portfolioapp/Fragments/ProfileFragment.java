package com.example.portfolioapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.text.BreakIterator;
import java.util.ArrayList;
import com.example.portfolioapp.R;


import java.util.Collections;
import java.util.List;

public class ProfileFragment extends Fragment {
    Spinner spinner;
    private View MultiSpinner;
    private Context mContext;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Profile Fragment");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        spinner = view.findViewById(R.id.spinner1);


        ArrayList<String> numberList = new ArrayList<>();
        numberList.add("Select your college");
        numberList.add("Indian Institute of Technology,Bombay");
        numberList.add("Indian Institute of Technology,Delhi");
        numberList.add("Indian Institute of Technology,Kharagpur");
        numberList.add("Indian Institute of Technology,Kanpur");
        numberList.add("Indian Institute of Technology,Roorkee");
        numberList.add("Indian Institute of Technology,Guwahati");
        numberList.add("Indian Institute of Technology,Gandhinagr");
        numberList.add("Indian Institute of Technology,Indore");



        spinner.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,numberList));



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Toast.makeText(getContext(),"Please Select your College",Toast.LENGTH_SHORT).show();

                }else{
                    String sNumber = parent.getItemAtPosition(position).toString();


                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }
}
