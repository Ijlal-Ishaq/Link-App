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

import com.example.linkapp.adapter.directories_adp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class directories extends AppCompatActivity {


    private RecyclerView rv;
    private FirebaseAuth mAuth;
    private DatabaseReference fref;
    private List<String> directories=new ArrayList<>();
    private directories_adp adp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directories);



        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));





        mAuth=FirebaseAuth.getInstance();
        fref= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid()).child("directories");

        rv=findViewById(R.id.rv);

        adp=new directories_adp(directories,this);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);

        rv.setAdapter(adp);
        rv.setLayoutManager(gridLayoutManager);


        fref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                directories.add(0,dataSnapshot.getValue(String.class));
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

    public void goto_adddirectory(View view) {
        Intent i=new Intent(directories.this,add_directory.class);
        startActivity(i);

    }

    public void goto_searchdirectory(View view) {
        Intent i=new Intent(directories.this,search_directoory.class);
        startActivity(i);

    }


}