package com.example.portfolioapp.Adaptors;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfolioapp.Fragments.AddPostFragment;
import com.example.portfolioapp.Fragments.HomeFragment;
import com.example.portfolioapp.MainActivity;
import com.example.portfolioapp.Models.Posts;
import com.example.portfolioapp.R;
import com.example.portfolioapp.Startactivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PostAdaptor extends RecyclerView.Adapter<PostAdaptor.myViewHolder> {

    ArrayList<Posts> detalist;
    Context mcontext;
    String myuid;
    ProgressDialog pd;



    public PostAdaptor(ArrayList<Posts> detalist, Context mcontext) {
        this.detalist = detalist;
        this.mcontext = mcontext;
           myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
         pd = new ProgressDialog(mcontext);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        String uid = detalist.get(position).getId();
        String uname = detalist.get(position).getFullName();
        String uimage = detalist.get(position).getUserImage();
        String pid = detalist.get(position).getPid();
        String ptitle = detalist.get(position).getProjectName();
        String pdetails = detalist.get(position).getDetail();
        String pimage = detalist.get(position).getPostImage();
        String time = detalist.get(position).getpTime();

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(Long.parseLong(time));
        String ptime = DateFormat.format("dd/MM/yyyy hh:mm:aa", cal).toString();


        holder.username.setText(uname);
        holder.projectname.setText(ptitle);
        holder.details.setText(pdetails);
        holder.ptime.setText(ptime);


        if (uimage.equals("noImage")) {
            try {
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/portfolio-app-6f30e.appspot.com/o/Users%2Favatar.jpg?alt=media&token=f342a8f2-bae3-4e23-a87e-401d533bcee8").into(holder.userpic);

            }catch(Exception e){}
        }
        else
        {
            try{
                Picasso.get().load(uimage).into(holder.userpic);
            }catch(Exception e){}
        }



            if(pimage.equals("noImage"))
            {
                holder.projectpic.setVisibility(View.GONE);
            }
            else
            {
                holder.projectpic.setVisibility(View.VISIBLE);
                try{
                Picasso.get().load(pimage).into(holder.projectpic);
                }catch (Exception e){}
            }

        if(!uid.equals(myuid))
        {
            holder.threedot.setVisibility(View.GONE);
        }
        else
        {
            holder.threedot.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {

                    showoptions(holder.threedot,uid,pid,pimage,myuid);
                }
            });

        }


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext,"Like",Toast.LENGTH_SHORT).show();
            }
        });


        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext,"comment",Toast.LENGTH_SHORT).show();
            }
        });



        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext,"share",Toast.LENGTH_SHORT).show();
            }
        });

    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showoptions(ImageButton threedot, String uid, String pid, String pimage, String myuid) {

        PopupMenu pop  = new PopupMenu(mcontext,threedot, Gravity.END);


        if(uid.equals(myuid))
        {
            pop.getMenu().add(Menu.NONE,0,0,"Delete");
            pop.getMenu().add(Menu.NONE,1,0,"Edit");

        }


        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if(id==0)
                {
                    begindelete(pid,pimage);
                }
                else
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("key","editPost");
                    bundle.putString("editPostid",pid);

                    AddPostFragment fragment = new AddPostFragment();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = ((MainActivity)mcontext).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment,fragment);
                    fragmentTransaction.addToBackStack(null).commit();
                }
                return false;
            }
        });

        pop.show();

    }

    private void begindelete(String pid, String pimage) {


        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setMessage("Are you sure you want to DELETE this Project ?");
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                pd.setMessage("Deleting...");
                pd.setCancelable(false);
                pd.show();

                if(pimage.equals("noImage"))
                {
                    deletepost(pid);
                }
                else
                {
                    StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(pimage);
                    picRef.delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    deletepost(pid);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(mcontext,"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                }


            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();




    }

    private void deletepost(String pid) {
        FirebaseFirestore.getInstance().collection("Posts").document(pid).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        Toast.makeText(mcontext,"Project Deleted Successfully",Toast.LENGTH_SHORT).show();
                        MainActivity act = (MainActivity)mcontext;
                        Fragment f = new HomeFragment();
                        act.getSupportFragmentManager().beginTransaction().replace(R.id.fragment,f).addToBackStack(null).commit();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(mcontext,"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }



    @Override
    public int getItemCount() {
        return detalist.size();
    }




    class myViewHolder extends RecyclerView.ViewHolder{

        TextView username,projectname,details,ptime,totlikes;
        ImageButton threedot;
        Button like,comment,share;
        ImageView userpic,projectpic;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.luname);
            projectname = itemView.findViewById(R.id.lproject_name);
            details = itemView.findViewById(R.id.ldeatils);
            userpic = itemView.findViewById(R.id.luserpic);
            projectpic = itemView.findViewById(R.id.lprojectpic);
            ptime = itemView.findViewById(R.id.ltime);
            totlikes = itemView.findViewById(R.id.Llikes);
            threedot = itemView.findViewById(R.id.lthreedot);
            like = itemView.findViewById(R.id.likebtn);
            comment = itemView.findViewById(R.id.commentbtn);
            share = itemView.findViewById(R.id.sharebtn);
        }
    }
}