package com.example.portfolioapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.portfolioapp.Adaptors.FilterAdaptor;
import com.example.portfolioapp.Adaptors.PostAdaptor;
import com.example.portfolioapp.Models.Filters;
import com.example.portfolioapp.Models.Posts;
import com.example.portfolioapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {

    private Context mContext;
    private FirebaseFirestore fstore;
    private FirebaseAuth fauth;
    private RecyclerView recyclerView;
    private ArrayList<Posts> datalist;
    private String paid_unpaid = "false",paid_unpaid1 = "false",type = "false",typeofpost="false";
    private PostAdaptor fadaptor;
    private String Profile = "false", domain1 = "false", domain2="false";
    private RecyclerView filterrecycler;
    private ArrayList<Filters> filterOptions;
    private FilterAdaptor filterAdaptor;
    private String apply="false";
    private ArrayList<Posts> searchList=new ArrayList<>();
    private ArrayList  domains1;
    Bundle bundle;

    String token;
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

        fstore = FirebaseFirestore.getInstance();
        recyclerView = v.findViewById(R.id.recview);
        filterrecycler = v.findViewById(R.id.filterrecycler);
        fauth = FirebaseAuth.getInstance();

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true);
        linearLayoutManager1.setStackFromEnd(true);

        filterrecycler.setLayoutManager(linearLayoutManager1);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        datalist = new ArrayList<>();
        fadaptor = new PostAdaptor(datalist, getContext());
         bundle = getArguments();
         if(bundle!=null){
             if(bundle.getString("paid/unpaid")!=null)
         paid_unpaid1=bundle.getString("paid/unpaid");
             if(bundle.getString("type")!=null)
                 type=bundle.getString("type");
             if(bundle.getString("domain")!=null)
                 domain2=bundle.getString("domain");
             if(bundle.getString("apply")!=null)
                 apply=bundle.getString("apply");

         }
         if(apply.equals("true")){
fstore.collection("Posts").orderBy("pTime").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
    @Override
    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        datalist.clear();
        List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
        for(DocumentSnapshot d:list){
           fstore.collection("Posts").document(d.getString("pid")).collection("Apply").document(fauth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
               @Override
               public void onSuccess(DocumentSnapshot documentSnapshot) {
                   if(documentSnapshot.exists()){
                       Posts obj=d.toObject(Posts.class);
                       datalist.add(obj);
                   }
                   recyclerView.setAdapter(fadaptor);
                   fadaptor.notifyDataSetChanged();
               }
           });
        }

    }
});
         }
        else  if(paid_unpaid1.equals("true") || type.equals("true") || domain2.equals("true")){
             fetchdata2();
           }
         else{
        fetchdata();}


        filterOptions = new ArrayList<>();
        filterAdaptor = new FilterAdaptor(filterOptions, getContext());

        filterOptions.add(new Filters("Job Type"));
        filterOptions.add(new Filters("Paid/Unpaid"));
        filterOptions.add(new Filters("Domain"));
        filterOptions.add(new Filters("Applied"));

        filterrecycler.setAdapter(filterAdaptor);
        filterAdaptor.notifyDataSetChanged();


        return v;
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FirebaseInstallations.getInstance().getToken(true).addOnSuccessListener(new OnSuccessListener<InstallationTokenResult>() {
            @Override
            public void onSuccess(InstallationTokenResult installationTokenResult) {
                token  = installationTokenResult.getToken();
                Savetoken(token);
            }
        });
    }

    private void Savetoken(String token) {


        HashMap<String,Object> map = new HashMap<>();
        map.put("Token",token);

        fstore.collection("Tokens").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });


    }

    private void fetchdata2(){
        fstore.collection("filter").document(fauth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    domains1=(ArrayList<String>) documentSnapshot.get("domainitems");
                    domain1=documentSnapshot.getString("paid_unpaid");
                    typeofpost=documentSnapshot.getString("typeofpost");
                    if(domains1.isEmpty() && domain1.equals("noFilter") && !typeofpost.equals("noFilter")){
                    fstore.collection("Posts").whereEqualTo("Type_of_post",typeofpost).orderBy("pTime").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Posts obj = d.toObject(Posts.class);
                                datalist.add(obj);
                            }
                            recyclerView.setAdapter(fadaptor);
                            fadaptor.notifyDataSetChanged();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                   else if(domains1.isEmpty() && !domain1.equals("noFilter") && typeofpost.equals("noFilter")){
                        fstore.collection("Posts").whereEqualTo("Paid_Unpaid",domain1).orderBy("pTime").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    Posts obj = d.toObject(Posts.class);
                                    datalist.add(obj);
                                }
                                recyclerView.setAdapter(fadaptor);
                                fadaptor.notifyDataSetChanged();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if(!domains1.isEmpty() && domain1.equals("noFilter") && typeofpost.equals("noFilter")){
                        fstore.collection("Posts").whereArrayContainsAny("Domain",domains1).orderBy("pTime").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    Posts obj = d.toObject(Posts.class);
                                    datalist.add(obj);
                                }
                                recyclerView.setAdapter(fadaptor);
                                fadaptor.notifyDataSetChanged();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if(!domains1.isEmpty() && !domain1.equals("noFilter") && typeofpost.equals("noFilter")){
                        fstore.collection("Posts").whereArrayContainsAny("Domain",domains1).whereEqualTo("Paid_Unpaid",domain1).orderBy("pTime").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    Posts obj = d.toObject(Posts.class);
                                    datalist.add(obj);
                                }
                                recyclerView.setAdapter(fadaptor);
                                fadaptor.notifyDataSetChanged();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if(domains1.isEmpty() && !domain1.equals("noFilter") && !typeofpost.equals("noFilter")){
                        fstore.collection("Posts").whereEqualTo("Paid_Unpaid",domain1).whereEqualTo("Type_of_post",typeofpost).orderBy("pTime").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    Posts obj = d.toObject(Posts.class);
                                    datalist.add(obj);
                                }
                                recyclerView.setAdapter(fadaptor);
                                fadaptor.notifyDataSetChanged();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if(!domains1.isEmpty() && domain1.equals("noFilter") && !typeofpost.equals("noFilter")){
                        fstore.collection("Posts").whereArrayContainsAny("Domain",domains1).whereEqualTo("Type_of_post",typeofpost).orderBy("pTime").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    Posts obj = d.toObject(Posts.class);
                                    datalist.add(obj);
                                }
                                recyclerView.setAdapter(fadaptor);
                                fadaptor.notifyDataSetChanged();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if(!domains1.isEmpty() && !domain1.equals("noFilter") && !typeofpost.equals("noFilter")){
                        fstore.collection("Posts").whereArrayContainsAny("Domain",domains1).whereEqualTo("Paid_Unpaid",domain1).whereEqualTo("Type_of_post",typeofpost).orderBy("pTime").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    Posts obj = d.toObject(Posts.class);
                                    datalist.add(obj);
                                }
                                recyclerView.setAdapter(fadaptor);
                                fadaptor.notifyDataSetChanged();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

}
    private void fetchdata() {

        if (bundle != null) {
            if (bundle.getString("post") != null) {
                Profile = bundle.getString("post");
            }
        }

        if (Profile.equals("true")) {
            String uid = bundle.getString("uid");
            fstore.collection("Posts").whereEqualTo("Id",uid).orderBy("pTime").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                    List<DocumentSnapshot>list=value.getDocuments();
                    datalist.clear();
                    for(DocumentSnapshot doc: list){
                        Posts obj=doc.toObject(Posts.class);
                        datalist.add(obj);
                    }
                    recyclerView.setAdapter(fadaptor);
                    fadaptor.notifyDataSetChanged();

                }
            });


        } else {
            fstore.collection("Posts").orderBy("pTime").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        Posts obj = d.toObject(Posts.class);
                        datalist.add(obj);
                    }
                    recyclerView.setAdapter(fadaptor);
                    fadaptor.notifyDataSetChanged();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
            /*fstore.collection("Posts").orderBy("pTime").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                List<DocumentSnapshot>list=value.getDocuments();
                datalist.clear();
                for(DocumentSnapshot doc: list){
                    Posts obj=doc.toObject(Posts.class);
                    datalist.add(obj);
                }
                recyclerView.setAdapter(fadaptor);
                fadaptor.notifyDataSetChanged();

            }
        });*/

        }


    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
setHasOptionsMenu(true);}

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
getActivity().getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {

                    fstore.collection("Posts").orderBy("pTime").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            datalist.clear();
                            for (DocumentSnapshot d : queryDocumentSnapshots) {

                                    Posts obj = d.toObject(Posts.class);
                                    datalist.add(obj);

                            }
                            recyclerView.setAdapter(fadaptor);
                            fadaptor.notifyDataSetChanged();
                        }
                    });

                } else{

                    fstore.collection("Posts").orderBy("pTime").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            datalist.clear();
                            for (DocumentSnapshot d : queryDocumentSnapshots) {

                                if (d.getString("FullName").toLowerCase().contains(newText.toLowerCase()) || d.getString("Detail").toLowerCase().contains(newText.toLowerCase()) || d.getString("ProjectName").toLowerCase().contains(newText.toLowerCase())) {
                                    Posts obj = d.toObject(Posts.class);
                                    datalist.add(obj);
                                }
                            }
                            recyclerView.setAdapter(fadaptor);
                            fadaptor.notifyDataSetChanged();
                        }
                    });
            }            return false;
            }
        });

    }

    private void searchData(String query) {

    }




}