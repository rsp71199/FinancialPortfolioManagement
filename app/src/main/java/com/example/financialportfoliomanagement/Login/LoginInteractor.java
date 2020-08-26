package com.example.financialportfoliomanagement.Login;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginInteractor implements LoginContract.Intractor{
    private LoginContract.onLoginListener mOnLoginListener;

    public LoginInteractor(LoginContract.onLoginListener onLoginListener) {
        this.mOnLoginListener = onLoginListener;
    }

    @Override
    public void performFirebaseLogin(Activity activity, String email, String password) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("TAg", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + task.getResult().toString());

                            mOnLoginListener.onSuccess(task.getResult().toString());


                        } else {
                            mOnLoginListener.onFailure("oops! something went wrong");

                        }
                    }
                });

    }
}
