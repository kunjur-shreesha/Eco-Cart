package com.beast.splashscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup_Activity extends AppCompatActivity {
    TextInputLayout regEmail,regPassword,regName,regUsername,regPhone;
    Button go,login;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);


        regName = findViewById(R.id.name);
        regUsername = findViewById(R.id.username);
        regEmail = findViewById(R.id.email);
        regPhone = findViewById(R.id.phone);
        regPassword = findViewById(R.id.password);
        go = findViewById(R.id.go);
        login = findViewById(R.id.login_back);
        login.setOnClickListener(view -> {
            Intent i = new Intent(Signup_Activity.this,Login_Activity.class);
            startActivity(i);
            finish();
        });
    }
    private Boolean validateName() {
        String val = regName.getEditText().getText().toString();
        if(val.isEmpty())
        {
            regName.setError("Field cannot be Empty");
            return false;
        }
        else{
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateUsername() {
        String val = regUsername.getEditText().getText().toString();
        String noSpaces = "\\A\\w{4,20}\\z";
        if(val.isEmpty())
        {
            regUsername.setError("Field cannot be Empty");
            return false;
        }
        else if(val.length()>=15)
        {
            regUsername.setError("Username too long");
            return false;
        }
        else if(!val.matches(noSpaces))
        {
            regUsername .setError("White Spaces  not allowed");
            return false;
        }
        else{
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }

    }
    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(val.isEmpty())
        {
            regEmail.setError("Field cannot be Empty");
            return false;
        }
        else if(!val.matches(pattern))
        {
            regEmail.setError("Invalid Email Address");
            return false;
        }
        else{
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }

    }
    private Boolean validatePhone() {
        String val = regPhone.getEditText().getText().toString();
        if(val.isEmpty())
        {
            regPhone.setError("Field cannot be Empty");
            return false;
        }
        else{
            regPhone.setError(null);
            regPhone.setErrorEnabled(false);
            return true;
        }

    }
    private Boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
        String pattern = "^"+
                        "(?=.*[a-zA-Z])"    //any letter
                        +"(?=.*[@#$%^&+=])" //1 sp char
                        +"(\\s+$)"          //no wtspce
                        +".{3,}"            //3 digit no atleast
                        +"$";
        if(val.isEmpty())
        {
            regPassword.setError("Field cannot be Empty");
            return false;
        }
//        else if(!val.matches(pattern))
//        {
//            regPassword.setError("Password is too weak");
//            regPassword.setErrorEnabled(false);
//            return false;
//        }
        else{
            regPassword.setError(null);
            return true;
        }

    }


    public void registerUser(View v)
    {
     if(!validateName()|!validateUsername()| !validateEmail() | !validatePhone() | !validatePassword())
     {
         return;
     }

            String name = regName.getEditText().getText().toString();
            String username = regUsername.getEditText().getText().toString();
            String email = regEmail.getEditText().getText().toString();
            String phone = regPhone.getEditText().getText().toString();
            String password = regPassword.getEditText().getText().toString();


             database = FirebaseDatabase.getInstance();
             mAuth = FirebaseAuth.getInstance();
             myRef = database.getReference("/");
             myRef= myRef.child("Users");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(username).getValue()==null)
                    {
                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Log.i("BAVA","firebase error");
                                    myRef = myRef.child(username);
                                    myRef.child("Name : ").setValue(name);
                                    myRef.child("Username : ").setValue(username);
                                    myRef.child("Email : ").setValue(email);
                                    myRef.child("Phone : ").setValue(phone);
                                    myRef.child("Password : ").setValue(password);
                                    Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(Signup_Activity.this,Login_Activity.class));
                                }
                                else
                                {
                                    Toast.makeText(Signup_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                          }
                    else {
                        Toast.makeText(getApplicationContext(), "Username already exits", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

}