package com.example.linkapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkapp.adapter.bio_link_adp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class profile extends AppCompatActivity {



    private TextView name;
    private TextView email;
    private ImageView profile_pic;
    private ImageView menu;
    private ClipboardManager clipboard;
    private FirebaseAuth mAuth;
    private DatabaseReference ferf;
    private RecyclerView rv;
    private List<String> links=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));


        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        ferf= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid()).child("bio_links");

        rv=findViewById(R.id.rv);

        name=findViewById(R.id.textView);
        email=findViewById(R.id.textView2);
        profile_pic=findViewById(R.id.imageView);
        menu=findViewById(R.id.imageView2);


        clipboard = (ClipboardManager)getSystemService(this.CLIPBOARD_SERVICE);


        name.setText(user.getDisplayName());
        email.setText(user.getEmail());
        Picasso.get().load(user.getPhotoUrl()).placeholder(R.color.colorAccent).into(profile_pic);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(profile.this,view);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        String string = mAuth.getUid();
                        String  encrypted = null;
                        try {
                            encrypted = AESUtils.encrypt(string);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        ClipData clip = ClipData.newPlainText("key", encrypted);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(profile.this,"Copied to clipboard",Toast.LENGTH_SHORT).show();


                        return false;
                    }
                });

                popup.inflate(R.menu.menu_profile);
                popup.show();
            }
        });




        final bio_link_adp adp =new bio_link_adp(links,this,true,"non");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);


        rv.setAdapter(adp);
        rv.setLayoutManager(linearLayoutManager);



        ferf.addChildEventListener(new ChildEventListener() {
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


    public void goto_add_link(View view) {
        startActivity(new Intent(profile.this,add_link_to_bio.class));

    }
}