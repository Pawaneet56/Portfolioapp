package com.example.portfolioapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.example.portfolioapp.MainActivity;
import com.example.portfolioapp.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BottomFilter extends BottomSheetDialogFragment {


    private TextView filter_text;
    private BottomSheetListner bottomSheetListner;
    private Button Close;
    private MultiSpinnerSearch domainselection;
    private List<String>item;
    private FirebaseFirestore fstore;
    private FirebaseAuth fauth;
    private boolean paid=false,unpaid=false,project=false,parttime=false,internships=false;
    private final ArrayList<String> itemsindomain=new ArrayList<>();
    RadioGroup radiogroup;
     RadioButton radio1,radio2,radio3,radio4,radio5;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_filter_sheet,container,false);

        filter_text = v.findViewById(R.id.bottomsheetfilter);
        Close = v.findViewById(R.id.bottom_close);
        domainselection=v.findViewById(R.id.searchMultiSpinner);
        radio1=v.findViewById(R.id.r1);
        radio2=v.findViewById(R.id.r2);
        radio3=v.findViewById(R.id.r3);
        radio4=v.findViewById(R.id.r4);
        radio5=v.findViewById(R.id.r5);
        radiogroup=v.findViewById(R.id.rg);
        fstore=FirebaseFirestore.getInstance();
        fauth=FirebaseAuth.getInstance();

        item=new ArrayList<>();
        final List<KeyPairBoolData> isselected = new ArrayList();
        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.Domains));

        Bundle bundle = getArguments();
        assert bundle != null;
        String filter_type = bundle.getString("filter_type");
        if(filter_type.equals("Domain")){
            radio1.setVisibility(View.GONE);
            radio2.setVisibility(View.GONE);
            radio3.setVisibility(View.GONE);
            radio4.setVisibility(View.GONE);
            radio5.setVisibility(View.GONE);
            //final List<KeyPairBoolData> isselected = new ArrayList<>();
            for(int i=0;i<list.size();i++)
            {
                KeyPairBoolData k = new KeyPairBoolData();
                k.setId(i+1);
                k.setName(list.get(i));

                if(item.contains(list.get(i)))
                {
                    k.setSelected(true);
                }
                else
                {
                    k.setSelected(false);
                }

                isselected.add(k);
            }



        domainselection.setSearchEnabled(true);
        domainselection.setSearchHint("Domain");
        domainselection.setClearText("Close & Clear");
        domainselection.setEmptyTitle("No data found");


        domainselection.setItems(isselected, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                itemsindomain.clear();
                for(int i=0;i<selectedItems.size();i++)
                {
                    if(selectedItems.get(i).isSelected())
                    {
                        itemsindomain.add(selectedItems.get(i).getName());
                    }
                }
            }
        });



        }
        else if(filter_type.equals("Paid/Unpaid")){
            domainselection.setVisibility(View.GONE);
            radio3.setVisibility(View.GONE);
            radio4.setVisibility(View.GONE);
            radio5.setVisibility(View.GONE);
            radio1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paid=true;
                    unpaid=false;
                    project=false;
                    parttime=false;
                    internships=false;
                }
            });
            radio2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paid=false;
                    unpaid=true;
                    project=false;
                    parttime=false;
                    internships=false;
                }
            });





        }
        else if(filter_type.equals("Apply")){
            Bundle bundle2=new Bundle();
            bundle2.putString("apply","true");

            HomeFragment fragment = new HomeFragment();
            fragment.setArguments(bundle2);
            FragmentManager fragmentManager = ((MainActivity)bottomSheetListner).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment,fragment);
            fragmentTransaction.addToBackStack(null).commit();
            bottomSheetListner.onButtonClicked(filter_type);
            dismiss();

        }
        else{
            domainselection.setVisibility(View.GONE);
            radio1.setVisibility(View.GONE);
            radio2.setVisibility(View.GONE);
            radio3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paid=false;
                    unpaid=false;
                    project=true;
                    parttime=false;
                    internships=false;
                }
            });
            radio4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paid=false;
                    unpaid=false;
                    project=false;
                    parttime=true;
                    internships=false;
                }
            });
            radio5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paid=false;
                    unpaid=false;
                    project=false;
                    parttime=false;
                    internships=true;
                }
            });





        }

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filter_type.equals("Domain")){
                if(!itemsindomain.isEmpty()){
                    Bundle bundle2=new Bundle();
                    bundle2.putString("domain","true");
                    String id=fauth.getCurrentUser().getUid();

                    fstore.collection("filter").document(fauth.getCurrentUser().getUid()).update("Id",id,"domain","true","domainitems",itemsindomain);
                    HomeFragment fragment = new HomeFragment();
                    fragment.setArguments(bundle2);
                    FragmentManager fragmentManager = ((MainActivity)bottomSheetListner).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment,fragment);
                    fragmentTransaction.addToBackStack(null).commit();

                }
                else{
                    bottomSheetListner.onButtonClicked(filter_type);

                }
                }
                else if(filter_type.equals("Paid/Unpaid")){
                    if(paid){
                        Bundle bundle2=new Bundle();
                        
                        bundle2.putString("paid/unpaid","true");
                        fstore.collection("filter").document(fauth.getCurrentUser().getUid()).update("paid_unpaid","Paid");

                        HomeFragment fragment = new HomeFragment();
                        fragment.setArguments(bundle2);
                        FragmentManager fragmentManager = ((MainActivity)bottomSheetListner).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment,fragment);
                        fragmentTransaction.addToBackStack(null).commit();

                    }
                    else{
                        Bundle bundle2=new Bundle();
                        bundle2.putString("paid/unpaid","true");
                        fstore.collection("filter").document(fauth.getCurrentUser().getUid()).update("paid_unpaid","Unpaid");

                        HomeFragment fragment = new HomeFragment();
                        fragment.setArguments(bundle2);
                        FragmentManager fragmentManager = ((MainActivity)bottomSheetListner).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment,fragment);
                        fragmentTransaction.addToBackStack(null).commit();

                    }
                }
                else if(filter_type.equals("Apply")){
                    Bundle bundle2=new Bundle();
                    bundle2.putString("apply","true");

                    HomeFragment fragment = new HomeFragment();
                    fragment.setArguments(bundle2);
                    FragmentManager fragmentManager = ((MainActivity)bottomSheetListner).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment,fragment);
                    fragmentTransaction.addToBackStack(null).commit();

                }
                else{
if(project){
    Bundle bundle2=new Bundle();
    bundle2.putString("type","true");
    //bundle2.putStringArrayList("domainsitems",itemsindomain);
    fstore.collection("filter").document(fauth.getCurrentUser().getUid()).update("typeofpost","Project");
    HomeFragment fragment = new HomeFragment();
    fragment.setArguments(bundle2);
    FragmentManager fragmentManager = ((MainActivity)bottomSheetListner).getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.fragment,fragment);
    fragmentTransaction.addToBackStack(null).commit();

}
else if(parttime){
    Bundle bundle2=new Bundle();
    bundle2.putString("type","true");
    fstore.collection("filter").document(fauth.getCurrentUser().getUid()).update("typeofpost","Part Time Jobs");
    //bundle2.putStringArrayList("domainsitems",itemsindomain);
    HomeFragment fragment = new HomeFragment();
    fragment.setArguments(bundle2);
    FragmentManager fragmentManager = ((MainActivity)bottomSheetListner).getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.fragment,fragment);
    fragmentTransaction.addToBackStack(null).commit();

}
else if(internships){
    Bundle bundle2=new Bundle();
    bundle2.putString("type","true");
    fstore.collection("filter").document(fauth.getCurrentUser().getUid()).update("typeofpost","Internships");
    //bundle2.putStringArrayList("domainsitems",itemsindomain);
    HomeFragment fragment = new HomeFragment();
    fragment.setArguments(bundle2);
    FragmentManager fragmentManager = ((MainActivity)bottomSheetListner).getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.fragment,fragment);
    fragmentTransaction.addToBackStack(null).commit();

}
                }
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
