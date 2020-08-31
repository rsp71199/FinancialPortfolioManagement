package com.example.financialportfoliomanagement.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.financialportfoliomanagement.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Commentary extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_commentary);
        setBottomNavigation();
    }
    private void setBottomNavigation(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_tab:
                        Toast.makeText(Commentary.this, "Home", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Commentary.this, DashBoardActivity2.class);
                        startActivity(i);
                        break;
                    case R.id.commentary:
                        Toast.makeText(Commentary.this, "Commentary", Toast.LENGTH_SHORT).show();
                        Intent j = new Intent(Commentary.this, Commentary.class);
                        startActivity(j);
                        break;
                    case R.id.report:
                        Toast.makeText(Commentary.this, "Reports", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

}