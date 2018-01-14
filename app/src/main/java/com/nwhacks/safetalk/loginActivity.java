package com.nwhacks.safetalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuthException;
import java.util.ArrayList;

/**
 * Created by Amanda on 2018-01-13.
 */

public class loginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText TextEmail;
    private EditText TextPassword;
    private Button buttonlogin;
    private Button buttonsignup;

    //define firebase auth object (private = only used in this class)
    private FirebaseAuth mAuth;

    public void mainPageRunner() {
        Intent launchMainPage = new Intent(loginActivity.this, MainActivity.class);
        FirebaseUser fireUser = mAuth.getCurrentUser();
        launchMainPage.putExtra("id", fireUser.getUid());
        startActivity(launchMainPage);
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
        mainPageRunner();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_page);


        //initialize firebase auth object
        mAuth = FirebaseAuth.getInstance();


        //initialize views
        TextEmail = (EditText) findViewById(R.id.TextEmail);
        TextPassword = (EditText) findViewById(R.id.TextPassword);
        buttonlogin = (Button) findViewById(R.id.buttonlogin);
        buttonsignup = (Button) findViewById(R.id.buttonsignup);

        buttonsignup.setOnClickListener(this);
        buttonlogin.setOnClickListener(this);
    }

    private void loginUser(){
        String email = TextEmail.getText().toString().trim();
        String password = TextPassword.getText().toString().trim();
    mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                FirebaseUser user = mAuth.getCurrentUser();
                mainPageRunner();

            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(loginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    });


    }
    @Override
    public void onClick(View view) {

       if (view == buttonsignup) {
           Intent launchSignUpPage = new Intent(loginActivity.this, SignUpActivity.class);
           startActivity(launchSignUpPage);
           }

            if (view == buttonlogin) {
                //open login activity when user taps on the already registered textview
               loginUser();
            }
        }
    }




