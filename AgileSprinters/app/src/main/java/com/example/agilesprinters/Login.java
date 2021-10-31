package com.example.agilesprinters;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class Login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView register;
        Button login;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(this);

        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }


    private void signIn() {

        // get the email and password from respective fields
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {

                            if (email.equals("")){
                                errMsg(task,"Email field is empty");
                            }

                            else if (password.equals("")){
                               errMsg(task,"Password field is empty");
                            }
                            else {
                                // If sign in fails, display a message to the user.
                                errMsg(task,"Email or password entered is incorrect");
                            }
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void errMsg(@NonNull Task<AuthResult> task, String errStr){
        Log.w(TAG, "signInWithEmail:failure", task.getException());
        Toast.makeText(Login.this, errStr,
                Toast.LENGTH_SHORT).show();
        updateUI(null);
    }

    // function switches to the home page
    private void reload() {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

    private void updateUI(FirebaseUser user) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId()){
            case R.id.register:
                intent = new Intent(Login.this, Register.class);
                break;
            case R.id.login:
                signIn();
                break;
            default:
                break;
        }
        if (null!=intent) startActivity(intent);
    }

}