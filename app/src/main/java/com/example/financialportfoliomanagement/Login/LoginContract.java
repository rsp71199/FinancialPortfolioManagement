package com.example.financialportfoliomanagement.Login;

import android.app.Activity;

public class LoginContract {
    public interface View {
        void onLoginSuccess(String message);

        void onLoginFailure(String message);
    }

    interface Presenter {
        void login(Activity activity, String email, String password);
    }

    interface Intractor {
        void performFirebaseLogin(Activity activity, String email, String password);
    }

    interface onLoginListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
