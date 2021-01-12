package com.example.financialportfoliomanagement.Activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Adapters.QuestionAdapter;
import com.example.financialportfoliomanagement.Models.Question;
import com.example.financialportfoliomanagement.Models.User;
import com.example.financialportfoliomanagement.R;
import com.example.financialportfoliomanagement.ViewModel.AuthViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuestionnareActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;
    private List<Question> questions;
    private RecyclerView recyclerView;
    private QuestionAdapter mAdapter;
    private Button submit;
    private AuthViewModel authViewModel;
    private User user_main;
    public DeleteWatchListItem deleteWatchListItem;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnare);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    public void init() {
        submit = findViewById(R.id.submit);
        submit.setClickable(false);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questions = mAdapter.getmDataset();

                List<String> marks = new ArrayList<String>();
                Integer risk_score = 0, ts = 0, rs = 0;
                for (Question q : questions) {
                    Log.i("type", ">>>>>>>>>>>>>" + q.getType());
                    if (q.getType().equals("risk")) {
                        rs += Integer.parseInt(q.getSelected_ans());
                    } else if (q.getType().equals("time")) {
                        ts += Integer.parseInt(q.getSelected_ans());
                    }
                    marks.add(q.getSelected_ans().toString());


                    risk_score = risk_score + Integer.parseInt(q.getSelected_ans());

                }

                user_main.setCategory(categorize(rs, ts));
                Log.i("Category>>>>>", "rs=" + rs + " ts=" + ts + " cat=" + categorize(rs, ts));
                user_main.setRiskScore(marks);
                authViewModel.updateUser(user_main);
                finish();
            }
        });
        setAuthViewModel();
        recyclerViewSetter(this);
        questions = new ArrayList<Question>();
        firebaseFirestore = FirebaseFirestore.getInstance();


        firebaseFirestore.collection("questions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                questions.add(document.toObject(Question.class));
                                Log.i("TAG>>>>>>", ">>>>>>>>>>>>>>>>>>>>>>" + document.getId());
                            }

                            mAdapter = new QuestionAdapter(questions, getApplication());
                            recyclerView.setAdapter(mAdapter);

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    private void setAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getAuthData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                user_main = user;
                Log.i("Report>>>>>>>>>>", user.getUser_id());
                submit.setClickable(true);
//                getReportDataFromNetwork();
            }
        });
        if (authViewModel.getAuthData().getValue() == null) {
            authViewModel.setFirebaseFirestore();
        }

    }

    private void recyclerViewSetter(Context context) {
        recyclerView = findViewById(R.id.my_recycler_view);


        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        Drawable verticalDivider = ContextCompat.getDrawable(this, R.drawable.shape);
        dividerItemDecoration.setDrawable(verticalDivider);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    private String categorize(int rs, int ts) {
        if ((rs <= 10) || (rs <= 11 && ts <= 12) || (rs <= 12 && ts <= 9) || (rs <= 15 && ts == 5) || (rs <= 18 && ts <= 4))
            return "conservative";
        if ((rs <= 17 && ts <= 18) || (rs <= 18 && ts <= 12) || (rs <= 20 && ts <= 9) || (rs <= 24 && ts == 5) || (rs <= 31 && ts <= 4))
            return "moderately conservative";
        if ((rs <= 24 && ts <= 18) || (rs <= 26 && ts <= 12) || (rs <= 28 && ts <= 9) || (rs <= 35 && ts == 5) || (rs <= 40 && ts <= 4))
            return "moderately";
        if ((rs <= 31 && ts <= 18) || (rs <= 34 && ts <= 12) || (rs <= 37 && ts <= 9) || (rs <= 40 && ts <= 5))
            return "moderately aggressive";
        return "aggressive";
    }

    public interface DeleteWatchListItem {
        public void onDelete();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.commentry_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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