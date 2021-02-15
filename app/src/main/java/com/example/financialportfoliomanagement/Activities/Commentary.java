package com.example.financialportfoliomanagement.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.financialportfoliomanagement.Models.User;
import com.example.financialportfoliomanagement.R;
import com.example.financialportfoliomanagement.Utilities.CustomDateTimePicker;
import com.example.financialportfoliomanagement.ViewModel.AuthViewModel;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

//import com.example.financialportfoliomanagement.Auth.Auth;

public class Commentary extends AppCompatActivity {

    private Toolbar toolbar;
    private RadioGroup radioGroup;
    private EditText stock_sym, stock_amount;
    FirebaseFirestore firebaseFirestore;
    private Button submit, add_transaction;
    private ImageButton date_time_button;
    private MaterialAutoCompleteTextView prediction_period;
    private CustomDateTimePicker custom;
    private AutoCompleteTextView time_period_selector;
    private AuthViewModel authViewModel;
    private User user_main;
//    private Auth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentary);

        firebaseFirestore = FirebaseFirestore.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stock_sym = findViewById(R.id.stock_name);
        stock_amount = findViewById(R.id.stock_amount);
        time_period_selector = findViewById(R.id.prediction_period);
        date_time_button = findViewById(R.id.date_time_button);
        radioGroup = findViewById(R.id.radio_group);
        prediction_period = findViewById(R.id.prediction_period);
        submit = findViewById(R.id.submit_comm);
        add_transaction = findViewById(R.id.add_to_transaction);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.commentry_menu, menu);
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

    private void init() {
        setAuthViewModel();
        final String[] durations = new String[]{"1 day", "1 week", "1 month", "1 year"};

        final ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.time_period_menu,
                        durations);
        time_period_selector.setAdapter(adapter);

        custom = new CustomDateTimePicker(this,
                new CustomDateTimePicker.ICustomDateTimeListener() {

                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
        custom.set24HourFormat(false);
        custom.setDate(Calendar.getInstance());

        date_time_button.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        custom.showDialog();
                    }
                });

        prediction_period.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("TAG>>>>>>>>>>>>>>>>>>", durations[i]);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int buy_sold = radioGroup.getCheckedRadioButtonId();

            }
        });
        add_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                Long time = date.getTime();
                String s = time.toString() + "," + stock_sym.getText() + "," + stock_amount.getText();
                user_main.add_transaction_item(s);
                authViewModel.updateUser(user_main);


            }
        });
//        add_transaction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                auth =
//                user_main.add_transaction_item("hello");
//                authViewModel.updateUser(user_main);
//
//            }
//        });
    }

    private void setAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getAuthData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                user_main = user;


            }
        });
        if (authViewModel.getAuthData().getValue() == null) {
            authViewModel.setFirebaseFirestore();
        }

    }
}