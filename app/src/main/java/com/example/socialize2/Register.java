package com.example.socialize2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPassword,mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

       mFullName = findViewById(R.id.fullName);
       mEmail = findViewById(R.id.Email);
       mPassword = findViewById(R.id.Password);
       mPhone = findViewById(R.id.phone);
       mRegisterBtn = findViewById(R.id.registerBtn);
       mLoginBtn = findViewById(R.id.createText);
       fAuth = FirebaseAuth.getInstance();
       progressBar = findViewById(R.id.progressBar);
       fStore = FirebaseFirestore.getInstance();




       mRegisterBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final String email = mEmail.getText().toString().trim();
               String password = mPassword.getText().toString().trim();
               final String fullName = mFullName.getText().toString();
               final String phone = mPhone.getText().toString();

               if(TextUtils.isEmpty(email)){
                   mEmail.setError("Email is required");
                   return;
               }

               if(TextUtils.isEmpty(password)){
                   mPassword.setError("Password is required");
               return;
               }

               if (password.length() < 6){
                   mPassword.setError("Password must be >= 6 Characters");
                   return;
               }
               progressBar.setVisibility(View.VISIBLE);

               //Registering user
               fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){


                           FirebaseUser user = fAuth.getCurrentUser();
                           user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Toast.makeText(Register.this, "Verification Email has been sent", Toast.LENGTH_SHORT).show();
                               }
                           }) .addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(Register.this, "Error ! Verification Email sent failed", Toast.LENGTH_SHORT).show();
                               }
                           }) ;


                           Toast.makeText(Register.this,"User Created",Toast.LENGTH_SHORT).show();

                           //Database creation
                           userId = fAuth.getCurrentUser().getUid();
                           DocumentReference documentReference = fStore.collection("users").document(userId);
                           Map<String,Object> user3 = new HashMap<>();
                           user3.put("fName", fullName);
                           user3.put("email", email);
                           user3.put("phone",phone);
                           documentReference.set(user3).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   Log.d(TAG, "onSuccess: user profile is created for" + userId);
                               }
                           });

                           startActivity(new Intent(getApplicationContext(),MainActivity.class));
                       }else{
                           Toast.makeText(Register.this,"Error" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                           progressBar.setVisibility(View.GONE);
                       }
                   }
               });
           }
       });
                      mLoginBtn.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              startActivity(new Intent(Register.this,Login.class));
                          }
                      });
    }
}
