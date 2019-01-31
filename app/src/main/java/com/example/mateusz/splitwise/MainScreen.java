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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {

    private static final String TAG = "";
    private Button buttonBillSplit;
    private EditText editTextBillEmail;
    private EditText editTextBillAmount;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Button buttonViewUserInfo = (Button) findViewById(R.id.buttonViewUserInfo);
        buttonBillSplit = (Button) findViewById(R.id.buttonSplitBill);
        editTextBillAmount = (EditText) findViewById(R.id.editTextBillAmount);
        editTextBillEmail = (EditText) findViewById(R.id.editTextBillEmail);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        buttonViewUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, UserData.class));
            }
        });

        buttonBillSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

    }

    private void updateData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String amountFromEditText = editTextBillAmount.getText().toString();
                String emailFromEditText = editTextBillEmail.getText().toString();
                Double amountFromEditTextAsDouble = Double.valueOf(amountFromEditText);
                if (getAllEmails(dataSnapshot).contains(emailFromEditText)) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String email = ds.child("email").getValue(String.class);
                        Double balance = ds.child("balance").getValue(Double.class);

                        assert email != null;
                        assert firebaseUser != null;
                        if (email.equals(firebaseUser.getEmail())) {
                            Bill bill = new Bill(emailFromEditText, amountFromEditTextAsDouble / 2);
                            double summaryBalance = balance + amountFromEditTextAsDouble / 2;
                            ds.getRef().child("bills").push().setValue(bill);
                            ds.getRef().child("balance").setValue(summaryBalance);
                        }
                    }
                   for(DataSnapshot ds : dataSnapshot.getChildren()){
                       String email = ds.child("email").getValue(String.class);
                       Double balance = ds.child("balance").getValue(Double.class);
                       assert email != null;
                       if(email.equals(emailFromEditText)){
                           Bill bill = new Bill(firebaseUser.getEmail(), (amountFromEditTextAsDouble /2)*-1);
                           double summaryBalance = balance + ((amountFromEditTextAsDouble /2 )*-1);
                           ds.getRef().child("bills").push().setValue(bill);
                           ds.getRef().child("balance").setValue(summaryBalance);
                       }
                   }
                } else {
                    Toast.makeText(MainScreen.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public List<String> getAllEmails(DataSnapshot dataSnapshot) {
        List<String> emails = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String email = ds.child("email").getValue(String.class);
            emails.add(email);
        }
        return emails;
    }
}
