package com.example.linkapp.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.linkapp.MainActivity;
import com.example.linkapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_in extends AppCompatActivity {


    private EditText email;
    private EditText password;
    private Button sign_in;
    private FirebaseAuth mauth=FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));



        email=findViewById(R.id.et1);
        password=findViewById(R.id.et2);
        sign_in=findViewById(R.id.button);


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!email.getText().toString().trim().equals("")&&!password.getText().toString().equals("")){

                    mauth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            if(authResult.getUser().isEmailVerified()){
                            Intent i=new Intent(Sign_in.this, MainActivity.class);
                            startActivity(i);
                            }else{

                                authResult.getUser().sendEmailVerification();
                                Toast.makeText(Sign_in.this,"Account not Verified",Toast.LENGTH_SHORT);
                                Dialog dialog =new Dialog(Sign_in.this);
                                dialog.setContentView(R.layout.dialog);
                                dialog.show();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Sign_in.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Toast.makeText(Sign_in.this,"All fields are necessary",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        FirebaseUser user=mauth.getCurrentUser();

        if(user!=null){
            if(user.isEmailVerified()){
                Intent i=new Intent(Sign_in.this, MainActivity.class);
                startActivity(i);
            }
        }

        super.onStart();

    }

    public void goto_sign_up(View view) {

        Intent i=new Intent(this,Sign_up.class);
        startActivity(i);
        finish();
    }
}