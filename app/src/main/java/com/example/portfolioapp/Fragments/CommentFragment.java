package com.example.portfolioapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.portfolioapp.Adaptors.CommentAdaptor;
import com.example.portfolioapp.Models.Comments;
import com.example.portfolioapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CommentFragment extends Fragment {

    ImageButton sendbtn;
    EditText comments;
    Context mcontext;
    String pid,commenteruid;
    FirebaseFirestore fstore;
    FirebaseAuth fauth;
    FirebaseUser fuser;
    String userimage="noImage",username="noName";
    String puid;

    RecyclerView comment_rec;
    CommentAdaptor cadaptor;
    ArrayList<Comments> comment_list;

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comment,container,false);
        requireActivity().setTitle("Comments");

        sendbtn = v.findViewById(R.id.comment_btn);
        comments = v.findViewById(R.id.Comments);
        fstore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        fuser = fauth.getCurrentUser();
        comment_rec = v.findViewById(R.id.comment_rec);
        comment_list = new ArrayList<>();
        commenteruid = fuser.getUid();

        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            pid = bundle.getString("pid",null);
            puid = bundle.getString("useruid",null);
        }





        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postComment();


            }
        });
        loadcomment();



        return v;
    }

    private void postComment() {

        String timestamp = ""+System.currentTimeMillis();


        String txt_comment = comments.getText().toString();

        if(!txt_comment.isEmpty())
        {

            HashMap<String,Object> map = new HashMap<>();

            fstore.collection("users").document(commenteruid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                    if(error!=null)
                        return;
                    if(value.exists())
                    {
                        username = value.getString("Full Name");
                        userimage = value.getString("Image");



                        map.put("timestamp",timestamp);
                        map.put("comment",txt_comment);
                        map.put("uid",commenteruid);
                        map.put("uname",username);
                        map.put("pid",pid);
                        if(userimage.equals("noImage"))
                            map.put("dp","https://firebasestorage.googleapis.com/v0/b/portfolio-app-6f30e.appspot.com/o/Users%2Favatar.jpg?alt=media&token=f342a8f2-bae3-4e23-a87e-401d533bcee8");
                        else
                            map.put("dp",userimage);

                        fstore.collection("Comments").document(pid).collection("user").document(commenteruid+timestamp)
                                .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                comments.setText("");

                                if(!commenteruid.equals(puid))
                                addtonotification(
                                        commenteruid,
                                        pid,
                                        "Commented on Your Post",
                                        userimage,
                                        username
                                        );

                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(mcontext,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                }
            });


                   }

    }

    private void loadcomment() {

        comment_list = new ArrayList<>();


        LinearLayoutManager ll = new LinearLayoutManager(mcontext);
        ll.setStackFromEnd(true);
        ll.setReverseLayout(true);

        comment_rec.setLayoutManager(ll);

        cadaptor = new CommentAdaptor(mcontext,comment_list);


        fstore.collection("Comments").document(pid).collection("user").orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        if(!value.isEmpty())
                        {
                            comment_list.clear();
                            List<DocumentSnapshot> list = value.getDocuments();
                            for(DocumentSnapshot d:list)
                            {
                                Comments obj = d.toObject(Comments.class);
                                comment_list.add(obj);
                            }

                            comment_rec.setAdapter(cadaptor);
                            cadaptor.notifyDataSetChanged();
                        }
                    }
                });

    }


    private void addtonotification(String uid, String pid, String notifications, String userimage, String username) {

        String timestamp = ""+System.currentTimeMillis();


        fstore.collection("Posts").document(pid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                HashMap<Object,String> hashMap = new HashMap<>();
                hashMap.put("pid",pid);
                hashMap.put("timestamp",timestamp);
                hashMap.put("puid",puid);
                hashMap.put("notification",notifications);
                hashMap.put("suid",uid);
                hashMap.put("sname",username);
                hashMap.put("simage",userimage);
                hashMap.put("type","comment");

                fstore.collection("Notifications").document(pid+"comment"+timestamp)
                        .set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
            }
        });



    }


}