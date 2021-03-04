package com.example.linkapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkapp.R;
import com.example.linkapp.profile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class bio_link_adp extends RecyclerView.Adapter<bio_link_adp.viewholder> {

    List<String> links;
    Context context;
    LayoutInflater layoutInflater;
    Boolean owner;
    DatabaseReference fref;
    FirebaseAuth mAuth;
    String id;

    public bio_link_adp(List<String> links, Context context, Boolean owner,String id) {
        this.links = links;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.owner = owner;
        this.id=id;
        mAuth=FirebaseAuth.getInstance();
        if(owner){
        fref= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid()).child("bio_links");
        }else{
            fref= FirebaseDatabase.getInstance().getReference().child("users").child(id).child("bio_links");
        }

    }

    @NonNull
    @Override
    public bio_link_adp.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.bio_link,parent,false);
        return new bio_link_adp.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final bio_link_adp.viewholder holder, final int position) {

        fref.child(links.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.tv1.setText(dataSnapshot.child("description").getValue(String.class));
                holder.tv2.setText(dataSnapshot.child("links").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        if(owner){
            holder.cl.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
                    alertDialog.setMessage("Do you want to delete this link?");
                    alertDialog.setTitle("Attention");
                    alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            fref.child(links.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    ((Activity)context).finish();
                                    context.startActivity(((Activity)context).getIntent());
                                }
                            });
                        }
                    });

                    alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    alertDialog.show();
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return links.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        private TextView tv1;
        private TextView tv2;
        private ConstraintLayout cl;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            tv1=itemView.findViewById(R.id.textView22);
            tv2=itemView.findViewById(R.id.textView23);
            cl=itemView.findViewById(R.id.cl);


        }
    }
}
