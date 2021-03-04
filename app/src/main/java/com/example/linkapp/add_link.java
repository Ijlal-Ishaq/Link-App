package com.example.linkapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.linkapp.objects.link_obj;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_link extends AppCompatActivity {



    private EditText description;
    private EditText link;
    private Button add;
    private DatabaseReference fref;
    private FirebaseAuth mAuth;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_link);


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));


        description=findViewById(R.id.et1);
        link=findViewById(R.id.et2);
        add=findViewById(R.id.button);

        key=getIntent().getStringExtra("key");

        fref= FirebaseDatabase.getInstance().getReference().child("directories").child(key);
        mAuth= FirebaseAuth.getInstance();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!description.getText().toString().trim().equals("")&&!link.getText().toString().trim().equals("")){

                    link_obj obj=new link_obj(description.getText().toString().trim(),link.getText().toString().trim(),"",mAuth.getUid());
                    String id=fref.child("links").push().getKey();
                    obj.setId(id);
                    fref.child("links").child(id).setValue(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            onBackPressed();
                            finish();
                        }
                    });


                }else{
                    Toast.makeText(add_link.this,"All fields are necessary",Toast.LENGTH_SHORT).show();
                }



            }
        });












    }
}