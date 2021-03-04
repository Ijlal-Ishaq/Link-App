package com.example.linkapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_link_to_bio extends AppCompatActivity {


    private EditText et1;
    private EditText et2;
    private Button btn;

    private FirebaseAuth mAuth;
    private DatabaseReference ferf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_link_to_bio);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));


        mAuth=FirebaseAuth.getInstance();
        ferf=FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid()).child("bio_links");

        et1=findViewById(R.id.et1);
        et2=findViewById(R.id.et2);
        btn=findViewById(R.id.button);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String et1text=et1.getText().toString().trim();
                String et2text=et2.getText().toString().trim();
                if(!et1text.equals("")&&!et2text.equals("")){

                    link_obj obj=new link_obj(et1text,et2text,"","non");
                    String key=ferf.push().getKey();
                    obj.setId(key);

                    ferf.child(key).setValue(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            onBackPressed();
                            finish();
                        }
                    });



                }else{
                    Toast.makeText(add_link_to_bio.this,"All fields are necessary",Toast.LENGTH_SHORT).show();
                }





            }
        });






    }
}