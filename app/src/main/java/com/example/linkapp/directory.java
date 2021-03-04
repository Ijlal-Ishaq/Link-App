package com.example.linkapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkapp.adapter.link_adp;
import com.example.linkapp.auth.Sign_in;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class directory extends AppCompatActivity {

    private TextView name;
    private ImageView menu;
    private String key;
    private DatabaseReference fref;
    private DatabaseReference fref1;
    private DatabaseReference fref3;
    private FirebaseAuth mAuth;
    private String owner;
    private String owner_name;
    private String description;
    private Boolean post_status=false;
    private List<String> links=new ArrayList<>();
    private RecyclerView rv;
    private link_adp adp;
    private ClipboardManager clipboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));


        name=findViewById(R.id.textView15);
        menu=findViewById(R.id.imageView6);


        key=getIntent().getStringExtra("key");


        rv=findViewById(R.id.rv);



        mAuth=FirebaseAuth.getInstance();
        fref= FirebaseDatabase.getInstance().getReference().child("directories").child(key);
        fref1= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid()).child("directories");
        fref3= FirebaseDatabase.getInstance().getReference().child("users");


        clipboard = (ClipboardManager)getSystemService(this.CLIPBOARD_SERVICE);




        fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue(String.class));
                owner=dataSnapshot.child("owner").getValue(String.class);
                description=dataSnapshot.child("discription").getValue(String.class);
                post_status=dataSnapshot.child("post_status").getValue(Boolean.class);


                fref3.child(owner).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        owner_name=dataSnapshot.child("name").getValue(String.class);
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


        adp=new link_adp(links,this,key,owner);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adp);


        fref.child("links").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                links.add(dataSnapshot.child("id").getValue(String.class));
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




        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(directory.this,view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.add_link : {

                                if (mAuth.getUid().equals(owner)) {

                                    Intent i = new Intent(directory.this, add_link.class);
                                    i.putExtra("key", key);
                                    startActivity(i);

                                } else {
                                    if (post_status) {

                                        Intent i = new Intent(directory.this, add_link.class);
                                        i.putExtra("key", key);
                                        startActivity(i);

                                    } else {
                                        Toast.makeText(directory.this, "Owner doesn't allow you to add links", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                break;
                            }


                            case R.id.anyone:

                                fref.child("post_status").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(directory.this,"Anyone can add links",Toast.LENGTH_SHORT).show();
                                    }
                                });

                                break;

                            case R.id.only_me:
                                fref.child("post_status").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(directory.this,"Only you can add links",Toast.LENGTH_SHORT).show();
                                    }
                                });

                                break;

                            case R.id.copy:




                                try {

                                    String string = key;
                                  String  encrypted = AESUtils.encrypt(string);

                                    ClipData clip = ClipData.newPlainText("key", encrypted);
                                    clipboard.setPrimaryClip(clip);
                                    Toast.makeText(directory.this,"Copied to clipboard",Toast.LENGTH_SHORT).show();



                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(directory.this,"Cannot Copy Error",Toast.LENGTH_SHORT).show();
                                }

                                break;

                            case R.id.remove:

                                if(mAuth.getUid().equals(owner)){
                                    AlertDialog.Builder adb = new AlertDialog.Builder(directory.this);
                                   // adb.setView(alertDialogView);
                                    adb.setTitle("Do you want to remove this directory from your list?");
                                  //  adb.setIcon(android.R.drawable.ic_dialog_alert);
                                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            fref1.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(directory.this,"Directory Removed",Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(directory.this, MainActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            });


                                        }
                                    });
                                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    adb.show();

                                }
                                break;

                            case R.id.details:

                                AlertDialog.Builder adb = new AlertDialog.Builder(directory.this);
                                // adb.setView(alertDialogView);
                                adb.setTitle("Details:\n");
                                if(post_status){
                                adb.setMessage("Directory name: "+name.getText()+"\n\n"+
                                                "Owner: "+owner_name+"\n\n" +
                                                "Description: "+description+"\n\n"+
                                                "Posting Status: Anyone can add links");
                                }else {
                                    adb.setMessage("Directory name: "+name.getText()+"\n\n"+
                                            "Owner: "+owner_name+"\n\n" +
                                            "Description: "+description+"\n\n"+
                                            "Posting status: Only owner can add links");
                                }

                                //  adb.setIcon(android.R.drawable.ic_dialog_alert);
                                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                });

                                adb.show();




                            default: break;
                        }


                        return false;
                    }
                });

                if(owner.equals(mAuth.getUid())){
                     popup.inflate(R.menu.directory_menu1);
                }else{
                    popup.inflate(R.menu.directory_menu);
                }

                popup.show();
            }
        });







    }


}



