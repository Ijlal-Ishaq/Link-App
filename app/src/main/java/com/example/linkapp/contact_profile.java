package com.example.linkapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.linkapp.adapter.bio_link_adp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class contact_profile extends AppCompatActivity {


    private TextView name;
    private TextView email;
    private ImageView profile_pic;
    private DatabaseReference ferf;
    private RecyclerView rv;
    private List<String> links=new ArrayList<>();
    private String id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));


        id=getIntent().getStringExtra("id");

        ferf= FirebaseDatabase.getInstance().getReference().child("users").child(id);

        rv=findViewById(R.id.rv);

        name=findViewById(R.id.textView);
        email=findViewById(R.id.textView2);
        profile_pic=findViewById(R.id.imageView);


        ferf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue(String.class));
                email.setText(dataSnapshot.child("email").getValue(String.class));
                Picasso.get().load(dataSnapshot.child("img_url").getValue(String.class)).into(profile_pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final bio_link_adp adp=new bio_link_adp(links,this,false,id);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        rv.setAdapter(adp);
        rv.setLayoutManager(linearLayoutManager);




        ferf.child("bio_links").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                links.add(0,dataSnapshot.child("id").getValue(String.class));
                adp.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}