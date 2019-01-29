package com.example.mateusz.splitwise;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserData extends AppCompatActivity {

    private static final String TAG = "Hello";
    private TextView username;
    private TextView emailTextView;
    private TextView balance;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        username = (TextView) findViewById(R.id.textViewUsername);
        emailTextView = (TextView) findViewById(R.id.textViewEmail);
        balance = (TextView) findViewById(R.id.textViewBalance);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String email = ds.child("email").getValue(String.class);
                    String usernameFromDatabase = ds.child("username").getValue(String.class);
                    String emailFromDatabase = ds.child("email").getValue(String.class);
                    Integer balanceFromDatabase = ds.child("balance").getValue(Integer.class);
                    assert email != null;
                    assert user != null;
                    if (email.equals(user.getEmail())) {
                        username.setText(usernameFromDatabase);
                        emailTextView.setText(emailFromDatabase);
                        balance.setText(balanceFromDatabase.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
