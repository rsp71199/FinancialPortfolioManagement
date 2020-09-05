package com.example.financialportfoliomanagement.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Adapters.WatchListAdapter;
import com.example.financialportfoliomanagement.Auth.Auth;
import com.example.financialportfoliomanagement.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.financialportfoliomanagement.Listners.WatchListDataListner;
import com.example.financialportfoliomanagement.Models.WatchListItem;
import com.example.financialportfoliomanagement.NetworkCalls.WatchListAsyncTask;
import com.example.financialportfoliomanagement.R;

import java.util.List;

public class WatchListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WatchListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private WatchListAsyncTask watchListAsyncTask;
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
        progressDialog = new ProgressDialog(this);

        auth.getUser(new AuthOnCompleteRetreiveInterface() {
            @Override
            public void onFireBaseUserRetrieveSuccess() {

                recyclerViewSetter();
                watchListAsyncTask = new WatchListAsyncTask(getApplication(), auth.user.getWatch_list_symbols(), progressDialog);
                watchListAsyncTask.setWatchListDataListner(new WatchListDataListner() {
                    @Override
                    public void onDataFetched(List<WatchListItem> listItems) {
                        mAdapter = new WatchListAdapter(listItems, getApplication(), auth, progressDialog);
                        recyclerView.setAdapter(mAdapter);
                        progressDialog.cancel();
                    }
                });
                watchListAsyncTask.execute(1);
                progressDialog.setTitle("loading...");
                progressDialog.show();

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
            case R.id.analyze: {
                Toast.makeText(this, "Analyzing", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(this, SearchActivity.class);
//                startActivity(i);
                break;
            }
            case R.id.refresh: {
                mAdapter.refresh();
                break;
            }


        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mAdapter != null) mAdapter.refresh();
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

    }
}