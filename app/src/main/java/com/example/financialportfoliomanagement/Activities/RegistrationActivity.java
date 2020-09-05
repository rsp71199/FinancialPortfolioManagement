package com.example.financialportfoliomanagement.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financialportfoliomanagement.Auth.Auth;
import com.example.financialportfoliomanagement.R;
import com.example.financialportfoliomanagement.Registration.RegistrationContract;
import com.example.financialportfoliomanagement.Registration.RegistrationPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegistrationActivity extends AppCompatActivity implements RegistrationContract.View {
    Button btnRegistration, btnLogin;
    EditText email, password1, password2;
    final String TAG = "GS";


    private RegistrationPresenter mRegisterPresenter;
    ProgressDialog mPrgressDialog;

    private static final int RC_SIGN_IN = 1;
    Auth auth;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        initViews();
    }

    private void initViews() {
        btnRegistration = findViewById(R.id.button_register);
        btnLogin = findViewById(R.id.button_login);
        email = findViewById(R.id.email_register);
        password1 = findViewById(R.id.password_register1);
        password2 = findViewById(R.id.password_register2);
        mRegisterPresenter = new RegistrationPresenter(this);
        mPrgressDialog = new ProgressDialog(this);
        mPrgressDialog.setMessage("Please wait, Adding profile to database...");


        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRegistrationDetails();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToLoginActivity();
            }
        });

    }


    private void moveToLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }


    ///validation
    private void checkRegistrationDetails() {
        if (!TextUtils.isEmpty(email.getText().toString())
                && !TextUtils.isEmpty(password1.getText().toString())
                && !TextUtils.isEmpty(password1.getText().toString())) {
            initLogin(email.getText().toString(), password1.getText().toString());


        } else {
            if (TextUtils.isEmpty(email.getText().toString())) {
                email.setError("Please enter a valid email");
            }
            if (TextUtils.isEmpty(password1.getText().toString())) {
                password1.setError("Please enter password");
            }
            if (TextUtils.isEmpty(password2.getText().toString())) {
                password2.setError("Please enter password");
            }
        }
    }

    private void initLogin(String email, String password) {
        mPrgressDialog.show();
        mRegisterPresenter.register(this, email, password);
    }

    @Override
    public void onRegistrationSuccess(FirebaseUser firebaseUser) {
        mPrgressDialog.dismiss();
//        btnRegistration.setText("Email verified ?");
        Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegistrationActivity.this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegistrationFailure(String message) {
        mPrgressDialog.dismiss();
        email.setError(message);
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}