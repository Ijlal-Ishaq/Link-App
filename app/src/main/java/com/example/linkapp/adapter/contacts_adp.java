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
import com.example.linkapp.contact_profile;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class contacts_adp extends RecyclerView.Adapter<contacts_adp.viewholder> {

    private List<String> contacts;
    private Context context;
    private LayoutInflater layoutInflater;
    private DatabaseReference fref;



    public contacts_adp(List<String> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
        this.layoutInflater=LayoutInflater.from(context);
        fref= FirebaseDatabase.getInstance().getReference().child("users");
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.directories_hs,parent,false);
        return new contacts_adp.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {


        fref.child(contacts.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.name.setText(dataSnapshot.child("name").getValue(String.class));
                Picasso.get().load(dataSnapshot.child("img_url").getValue(String.class)).placeholder(R.color.colorPrimary).into(holder.iv);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, contact_profile.class);
                i.putExtra("id",contacts.get(position));
                context.startActivity(i);
            }
        });




    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private TextView name;
        private ConstraintLayout cl;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            iv=itemView.findViewById(R.id.imageView4);
            name=itemView.findViewById(R.id.textView6);
            cl=itemView.findViewById(R.id.cl);



        }
    }
}
