package com.example.financialportfoliomanagement.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Adapters.WatchListAdapter;
import com.example.financialportfoliomanagement.Auth.Auth;
import com.example.financialportfoliomanagement.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.financialportfoliomanagement.R;

public class WatchListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private com.google.android.material.floatingactionbutton.FloatingActionButton floatingActionButton;
    Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);
        toolbar = findViewById(R.id.toolbar);
        floatingActionButton = findViewById(R.id.floating_add_button);
        setSupportActionBar(toolbar);
        auth = new Auth();
        auth.getUser(new AuthOnCompleteRetreiveInterface() {
            @Override
            public void onFireBaseUserRetrieveSuccess() {
                recyclerViewSetter();
            }

            @Override
            public void onFireBaseUserRetrieveFailure() {

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(WatchListActivity.this, SearchActivity.class);
                startActivity(i);
            }
        });
    }

    private void recyclerViewSetter() {
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        Drawable verticalDivider = ContextCompat.getDrawable(this, R.drawable.shape);
        dividerItemDecoration.setDrawable(verticalDivider);
        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new WatchListAdapter(auth.user.getWatch_list_symbols(), getApplicationContext());
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
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
            case R.id.search: {
                Intent i = new Intent(this, SearchActivity.class);
                startActivity(i);
                break;
            }
            case R.id.refresh: {
//                setViewsData();
                break;
            }


        }
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}