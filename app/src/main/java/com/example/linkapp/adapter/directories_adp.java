package com.example.linkapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkapp.R;
import com.example.linkapp.directory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class directories_adp extends RecyclerView.Adapter<directories_adp.viewholder> {

    List<String> directories;
    LayoutInflater layoutInflater;
    private DatabaseReference fref;
    private DatabaseReference fref1;
    private Boolean home_screen=false;
    private Context context;

    public directories_adp(List<String> directories, Context context) {
        this.directories = directories;
        this.layoutInflater=LayoutInflater.from(context);
        this.context=context;

        fref= FirebaseDatabase.getInstance().getReference().child("directories");
        fref1= FirebaseDatabase.getInstance().getReference().child("users");


    }

    public directories_adp(List<String> directories, Context context,Boolean home_screen) {
        this.directories = directories;
        this.layoutInflater=LayoutInflater.from(context);
        this.context=context;
        fref= FirebaseDatabase.getInstance().getReference().child("directories");
        fref1= FirebaseDatabase.getInstance().getReference().child("users");
        this.home_screen=true;


    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(!home_screen){

        View view = layoutInflater.inflate(R.layout.directories_layout,parent,false);
        return new viewholder(view);

        } else {
            View view = layoutInflater.inflate(R.layout.directories_hs,parent,false);
            return new viewholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {

        if(!home_screen) {
            fref.child(directories.get(position)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    holder.name.setText("Name: " + dataSnapshot.child("name").getValue(String.class));
                    holder.discription.setText("Description: " + dataSnapshot.child("discription").getValue(String.class));

                    try {
                        fref1.child(dataSnapshot.child("owner").getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                holder.owner.setText("Owner: " + dataSnapshot.child("name").getValue(String.class));
                                Picasso.get().load(dataSnapshot.child("img_url").getValue(String.class)).placeholder(R.color.colorPrimary).into(holder.iv);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } catch (Exception e) {

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });






        }else {

            fref.child(directories.get(position)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    holder.name.setText(dataSnapshot.child("name").getValue(String.class));

                    try {
                        fref1.child(dataSnapshot.child("owner").getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Picasso.get().load(dataSnapshot.child("img_url").getValue(String.class)).placeholder(R.color.colorPrimary).into(holder.iv);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } catch (Exception e) {

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


        holder.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, directory.class);
                i.putExtra("key",directories.get(position));
                context.startActivity(i);
            }
        });




    }

    @Override
    public int getItemCount() {
        return directories.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        private  ImageView iv;
        private TextView name;
        private TextView discription;
        private TextView owner;
        private ConstraintLayout cl;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            if(!home_screen) {
                iv = itemView.findViewById(R.id.imageView4);
                name = itemView.findViewById(R.id.textView5);
                owner = itemView.findViewById(R.id.textView6);
                discription = itemView.findViewById(R.id.textView13);
                cl=itemView.findViewById(R.id.cl);

            }


            else{
                iv = itemView.findViewById(R.id.imageView4);
                cl=itemView.findViewById(R.id.cl);
                name = itemView.findViewById(R.id.textView6);

            }

        }
    }
}
