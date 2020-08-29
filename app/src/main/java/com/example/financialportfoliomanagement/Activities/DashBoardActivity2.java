package com.example.financialportfoliomanagement.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.financialportfoliomanagement.Adapters.DahsBoardFragmentAdapter;
import com.example.financialportfoliomanagement.Auth.Auth;
import com.example.financialportfoliomanagement.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.financialportfoliomanagement.Models.User;
import com.example.financialportfoliomanagement.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class DashBoardActivity2 extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Auth auth;
    private Menu sideNavigationMenu;
    private LinearLayout navBarHeader;
    private Toolbar toolbar;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board2);
        auth = new Auth();
        setTabLayout();
        setSideNavigation();
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

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
                            Toast.makeText(DashBoardActivity2.this, "hello " + user.getUser_id(), Toast.LENGTH_SHORT).show();
                        } else {
                            moveToLoginActivity();
                        }

                        break;
                    }

                    case R.id.settings:
                        Toast.makeText(DashBoardActivity2.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.watchlist: {
                        Toast.makeText(DashBoardActivity2.this, "My Watch List", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DashBoardActivity2.this, WatchListActivity.class));
                        break;
                    }

                    case R.id.contact:
                        Toast.makeText(DashBoardActivity2.this, "Contact Us", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.help:
                        Toast.makeText(DashBoardActivity2.this, "Help", Toast.LENGTH_SHORT).show();
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

    private void setTabLayout() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Indices"));
        tabLayout.addTab(tabLayout.newTab().setText("Shares"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        final DahsBoardFragmentAdapter adapter = new DahsBoardFragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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
//                setViewsData();
                break;
            }


        }
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    private void moveToLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }


}


