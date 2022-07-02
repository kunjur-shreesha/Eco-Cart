package com.beast.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Test_demo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_demo);
    }
    public void logout(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,Login_Activity.class));
        finish();
    }
}