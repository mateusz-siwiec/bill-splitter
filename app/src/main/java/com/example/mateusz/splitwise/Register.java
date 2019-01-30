package com.example.mateusz.splitwise;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mateusz.splitwise.model.Bill;
import com.example.mateusz.splitwise.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {

    public static final String TAG = "Add to database";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userId;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextRePassword;
    private EditText editTextEmail;
    private Button registerButton;
    private Button skipRegisterButton;
    private DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = (EditText) findViewById(R.id.tvUsername);
        editTextPassword = (EditText) findViewById(R.id.tvPassword);
        editTextRePassword = (EditText) findViewById(R.id.tvRePassword);
        editTextEmail = (EditText) findViewById(R.id.tvEmail);
        registerButton = (Button) findViewById(R.id.buttonRegister);
        skipRegisterButton = (Button) findViewById(R.id.skipRegister);
        mAuth = FirebaseAuth.getInstance();
        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: Submit pressed. ");
                if (validate()) {
                    final String password = editTextPassword.getText().toString().trim();
                    final String email = editTextEmail.getText().toString().trim();
                    final String username = editTextUsername.getText().toString().trim();
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                String uid = firebaseUser.getUid();
                                User user = new User(username, email, 0);
                                databaseUser.child(uid).setValue(user);
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

        String name = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String rePassword = editTextRePassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(rePassword)) {
            Toast.makeText(this, "Passwords aren't identical", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }
}
