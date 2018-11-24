//LoginActivity
//This class checks user database for user info and verifys them
//Last edit made 11/4/2018 by Jared Bitanga
package com.bitanga.android.lynkactivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

//import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    EditText userPassword;
    EditText userEmail;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Declare instance of firebase
        mAuth = FirebaseAuth.getInstance();

        userPassword = (EditText) findViewById(R.id.login_password);
        userEmail = (EditText) findViewById(R.id.login_mail);
        mProgressBar = (ProgressBar) findViewById((R.id.progress_bar));

        findViewById(R.id.textViewRegister).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);

    }

    private void userLogin() {
        String pass = userPassword.getText().toString().trim();
        String email = userEmail.getText().toString().trim();

        //If the email is empty - displays toasts that email field is null
        if (email.isEmpty()) {
            userEmail.setError("Email is required");
            userEmail.requestFocus();
            return;
        }

        //If email field does not have a domain - toast error is displayed
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError("Email is required");
            userEmail.requestFocus();
            return;
        }


        //If password field is empty - display toast error
        if (pass.isEmpty()) {
            userPassword.setError("Password is required");
            userPassword.requestFocus();
            return;
        }

        //If password length is less than 6 chars - display toast error
        if (userPassword.length() < 6) {
            userPassword.setError("Must be longer than 6 characters");
            userPassword.requestFocus();
            return;
        }

        //Set progress bar visible
        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressBar.setVisibility(View.GONE);
                //Starts new activity if tasks is true (successful)
                if(task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //Clears all activites - opens new activity
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                //Displays toasts if username/password do not match or incorrect
                else {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getBaseContext(), "email or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //If user clicks register switches to RegisterActivity
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewRegister:

                startActivity(new Intent(this, RegisterActivity.class));

                break;

            case R.id.login_button:
                userLogin();
                break;

        }

    }
}

