package com.example.linkapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.linkapp.R;
import com.example.linkapp.directory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class link_adp extends RecyclerView.Adapter<link_adp.viewholder> {

    private List<String> links;
    private LayoutInflater layoutInflater;
    private Context context;
    private DatabaseReference fref;
    private DatabaseReference fref1;
    private DatabaseReference fref3;
    private String owner;
    private FirebaseAuth mAuth;
    private String link_owner;
    private String key;



    public link_adp(List<String> links,  Context context,String key,String owner) {
        this.links = links;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        fref= FirebaseDatabase.getInstance().getReference().child("directories").child(key).child("links");
        fref1= FirebaseDatabase.getInstance().getReference().child("users");
        fref3= FirebaseDatabase.getInstance().getReference().child("directories").child(key);
        this.owner=owner;
        mAuth=FirebaseAuth.getInstance();
        this.key=key;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.links_layout,parent,false);
        return new link_adp.viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position) {


        fref.child(links.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.description.setText("Label: "+dataSnapshot.child("description").getValue(String.class));
                holder.link.setText(dataSnapshot.child("links").getValue(String.class));

                link_owner=dataSnapshot.child("sender_id").getValue(String.class);


                fref1.child(dataSnapshot.child("sender_id").getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        holder.name.setText(dataSnapshot.child("name").getValue(String.class));
                        Picasso.get().load(dataSnapshot.child("img_url").getValue(String.class)).placeholder(R.color.colorPrimaryDark).into(holder.iv);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.cl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if(mAuth.getUid().equals(owner)||mAuth.getUid().equals(link_owner)) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(context);
                    // adb.setView(alertDialogView);
                    adb.setTitle("Do you want to delete this link?\n");

                    //  adb.setIcon(android.R.drawable.ic_dialog_alert);
                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            fref.child(links.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    ((Activity)context).finish();
                                    context.startActivity(((Activity)context).getIntent().putExtra("key",key));

                                }
                            });

                        }
                    });

                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    adb.show();
                }else {
                    Toast.makeText(context,"You cannot delete this link",Toast.LENGTH_LONG).show();


                }




                return false;
            }
        });





    }

    @Override
    public int getItemCount() {
        return links.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView description;
        private TextView link;
        private ImageView iv;
        private ConstraintLayout cl;


        public viewholder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.textView17);
            description=itemView.findViewById(R.id.textView18);
            link=itemView.findViewById(R.id.textView19);
            iv=itemView.findViewById(R.id.imageView7);
            cl=itemView.findViewById(R.id.cl);



        }
    }
}
