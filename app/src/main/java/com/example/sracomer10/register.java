package com.example.sracomer10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.FrameStats;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class register extends AppCompatActivity {


    EditText mFirstname,mLastname,mEmail,mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressbar;
    FirebaseFirestore fStore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mFirstname = findViewById(R.id.fname);
        mLastname = findViewById(R.id.lname);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.passwordfield);
        mPhone = findViewById(R.id.Phonenumber);
        mRegisterBtn = findViewById(R.id.registerbutton);
        mLoginBtn = findViewById(R.id.alreadyregistered);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressbar = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String fname = mFirstname.getText().toString();
                final String lname = mLastname.getText().toString();
                final String phonenumber = mPhone.getText().toString();



                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is invalid");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is not set");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password must be greater >= 6 characters");
                    return;
                }

                progressbar.setVisibility(View.VISIBLE);

                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(register.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("FName",fname);
                            user.put("LName",lname);
                            user.put("email",email);
                            user.put("phone",phonenumber);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","onSuccess: user Profile is created for " + userID);
                                }
                            });


                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        } else {
                            Toast.makeText(register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();



                        }
                    }

                });

            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), login.class));

            }
        });
    }}




