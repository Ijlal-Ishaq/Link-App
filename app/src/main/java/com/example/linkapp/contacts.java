package com.example.linkapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.linkapp.adapter.contacts_adp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class contacts extends AppCompatActivity {

    private RecyclerView rv;
    private contacts_adp adp;
    private List<String> contacts=new ArrayList<>();
    private DatabaseReference fref;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));



        mAuth=FirebaseAuth.getInstance();
        fref= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid()).child("contacts");


        rv=findViewById(R.id.rv);

        adp=new contacts_adp(contacts,this);

        rv.setAdapter(adp);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);

        rv.setLayoutManager(gridLayoutManager);

        fref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                contacts.add(0,dataSnapshot.getValue(String.class));
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

    public void goto_addcontact(View view) {
        Intent i=new Intent(contacts.this,add_contact.class);
        startActivity(i);
    }


}