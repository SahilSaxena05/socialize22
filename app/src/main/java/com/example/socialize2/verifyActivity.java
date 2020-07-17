package com.example.socialize2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class verifyActivity extends AppCompatActivity {
    TextView VerifyMsg;
    Button VerifyBtn;
    String userid1;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        VerifyMsg = findViewById(R.id.VerifyMsg);
        VerifyBtn = findViewById(R.id.VerifyBtn);
        final FirebaseUser user = fAuth.getCurrentUser();
        userid1 = fAuth.getUid();



        VerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(verifyActivity.this, "Verification Email has been sent", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(verifyActivity.this,MainActivity.class));
                    }
                }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(verifyActivity.this, "Error ! Verification Email sent failed", Toast.LENGTH_SHORT).show();
                    }
                }) ;
            }
        });
    }
    }

