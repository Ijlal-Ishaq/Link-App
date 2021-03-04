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

import com.example.linkapp.objects.directory_obj;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_directory extends AppCompatActivity {

    private EditText name;
    private EditText discription;
    private Button create;
    private FirebaseAuth mAuth;
    private DatabaseReference fref;
    private DatabaseReference fref1;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_directory);



        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));


        name=findViewById(R.id.et1);
        discription=findViewById(R.id.et2);
        create=findViewById(R.id.button);

        fref= FirebaseDatabase.getInstance().getReference().child("directories");

        mAuth=FirebaseAuth.getInstance();

         id=mAuth.getUid();

        fref1= FirebaseDatabase.getInstance().getReference().child("users").child(id).child("directories");


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!name.getText().toString().trim().equals("")&&!discription.getText().toString().trim().equals("")){

                    final directory_obj obj =new directory_obj(name.getText().toString().trim(),discription.getText().toString().trim(),"",id,false);

                    final String key=fref.push().getKey();

                    obj.setId(key);

                    if(!key.equals("")&&!id.equals("")){
                        fref.child(key).setValue(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                fref1.child(key).setValue(key).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent i=new Intent(add_directory.this,directories.class);
                                        startActivity(i);
                                        finish();
                                    }
                                });

                            }
                        });
                    }



                }else {
                    Toast.makeText(add_directory.this,"All fields are necessary",Toast.LENGTH_SHORT).show();
                }





            }
        });






    }
}