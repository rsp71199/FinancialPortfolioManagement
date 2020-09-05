package com.example.financialportfoliomanagement.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financialportfoliomanagement.Login.LoginContract;
import com.example.financialportfoliomanagement.Login.LoginPresenter;
import com.example.financialportfoliomanagement.R;


public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    Button btnRegistration, btnLogin;
    EditText email, password;
    final String TAG = "GS";
    private LoginPresenter mLoginPresenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initViews();
    }


    private void initViews() {
        btnRegistration = findViewById(R.id.button_register);
        btnLogin = findViewById(R.id.button_login);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        mLoginPresenter = new LoginPresenter(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait, Logging in..");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLoginDetails();
            }
        });

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToRegisterActivity();
            }
        });
    }


    private void moveToRegisterActivity() {
        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkLoginDetails() {
        if (!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
            progressDialog.show();
            initLogin(email.getText().toString(), password.getText().toString());
        } else {
            if (TextUtils.isEmpty(email.getText().toString())) {
                email.setError("Please enter a valid email");
            }
            if (TextUtils.isEmpty(password.getText().toString())) {
                password.setError("Please enter password");
            }
        }
    }

    private void initLogin(String email, String password) {
        progressDialog.show();
        Log.i("TAG", email + " " + password);
        mLoginPresenter.login(this, email, password);
    }

    @Override
    public void onLoginSuccess(String message) {
        progressDialog.dismiss();
        Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailure(String message) {
        progressDialog.dismiss();
        email.setError(message);
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}