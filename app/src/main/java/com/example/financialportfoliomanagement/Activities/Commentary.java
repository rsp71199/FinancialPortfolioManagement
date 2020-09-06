package com.example.financialportfoliomanagement.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.financialportfoliomanagement.R;

public class Commentary extends AppCompatActivity {

    private Toolbar toolbar;
    private AutoCompleteTextView time_period_selector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_commentary);
        time_period_selector = findViewById(R.id.prediction_period);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String[] COUNTRIES = new String[]{"1 day", "1 week", "1 month", "1 year"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.time_period_menu,
                        COUNTRIES);


        time_period_selector.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.commentry_menu, menu);
//        MenuItem item = menu.findItem(R.id.action_search);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                super.onBackPressed();
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}