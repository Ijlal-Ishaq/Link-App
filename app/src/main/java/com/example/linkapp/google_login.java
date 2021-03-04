package com.example.linkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.linkapp.objects.user_obj;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class google_login extends AppCompatActivity {

    private static final String TAG = "fail";
    private static final int RC_SIGN_IN = 333;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private GoogleSignInClient googleSignInClient;
    private DatabaseReference fref= FirebaseDatabase.getInstance().getReference().child("users");

    private Button sign_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);


        sign_in=findViewById(R.id.button2);


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient= GoogleSignIn.getClient(google_login.this, gso);


    }

    private void signIn() {


        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();



                           if( update_info() ==1){
                               updateUI();
                           }



                        } else {
                            // If sign in fails, display a message to the user.

                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private int update_info() {

        final FirebaseUser user =mAuth.getCurrentUser();

        try{

        fref.child(user.getUid()).child("id").setValue(user.getUid());
        fref.child(user.getUid()).child("name").setValue(user.getDisplayName());
        fref.child(user.getUid()).child("email").setValue(user.getEmail());
        fref.child(user.getUid()).child("img_url").setValue(user.getPhotoUrl().toString());




        return 1;

       }catch (Exception e){
           return 0;
       }



    }

    private void updateUI() {


        startActivity(new Intent(google_login.this,MainActivity.class));
        finish();


    }

    public void onBackPressed(View view) {
        super.onBackPressed();
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!=null){
            update_info();
            startActivity(new Intent(google_login.this,MainActivity.class));
            finish();
        }

    }
}