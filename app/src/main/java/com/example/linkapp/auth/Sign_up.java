package com.example.linkapp.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linkapp.R;
import com.example.linkapp.objects.user_obj;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_up extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button sign_up;
    private EditText email;
    private EditText name;
    private EditText password;
    private EditText confirm_password;
    private EditText sentence;
    private DatabaseReference fref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));


        sign_up=findViewById(R.id.button);
        email=findViewById(R.id.et1);
        name=findViewById(R.id.et5);
        sentence=findViewById(R.id.et2);
        password=findViewById(R.id.et3);
        confirm_password=findViewById(R.id.et4);
        mAuth = FirebaseAuth.getInstance();
        fref= FirebaseDatabase.getInstance().getReference().child("users");




        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(password.getText().toString().trim().equals(confirm_password.getText().toString().trim())){

                    if(!email.getText().toString().trim().equals("")&&!sentence.getText().toString().trim().equals("")&&!password.getText().toString().trim().equals("")&&!password.getText().toString().trim().equals("")&&!name.getText().toString().trim().equals(""))
                    {
                        mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                authResult.getUser().sendEmailVerification();
                                user_obj obj =new user_obj(authResult.getUser().getUid().toString().trim(),name.getText().toString().trim(),authResult.getUser().getEmail().toString().trim());


                                fref.child(authResult.getUser().getUid().toString().trim()).setValue(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Sign_up.this,"Task Successful",Toast.LENGTH_SHORT).show();


                                        Dialog dialog =new Dialog(Sign_up.this);
                                        dialog.setContentView(R.layout.dialog);
                                        dialog.show();
                                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                Intent i=new Intent(Sign_up.this,Sign_in.class);
                                                startActivity(i);
                                            }
                                        });
                                    }
                                });





                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Sign_up.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });


                    }else{
                        Toast.makeText(Sign_up.this,"All fields are necessary",Toast.LENGTH_SHORT).show();
                    }



                }else{

                    Toast.makeText(Sign_up.this,"Confirm password doesn't match",Toast.LENGTH_SHORT).show();
                }





            }
        });



    }
}