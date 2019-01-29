package com.example.mateusz.splitwise;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    public static final String TAG = "Add to database";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userId;
    private EditText tvUsername;
    private EditText tvPassword;
    private EditText tvRePassword;
    private EditText tvEmail;
    private Button registerButton;
    private Button skipRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvUsername = (EditText) findViewById(R.id.tvUsername);
        tvPassword = (EditText) findViewById(R.id.tvPassword);
        tvRePassword = (EditText) findViewById(R.id.tvRePassword);
        tvEmail = (EditText) findViewById(R.id.tvEmail);
        registerButton = (Button) findViewById(R.id.buttonRegister);
        skipRegisterButton = (Button) findViewById(R.id.skipRegister);
        mAuth = FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: Submit pressed. ");
                if (validate()) {
                    final String password = tvPassword.getText().toString().trim();
                    final String email = tvEmail.getText().toString().trim();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(Register.this, "Registration successfull", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this, Login.class));
                                mAuth.signOut();
                            } else {
                                Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        skipRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                mAuth.signOut();
            }
        });


    }
    private Boolean validate() {
        Boolean result = false;

        String name = tvUsername.getText().toString();
        String email = tvEmail.getText().toString();
        String password = tvPassword.getText().toString();
        String rePassword = tvRePassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else if(!password.equals(rePassword)) {
            Toast.makeText(this, "Passwords aren't identical", Toast.LENGTH_SHORT).show();
        }else {
            result = true;
        }
        return result;
    }
}
