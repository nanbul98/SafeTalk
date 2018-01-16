package com.nwhacks.safetalk;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by nandinibulusu on 2018-01-13.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPhoneNumber;
    private EditText editTextName;
    private Button buttonSignUp;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_page);

        mAuth = FirebaseAuth.getInstance();

        //initializing views
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        editTextPhoneNumber = (EditText)findViewById(R.id.editTextPhoneNumber);
        editTextName = (EditText)findViewById(R.id.editTextName);

        buttonSignUp = (Button)findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void registerUser() {

        //getting email and password
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this,"Please enter email", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Please enter password", Toast.LENGTH_LONG).show();
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this,"Please enter your full name", Toast.LENGTH_LONG).show();
        }



        //creating a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //does something
                            Toast.makeText(SignUpActivity.this,"You have successfully registered!", Toast.LENGTH_LONG).show();
                            FirebaseUser fireUser = mAuth.getCurrentUser();
                            User user = new User(fireUser, new ArrayList<User>(), phoneNumber, null, name);
                            Intent launchMainPage = new Intent(SignUpActivity.this, MainActivity.class);
                            launchMainPage.putExtra("id", user.getUserId());
                            startActivity(launchMainPage);
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException)task.getException();
                            //does something else
                            Toast.makeText(SignUpActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } );

        }

    @Override
    public void onClick(View view) {
        //calling register method on click
        registerUser();
    }


    }

