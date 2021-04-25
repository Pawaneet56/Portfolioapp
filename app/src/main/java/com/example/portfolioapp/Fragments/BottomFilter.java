package com.example.portfolioapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.portfolioapp.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomFilter extends BottomSheetDialogFragment {


    private TextView filter_text;
    private BottomSheetListner bottomSheetListner;
    private Button Close;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_filter_sheet,container,false);

        filter_text = v.findViewById(R.id.bottomsheetfilter);
        Close = v.findViewById(R.id.bottom_close);


        Bundle bundle = getArguments();
        assert bundle != null;
        String filter_type = bundle.getString("filter_type");

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetListner.onButtonClicked(filter_type);
                dismiss();
            }
        });



        filter_text.setText(filter_type);

        return v;
    }



    public interface BottomSheetListner{

        void onButtonClicked(String text);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            bottomSheetListner = (BottomSheetListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }

    }
}
