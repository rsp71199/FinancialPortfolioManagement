package com.example.financialportfoliomanagement.Activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Adapters.RecommendationListAdapter;
import com.example.financialportfoliomanagement.Models.RecommendationListItem;
import com.example.financialportfoliomanagement.Models.User;
import com.example.financialportfoliomanagement.R;
import com.example.financialportfoliomanagement.ViewModel.AuthViewModel;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

public class RecommendationListActivity extends AppCompatActivity {
    private LineChart lineChart;
    private LinearLayout no_connection_view, progress_bar_view;
    private RelativeLayout recycler_view_view;
    private RecyclerView recyclerView;
    private RecommendationListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private List<RecommendationListItem> recommendationListItemList;
    private AuthViewModel authViewModel;
    private User user_main;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_list);
        toolbar = findViewById(R.id.toolbar);
        recycler_view_view = findViewById(R.id.recycler_view_view);
        no_connection_view = findViewById(R.id.no_connection_view);
        progress_bar_view = findViewById(R.id.progress_bar_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        auth = new Auth();
        setAuthViewModel();
        showProgressbarView();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.watch_list_menu, menu);
//        MenuItem item = menu.findItem(R.id.action_search);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home: {
                super.onBackPressed();
                finish();
            }


        }


        return super.onOptionsItemSelected(item);
    }

    public void showWatchListRecyclerView() {
        recycler_view_view.setVisibility(View.VISIBLE);
        no_connection_view.setVisibility(View.INVISIBLE);
        progress_bar_view.setVisibility(View.INVISIBLE);
    }

    public void showNoConnectionView() {
        recycler_view_view.setVisibility(View.INVISIBLE);
        no_connection_view.setVisibility(View.VISIBLE);
        progress_bar_view.setVisibility(View.INVISIBLE);
    }

    public void showProgressbarView() {
        recycler_view_view.setVisibility(View.INVISIBLE);
        no_connection_view.setVisibility(View.INVISIBLE);
        progress_bar_view.setVisibility(View.VISIBLE);
    }

    private void setAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getAuthData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                user_main = user;
                if (user == null) showNoConnectionView();
                else {
                    Log.i("Report>>>>>>>>>>", user.getUser_id());
                    recyclerViewSetter();
                    mAdapter = new RecommendationListAdapter(user_main.getRecommendation_list());
                    recyclerView.setAdapter(mAdapter);
                    showWatchListRecyclerView();

                }


            }
        });
        if (authViewModel.getAuthData().getValue() == null) {
            authViewModel.setFirebaseFirestore();
        }

    }

    private void recyclerViewSetter() {
        recyclerView = findViewById(R.id.my_recycler_view);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(RecommendationListActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(RecommendationListActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView

                int position = viewHolder.getAdapterPosition();
                user_main.delete_recommendation_list_item(position);
//                mAdapter.notifyDataSetChanged();
                mAdapter.notifyItemRemoved(position);
                authViewModel.updateUser(user_main);

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        Drawable verticalDivider = ContextCompat.getDrawable(this, R.drawable.shape);
        dividerItemDecoration.setDrawable(verticalDivider);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }
}