package com.example.linkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class add_contact extends AppCompatActivity {


    private EditText key;
    private Button add;
    private DatabaseReference fref;
    private DatabaseReference fref1;
    private FirebaseAuth mAuth;
    private int count=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));





        key=findViewById(R.id.et1);
        add=findViewById(R.id.button);

        fref= FirebaseDatabase.getInstance().getReference().child("users");
        fref1= FirebaseDatabase.getInstance().getReference().child("users");
        mAuth= FirebaseAuth.getInstance();




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!key.getText().toString().trim().equals("")){

//                    Handler handler=new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            if(count<1){
//                                Toast.makeText(add_contact.this,"No Network",Toast.LENGTH_SHORT).show();
//                            }
//                            count=0;
//
//                        }
//                    },3000);


                    try {


                        final String string =AESUtils.decrypt(key.getText().toString().trim());


                        fref.child(string).addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.child("id").exists()){

                                    fref1.child(mAuth.getUid()).child("contacts").child(string).setValue(string).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            count++;
                                            onBackPressed();
                                        }
                                    });



                                }else {
                                    count++;
                                    Toast.makeText(add_contact.this, "Contact not found", Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {




                            }





                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(add_contact.this, "Contact not found", Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(add_contact.this,"Enter Key",Toast.LENGTH_SHORT).show();
                }
            }
        });







    }
}