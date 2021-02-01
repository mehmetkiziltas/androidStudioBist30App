package com.example.gradiotionproject.LoginProcesse;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gradiotionproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SingUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText inputUserName, inputEmail, inputPassword, inputPasswordCheck;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.textSingIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        inputUserName = findViewById(R.id.inputUserName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputPasswordCheck = findViewById(R.id.inputPasswordCheck);

        inputPasswordCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!inputPassword.getText().toString().equals(inputPasswordCheck.getText().toString())){
                    inputPasswordCheck.setTextColor(Color.parseColor("#cc0c0c"));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (inputPassword.getText().toString().equals(inputPasswordCheck.getText().toString())){
                    inputPasswordCheck.setTextColor(Color.parseColor("#000b76"));
                }
            }
        });
    }

    public void singUpClicked(View view) {
        String userName = inputUserName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (inputPassword.getText().toString().equals(inputPasswordCheck.getText().toString())) {

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    HashMap<String,Object> postData = new HashMap<>();
                    postData.put("userId",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    postData.put("userName",userName);
                    postData.put("userEmail",email);
                    postData.put("totalMoney",1000);
                    postData.put("moneyType","TLY");

                    firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(postData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SingUpActivity.this, "Hoş Geldiniz! Paranız Tanımlanmıştır.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SingUpActivity.this,"Failed!.",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    Toast.makeText(SingUpActivity.this,"User Created",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SingUpActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(SingUpActivity.this,"Passwords not match",Toast.LENGTH_LONG).show();
        }

    }
}












