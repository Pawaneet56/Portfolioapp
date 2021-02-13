package com.example.portfolioapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.example.portfolioapp.R;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;


public class FilterFragment extends Fragment {
    private Chip chip4,chip5,chip6,chip7,chip8,chip9,chip10,chip11,chip12,chip13;
    private Button Apply;
    private ArrayList<String> selectedChipData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_filter, container, false);
        chip4 = v.findViewById(R.id.chip4);
        chip5 = v.findViewById(R.id.chip5);
        chip6 = v.findViewById(R.id.chip6);
        chip7 = v.findViewById(R.id.chip7);
        chip8 = v.findViewById(R.id.chip8);
        chip9 = v.findViewById(R.id.chip9);
        chip10 =v. findViewById(R.id.chip10);
        chip11 = v.findViewById(R.id.chip11);
        chip12 = v.findViewById(R.id.chip12);
        chip13 = v.findViewById(R.id.chip13);

        selectedChipData = new ArrayList<>();

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    selectedChipData.add(buttonView.getText().toString());
                }
                else
                {
                    selectedChipData.remove(buttonView.getText().toString());
                }
            }
        };

        chip4.setOnCheckedChangeListener(checkedChangeListener);
        chip5.setOnCheckedChangeListener(checkedChangeListener);
        chip6.setOnCheckedChangeListener(checkedChangeListener);
        chip7.setOnCheckedChangeListener(checkedChangeListener);
        chip8.setOnCheckedChangeListener(checkedChangeListener);
        chip9.setOnCheckedChangeListener(checkedChangeListener);
        chip10.setOnCheckedChangeListener(checkedChangeListener);
        chip11.setOnCheckedChangeListener(checkedChangeListener);
        chip12.setOnCheckedChangeListener(checkedChangeListener);
        chip13.setOnCheckedChangeListener(checkedChangeListener);

        Apply = v.findViewById(R.id.Apply);
        Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("data",selectedChipData.toString());


            }
        });


        // Inflate the layout for this fragment
        return v;
    }
}