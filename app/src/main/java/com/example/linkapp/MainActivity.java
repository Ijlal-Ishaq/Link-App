package com.example.linkapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkapp.adapter.directories_adp;
import com.example.linkapp.auth.Sign_in;
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

public class MainActivity extends AppCompatActivity {

    private ImageView iv;
    private RecyclerView rv;


    private List<String> directories=new ArrayList<>();
    private directories_adp adp;

    private ImageView menu;




    private TextView name;
    private TextView about_me;
    private DatabaseReference fref;
    private FirebaseAuth mAuth;

    private ClipboardManager clipboard;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));


        mAuth=FirebaseAuth.getInstance();
        String id=mAuth.getUid();

        menu=findViewById(R.id.imageView2);
        name=findViewById(R.id.textView);
        about_me=findViewById(R.id.textView2);

        iv=findViewById(R.id.imageView);

        fref=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid());


        clipboard = (ClipboardManager)getSystemService(this.CLIPBOARD_SERVICE);





            FirebaseUser user=mAuth.getCurrentUser();
            try {
                name.setText(user.getDisplayName());
                about_me.setText(user.getEmail());
                Picasso.get().load(user.getPhotoUrl()).placeholder(R.color.colorPrimary).into(iv);
            }catch (Exception e){

            }





        rv=findViewById(R.id.rv);



        adp=new directories_adp(directories,this,true);




        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);

        rv.setAdapter(adp);
        rv.setLayoutManager(gridLayoutManager);

        fref.child("directories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try{
                directories.add(0,dataSnapshot.getValue(String.class));
                adp.notifyDataSetChanged();
                }catch(Exception e){

                }
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










        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this,view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.logout:mAuth.signOut();
                                Intent i =new Intent(MainActivity.this, google_login.class);
                                startActivity(i);
                                finish();
                                break;

                            case R.id.key:
                                String string = mAuth.getUid();
                                String  encrypted = null;
                                try {
                                    encrypted = AESUtils.encrypt(string);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                ClipData clip = ClipData.newPlainText("key", encrypted);
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(MainActivity.this,"Copied to clipboard",Toast.LENGTH_SHORT).show();break;


                            case R.id.profile:
                                startActivity(new Intent(MainActivity.this,profile.class));

                            default:

                        }


                        return false;
                    }
                });

                popup.inflate(R.menu.menu);
                popup.show();
            }
        });










    }

    public void goto_contacts(View view) {

        Intent i=new Intent(this, contacts.class);
        startActivity(i);


    }

    public void goto_directories(View view) {
        Intent i=new Intent(this, directories.class);
        startActivity(i);
    }


    public void profile_setting(View view) {
        Intent i=new Intent(this, profile.class);
        startActivity(i);
    }

    private int backpress_count=0;

    @Override
    public void onBackPressed() {

        backpress_count++;
        Toast.makeText(this,"Press again to exit App",Toast.LENGTH_SHORT).show();

        Handler handler =new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                backpress_count=0;
            }
        },1500);

        if(backpress_count>1) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }


    }


}