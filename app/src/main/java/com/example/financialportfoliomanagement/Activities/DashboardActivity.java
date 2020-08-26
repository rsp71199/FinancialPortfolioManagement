package com.example.financialportfoliomanagement.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Auth.Auth;
import com.example.financialportfoliomanagement.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.financialportfoliomanagement.Models.User;
import com.example.financialportfoliomanagement.NetworkCalls.DashBoardNetworkUtility;
import com.example.financialportfoliomanagement.R;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.navigation.NavigationView;

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
    private LineChart bse_chart, nse_chart, chart;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Auth auth;
    private Menu sideNavigationMenu;
    private LinearLayout navBarHeader;
    private User user;
    private ProgressDialog progressDialog;
    private final int fillColor = Color.argb(150, 51, 181, 229);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        newsRecyclerView = findViewById(R.id.news);
        trendingTickerRecyclerView = findViewById(R.id.trending);
        nifty_market_price = findViewById(R.id.nifty_regularMarketPrice);
        nifty_change = findViewById(R.id.nifty_marketPriceChange);
        bse_market_price = findViewById(R.id.sensex_regularMarketPrice);
        bse_change = findViewById(R.id.sensex_marketPriceChange);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading data...");
        dashBoardNetworkUtility = new DashBoardNetworkUtility(this, progressDialog);
        auth = new Auth();
        setSideNavigation();
        setNewsRecyclerView();
        setTrendingTickerRecyclerView();
        setViewsData();
        setChart();
        auth.getUser(new AuthOnCompleteRetreiveInterface() {
            @Override
            public void onFireBaseUserRetrieveSuccess() {
                user = auth.user;
                if (user != null) {
                    changeLoginStatus(true);
                }
            }

            @Override
            public void onFireBaseUserRetrieveFailure() {
                user = null;
            }
        });
    }


    private void setSideNavigation() {
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_dashboard);
        actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, R.string.actionToggleCircles, R.string.actionAddDataSet);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navbar);
        sideNavigationMenu = navigationView.getMenu();
//        navBarHeader = (LinearLayout) navigationView.getHeaderView(R.id.navBar_header);
        changeLoginStatus(false);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.login: {
                        if (user != null) {
                            Toast.makeText(DashboardActivity.this, "hello " + user.getUser_id(), Toast.LENGTH_SHORT).show();
                        } else {
                            moveToLoginActivity();
                        }

                        break;
                    }

                    case R.id.settings:
                        Toast.makeText(DashboardActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.watchlist:
                        Toast.makeText(DashboardActivity.this, "My Watch List", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.contact:
                        Toast.makeText(DashboardActivity.this, "Contact Us", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.help:
                        Toast.makeText(DashboardActivity.this, "Help", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.signOut: {
                        if (user != null) {
                            auth.signOut();
                            user = null;
                        }
                        changeLoginStatus(false);
                        break;
                    }

                    default:
                        return true;
                }
                return true;
            }
        });
    }

    private void setViewsData() {
        dashBoardNetworkUtility.set_NSE_BSE_data("", bse_market_price, bse_change, nifty_market_price, nifty_change);
        dashBoardNetworkUtility.set_News_data("", newsRecyclerView);
        dashBoardNetworkUtility.set_Trending_Tickers_data(trendingTickerRecyclerView);
    }

    private void moveToLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setChart() {
        nse_chart = findViewById(R.id.nse_chart);
        nse_chart.setDrawGridBackground(false);
        nse_chart.getDescription().setEnabled(false);
        nse_chart.setTouchEnabled(true);
        nse_chart.setDragEnabled(true);
        nse_chart.setScaleEnabled(false);
        nse_chart.setPinchZoom(false);
        nse_chart.getAxisLeft().setDrawGridLines(false);
        nse_chart.getAxisRight().setEnabled(false);
        nse_chart.getAxisLeft().setDrawLabels(false);
        nse_chart.getAxisRight().setDrawLabels(false);
        nse_chart.getXAxis().setDrawLabels(false);
        nse_chart.getXAxis().setDrawGridLines(false);
        nse_chart.getXAxis().setDrawAxisLine(false);
        dashBoardNetworkUtility.set_NSE_chart_data(nse_chart);
        nse_chart.invalidate();


        bse_chart = findViewById(R.id.bse_chart);
        bse_chart.setDrawGridBackground(false);
        bse_chart.getDescription().setEnabled(false);
        bse_chart.setTouchEnabled(true);


        bse_chart.setDragEnabled(true);
        bse_chart.setScaleEnabled(false);
        bse_chart.setPinchZoom(false);
        bse_chart.getAxisLeft().setDrawGridLines(false);
        bse_chart.getAxisLeft().setDrawLabels(false);
        bse_chart.getAxisRight().setDrawLabels(false);
        bse_chart.getXAxis().setDrawLabels(false);
        bse_chart.getAxisRight().setEnabled(false);
        bse_chart.getXAxis().setDrawGridLines(false);
        bse_chart.getXAxis().setDrawAxisLine(false);
        dashBoardNetworkUtility.set_BSE_chart_data(bse_chart);
        bse_chart.invalidate();
    }

    private void changeLoginStatus(boolean isLoggedIn) {
        MenuItem loginItem = sideNavigationMenu.findItem(R.id.login);
        MenuItem watchListItem = sideNavigationMenu.findItem(R.id.watchlist);
        MenuItem signOut = sideNavigationMenu.findItem(R.id.signOut);
        if (isLoggedIn) {
            watchListItem.setVisible(true);
            loginItem.setIcon(R.drawable.avatar);
            loginItem.setTitle("Profile");
            signOut.setVisible(true);
            navigationView.getHeaderView(0).findViewById(R.id.header_signIn).setVisibility(View.VISIBLE);
            navigationView.getHeaderView(0).findViewById(R.id.header_singOut).setVisibility(View.INVISIBLE);
        } else {
            watchListItem.setVisible(false);
            loginItem.setIcon(R.drawable.login);
            loginItem.setTitle("Sign in");
            signOut.setVisible(false);
            navigationView.getHeaderView(0).findViewById(R.id.header_signIn).setVisibility(View.INVISIBLE);
            navigationView.getHeaderView(0).findViewById(R.id.header_singOut).setVisibility(View.VISIBLE);
        }
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

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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