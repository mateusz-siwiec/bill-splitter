package com.example.mateusz.splitwise;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class UserData extends AppCompatActivity {

    private static final String TAG = "Hello";
    private TextView username;
    private TextView emailTextView;
    private TextView balance;
    private FirebaseAuth mAuth;
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        username = (TextView) findViewById(R.id.textViewUsername);
        emailTextView = (TextView) findViewById(R.id.textViewEmail);
        balance = (TextView) findViewById(R.id.textViewBalance);
        listView = (ListView) findViewById(R.id.billList);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String email = ds.child("email").getValue(String.class);
                    String usernameFromDatabase = ds.child("username").getValue(String.class);
                    String emailFromDatabase = ds.child("email").getValue(String.class);
                    Double balanceFromDatabase = ds.child("balance").getValue(Double.class);
                    assert email != null;
                    assert user != null;
                    if (email.equals(user.getEmail())) {
                        username.setText(usernameFromDatabase);
                        emailTextView.setText(emailFromDatabase);
                        balance.setText(balanceFromDatabase.toString() + " zł");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String email = ds.child("email").getValue(String.class);
                    assert user != null;
                    assert email != null;
                    if (email.equals(user.getEmail())) {
                        DataSnapshot billsSnapshot = ds.child("bills");
                        for (DataSnapshot bills : billsSnapshot.getChildren()) {
                            String billEmail = bills.child("email").getValue(String.class);
                            Double billAmount = bills.child("amount").getValue(Double.class);
                            String singleBill = billEmail + " " + billAmount;
                            arrayList.add(singleBill);
                        }
                    }
                }
                arrayAdapter = new ArrayAdapter<String>(UserData.this, android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(arrayAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
