package com.example.portfolioapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BottomFilter extends BottomSheetDialogFragment {


    private TextView filter_text;
    private BottomSheetListner bottomSheetListner;
    private Button Close;
    private MultiSpinnerSearch domainselection;
    private List<String>item;
    private final ArrayList<String> itemsindomain=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_filter_sheet,container,false);

        filter_text = v.findViewById(R.id.bottomsheetfilter);
        Close = v.findViewById(R.id.bottom_close);
domainselection=v.findViewById(R.id.searchMultiSpinner);
        item=new ArrayList<>();
        final List<KeyPairBoolData> isselected = new ArrayList();
        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.Domains));

        Bundle bundle = getArguments();
        assert bundle != null;
        String filter_type = bundle.getString("filter_type");
        if(filter_type.equals("Domain")){
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

        }
        else{
            domainselection.setVisibility(View.GONE);
        }

        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filter_type.equals("Domain")){
                if(itemsindomain!=null){
                    Bundle bundle2=new Bundle();
                    bundle2.putString("domain","true");
                    bundle2.putStringArrayList("domainsitems",itemsindomain);
                    HomeFragment fragment = new HomeFragment();
                    fragment.setArguments(bundle2);
                    FragmentManager fragmentManager = ((MainActivity)bottomSheetListner).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment,fragment);
                    fragmentTransaction.addToBackStack(null).commit();

                }
                else{

                    Bundle bundle2=new Bundle();
                    bundle2.putString("domain","false");
                   // bundle2.putStringArrayList("domainsitems",itemsindomain);
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
