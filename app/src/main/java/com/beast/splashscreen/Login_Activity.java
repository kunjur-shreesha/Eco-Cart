package com.beast.splashscreen;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.util.Pair;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login_Activity extends AppCompatActivity {
    Button callSignUp, login_button;
    ImageView logo;
    TextView logotext, slogantext;
    TextInputLayout email, pass;
    EditText email_addr, pass_addr;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        callSignUp = findViewById(R.id.signup_button);
        login_button = findViewById(R.id.Login_button);
        logo = findViewById(R.id.logo_image);
        slogantext = findViewById(R.id.slogan_text);
        logotext = findViewById(R.id.logo_name);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        email_addr = findViewById(R.id.email_addr);
        pass_addr = findViewById(R.id.pass_addr);

        FirebaseAuth.getInstance().signOut();
        callSignUp.setOnClickListener(view -> {
            Intent i = new Intent(Login_Activity.this, Signup_Activity.class);
            Pair[] pairs = new Pair[7];
            pairs[0] = new Pair<View, String>(logo, "logo_image");
            pairs[1] = new Pair<View, String>(logotext, "logo_name");
            pairs[2] = new Pair<View, String>(slogantext, "logo_desc");
            pairs[3] = new Pair<View, String>(email, "email_trans");
            pairs[4] = new Pair<View, String>(pass, "pass_trans");
            pairs[5] = new Pair<View, String>(login_button, "go_trans");
            pairs[6] = new Pair<View, String>(callSignUp, "login_signup_trans");
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login_Activity.this, pairs);
            startActivity(i, options.toBundle());
//            finish();
        });
    }

    public void login(View v) {

        String email_key = email_addr.getText().toString();
        String pass_key = pass_addr.getText().toString();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email_key, pass_key).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(Login_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login_Activity.this,DashBoard.class);
                    startActivity(i);
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(Login_Activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                }
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            startActivity(new Intent(Login_Activity.this, DashBoard.class));
            finish();
        }
    }
}