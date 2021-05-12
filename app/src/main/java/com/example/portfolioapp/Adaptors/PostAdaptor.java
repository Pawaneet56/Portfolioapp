package com.example.portfolioapp.Adaptors;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.portfolioapp.Fragments.AddPostFragment;
import com.example.portfolioapp.Fragments.CommentFragment;
import com.example.portfolioapp.Fragments.HomeFragment;
import com.example.portfolioapp.Fragments.ProfileFragment;
import com.example.portfolioapp.MainActivity;
import com.example.portfolioapp.Models.Posts;
import com.example.portfolioapp.R;
import com.example.portfolioapp.Startactivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PostAdaptor extends RecyclerView.Adapter<PostAdaptor.myViewHolder> {

    ArrayList<Posts> detalist;
    Context mcontext;
    String myuid;
    ProgressDialog pd;
    FirebaseFirestore fstore;

    public PostAdaptor(ArrayList<Posts> detalist, Context mcontext) {
        this.detalist = detalist;
        this.mcontext = mcontext;
           myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
         pd = new ProgressDialog(mcontext);
         fstore = FirebaseFirestore.getInstance();
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
        final int[] plikes = {detalist.get(position).getpLike()};
        String likes = Integer.toString(plikes[0]);

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(Long.parseLong(time));
        String ptime = DateFormat.format("dd/MM/yyyy hh:mm:aa", cal).toString();


        holder.username.setText(uname);
        holder.projectname.setText(ptitle);
        holder.details.setText(pdetails);
        holder.ptime.setText(ptime);


        setlikes(holder,pid,plikes);

        if (uimage.equals("noImage")) {
            try {
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/portfolio-app-6f30e.appspot.com/o/Users%2Favatar.jpg?alt=media&token=f342a8f2-bae3-4e23-a87e-401d533bcee8").into(holder.userpic);

            }catch(Exception e){}
        }
        else
        {
            try{
                Picasso.get().load(uimage).fit().centerCrop(-10).into(holder.userpic);
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

                    showoptions(holder.threedot,uid,pid,pimage,myuid,position);
                }
            });

        }


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fstore.collection("Posts").whereEqualTo("pid",pid)
                        .whereArrayContains("Likes",myuid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty())
                        {
                            fstore.collection("Posts").document(pid)
                                    .update("pLike",FieldValue.increment(1));
                            fstore.collection("Posts").document(pid)
                                    .update("Likes",FieldValue.arrayUnion(myuid));

                            holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked,0,0,0);
                            plikes[0]++;
                            if(plikes[0]==1)
                            {
                                holder.like.setText(Integer.toString(plikes[0])+" Like");
                            }
                            else
                            {
                                holder.like.setText(Integer.toString(plikes[0])+" Likes");
                            }


                            addtonotification(uid,pid,"Liked Your Post",uimage,uname);

                        }
                        else
                        {
                            fstore.collection("Posts").document(pid)
                                    .update("pLike", FieldValue.increment(-1));
                            fstore.collection("Posts").document(pid)
                                    .update("Likes",FieldValue.arrayRemove(myuid));

                            holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like,0,0,0);
                            plikes[0]--;
                            if(plikes[0]==0 || plikes[0]==1)
                            {
                                holder.like.setText(Integer.toString(plikes[0])+" Like");
                            }
                            else
                            {
                                holder.like.setText(Integer.toString(plikes[0])+" Likes");
                            }

                        }
                    }
                });
            }
        });



        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("pid",pid);
                bundle.putString("useruid",myuid);
                CommentFragment fragment  = new CommentFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((MainActivity)mcontext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,fragment);
                fragmentTransaction.addToBackStack(null).commit();
            }
        });



        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pimage.equals("noImage"))
                {
                    String sharebody = ptitle+"\n"+pdetails;

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
                    i.putExtra(Intent.EXTRA_TEXT,sharebody);
                    mcontext.startActivity(Intent.createChooser(i,"Share Via"));
                }
                else
                {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable)holder.projectpic.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();

                    String sharebody = ptitle+"\n"+pdetails;

                    File imageFolder = new File(mcontext.getCacheDir(),"images");
                    Uri uri = null;

                    try{
                        imageFolder.mkdirs();

                        File file = new File(imageFolder,"shared_image.png");
                        FileOutputStream stream = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.PNG,90,stream);
                        stream.flush();
                        stream.close();

                        uri = FileProvider.getUriForFile(mcontext,"com.example.portfolioapp.fileprovider",file);

                    }catch (Exception e)
                    {

                    }


                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_STREAM,uri);
                    i.putExtra(Intent.EXTRA_TEXT,sharebody);
                    i.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
                    i.setType("image/png");

                    mcontext.startActivity(Intent.createChooser(i,"Share Via"));

                }

            }
        });

        holder.userpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("TheirProfile","true");
                bundle.putString("uid",uid);

                ProfileFragment fragment = new ProfileFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((MainActivity)mcontext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,fragment);
                fragmentTransaction.addToBackStack(null).commit();

            }
        });

    }

    private void addtonotification(String hisuid,String pid,String notification,String image,String name)
    {
        String timestamp = ""+System.currentTimeMillis();

        HashMap<Object,String> hashMap = new HashMap<>();
        hashMap.put("pid",pid);
        hashMap.put("timestamp",timestamp);
        hashMap.put("puid",hisuid);
        hashMap.put("notification",notification);
        hashMap.put("suid",myuid);
        hashMap.put("sname",name);
        hashMap.put("simage",image);
        hashMap.put("type","like");

        fstore.collection("Notifications").document(pid+"like"+timestamp)
                .set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });

    }




    //to set the field of like
    private void setlikes(myViewHolder holder, String pid,int[] plikes) {

        fstore.collection("Posts").whereEqualTo("pid",pid)
                .whereArrayContains("Likes",myuid).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty())
                {
                    holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like,0,0,0);
                    if(plikes[0]==0 || plikes[0]==1)
                    {
                        holder.like.setText(Integer.toString(plikes[0])+" Like");
                    }
                    else
                    {
                        holder.like.setText(Integer.toString(plikes[0])+" Likes");
                    }
                }
                else
                {
                    holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked,0,0,0);
                    if(plikes[0]==0 || plikes[0]==1)
                    {
                        holder.like.setText(Integer.toString(plikes[0])+" Like");
                    }
                    else
                    {
                        holder.like.setText(Integer.toString(plikes[0])+" Likes");
                    }
                }
            }
        });
    }

//options when you click on the threedot
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showoptions(ImageButton threedot, String uid, String pid, String pimage, String myuid, int position) {

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
                    begindelete(pid,pimage,detalist,position);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                    builder.setMessage("Are you sure you want to Edit this project ?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

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
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();

                }
                return false;
            }
        });

        pop.show();
    }



    private void begindelete(String pid, String pimage,ArrayList<Posts> data,int position) {
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
                    deletepost(pid,data,position);
                }
                else
                {
                    StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(pimage);
                    picRef.delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    deletepost(pid,data,position);
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


    private void deletepost(String pid,ArrayList<Posts> data,int position) {
        FirebaseFirestore.getInstance().collection("Posts").document(pid).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        Toast.makeText(mcontext,"Project Deleted Successfully",Toast.LENGTH_SHORT).show();
                        data.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,data.size());
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
            //totlikes = itemView.findViewById(R.id.Llikes);
            threedot = itemView.findViewById(R.id.lthreedot);
            like = itemView.findViewById(R.id.likebtn);
            comment = itemView.findViewById(R.id.commentbtn);
            share = itemView.findViewById(R.id.sharebtn);
        }
    }


}
