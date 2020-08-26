package com.example.financialportfoliomanagement.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.financialportfoliomanagement.Login.LoginContract;
import com.example.financialportfoliomanagement.Login.LoginPresenter;
import com.example.financialportfoliomanagement.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginContract.View {

    Button btnLogin;
    TextView tvRegister;
    EditText edtEmail, edtPassword;
    private LoginPresenter mLoginPresenter;
    ProgressDialog mProgressDialog;
    private Window window;
    final String TAG = "GS";
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog mPrgressDialog;
    private ImageView gSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mPrgressDialog = new ProgressDialog(this);
        initViews();
    }
    private void initViews() {
        //Toast.makeText(getApplicationContext(), "Internet Required" , Toast.LENGTH_SHORT).show();
        btnLogin = (Button) findViewById(R.id.button_login);
        gSignIn = findViewById(R.id.g_sign_in_button);
        btnLogin.setOnClickListener(this);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(this);
        edtEmail = (EditText) findViewById(R.id.email_login);
        edtPassword = (EditText) findViewById(R.id.password_login);

        mLoginPresenter = new LoginPresenter(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait, Logging in..");
        gSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrgressDialog.setMessage("Authenticating user...");
                mPrgressDialog.show();
                signIn();
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                checkLoginDetails();
                break;
            case R.id.tv_register:
                moveToRegisterActivity();
                break;
        }
    }

    private void moveToRegisterActivity() {
        Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkLoginDetails() {
        if (!TextUtils.isEmpty(edtEmail.getText().toString()) && !TextUtils.isEmpty(edtPassword.getText().toString())) {
            initLogin(edtEmail.getText().toString(), edtPassword.getText().toString());
        } else {
            if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                edtEmail.setError("Please enter a valid email");
            }
            if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                edtPassword.setError("Please enter password");
            }
        }
    }

    private void initLogin(String email, String password) {
        mProgressDialog.show();
        mLoginPresenter.login(this, email, password);
    }

    @Override
    public void onLoginSuccess(String message) {
        mProgressDialog.dismiss();
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailure(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken(), this);
            } catch (ApiException e) {

                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "There was a problem signing in through Google", Toast.LENGTH_LONG);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken, final Context context) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseFirestore.getInstance().collection("User").document(user.getUid())
                                    .get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            mPrgressDialog.cancel();
                                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                            finish();
                                        } else {
                                            mPrgressDialog.cancel();
                                            addNewUser(user);
                                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                            finish();
                                        }
                                    }
                                }
                            });


                        } else {
                            mPrgressDialog.cancel();
                            Toast.makeText(context, "There was a problem signing in through Google", Toast.LENGTH_LONG);
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                    }
                });
    }

    private void addNewUser(FirebaseUser user) {
        List<String> l = new ArrayList<>();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String, Object> docData = new HashMap<>();
        docData.put("user_id", user.getUid());
        docData.put("current_time", 0);
        docData.put("current_duration", 0);
        docData.put("current_mall_id", "");
        docData.put("current_plot_id", "");
        docData.put("has_taken", false);
        docData.put("transaction_history", l);
        docData.put("name", "");
        docData.put("extended_time", 0);
        docData.put("entry", false);
        docData.put("mb_id", "");

        firebaseFirestore.collection("User").document(user.getUid()).set(docData);
    }
}