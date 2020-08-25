package com.example.financialportfoliomanagement.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.NetworkCalls.DashBoardNetworkUtility;
import com.example.financialportfoliomanagement.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    RecyclerView newsRecyclerView;
    RecyclerView trendingTickerRecyclerView;
    private RecyclerView.LayoutManager newsLayoutManager;
    private RecyclerView.LayoutManager trendingTickerLayoutManager;
    private DashBoardNetworkUtility dashBoardNetworkUtility;
    public TextView nifty_market_price;
    public TextView nifty_change;
    public TextView bse_market_price;
    public TextView bse_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dashBoardNetworkUtility = new DashBoardNetworkUtility(this);
        newsRecyclerView = findViewById(R.id.news);
        trendingTickerRecyclerView = findViewById(R.id.trending);
        nifty_market_price = findViewById(R.id.nifty_regularMarketPrice);
        nifty_change = findViewById(R.id.nifty_marketPriceChange);
        bse_market_price = findViewById(R.id.sensex_regularMarketPrice);
        bse_change = findViewById(R.id.sensex_marketPriceChange);
        setNewsRecyclerView();
        setTrendingTickerRecyclerView();
        setViewsData();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_tab:
                        Toast.makeText(DashboardActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.commentary:
                        Toast.makeText(DashboardActivity.this, "Commentary", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.report:
                        Toast.makeText(DashboardActivity.this, "Reports", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }


    private void setViewsData() {
        dashBoardNetworkUtility.set_News_data("", newsRecyclerView);
        dashBoardNetworkUtility.set_NSE_BSE_data("", bse_market_price, bse_change, nifty_market_price, nifty_change);
        dashBoardNetworkUtility.set_Trending_Tickers_data(trendingTickerRecyclerView);
    }

    private void setNewsRecyclerView() {
        newsRecyclerView.setHasFixedSize(true);
        newsLayoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(newsLayoutManager);
        newsRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

    }

    private void setTrendingTickerRecyclerView() {
        trendingTickerRecyclerView.setHasFixedSize(true);
        trendingTickerLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        trendingTickerRecyclerView.setLayoutManager(trendingTickerLayoutManager);
        trendingTickerRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL));
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
                setViewsData();
                break;
            }


        }
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

}