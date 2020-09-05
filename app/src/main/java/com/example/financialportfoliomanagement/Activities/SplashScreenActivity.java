package com.example.financialportfoliomanagement.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financialportfoliomanagement.Auth.Auth;
import com.example.financialportfoliomanagement.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.financialportfoliomanagement.R;

import java.util.Calendar;

public class SplashScreenActivity extends AppCompatActivity {
    TextView textView;
    Animation top, bottom;
    Auth auth;
    int current_time;
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.getSupportActionBar().hide();
        Log.i("Splash", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>before auth");
        auth = new Auth();
        Calendar calendar = Calendar.getInstance();
        final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        Log.i("Splash", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + current_time);

        textView = findViewById(R.id.textView);
        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        textView.setAnimation(bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                auth.getUser(new AuthOnCompleteRetreiveInterface() {
                    @Override
                    public void onFireBaseUserRetrieveSuccess() {
                        if (auth.user == null) {
                            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(SplashScreenActivity.this, DashBoardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFireBaseUserRetrieveFailure() {
                        Log.i("reached here", ">>>>>>>>>>>>>>>>>>>>>>>");
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        }, 2000);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
