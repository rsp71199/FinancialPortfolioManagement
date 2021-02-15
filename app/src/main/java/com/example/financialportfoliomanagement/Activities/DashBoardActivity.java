package com.example.financialportfoliomanagement.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.financialportfoliomanagement.Adapters.DashBoardFragmentAdapter;
import com.example.financialportfoliomanagement.Models.User;
import com.example.financialportfoliomanagement.R;
import com.example.financialportfoliomanagement.ViewModel.AuthViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

public class DashBoardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    //    private Auth auth;
    private Menu sideNavigationMenu;
    private Toolbar toolbar;
    //    private User user;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AuthViewModel authViewModel;
    private User user_main;
    private DashBoardFragmentAdapter dashBoardFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();
        setSideNavigation();
        setListeners();
        setAuthViewModel();
//        setAuth();
    }

    //    private void setAuth(){
//        auth.getUser(new AuthOnCompleteRetreiveInterface() {
//            @Override
//            public void onFireBaseUserRetrieveSuccess() {
//                user = auth.user;
//                if (user != null) {
//                    changeLoginStatus(true);
//                }
//            }
//            @Override
//            public void onFireBaseUserRetrieveFailure() {
//                user = null;
//            }
//        });
//    }
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
                        if (user_main != null) {
                            startActivity(new Intent(DashBoardActivity.this, ReportActivity.class));
                        } else {
                            moveToLoginActivity();
                        }
                        break;
                    }
                    case R.id.commentary: {
                        startActivity(new Intent(DashBoardActivity.this, Commentary.class));
                        break;
                    }
                    case R.id.settings:
                        Toast.makeText(DashBoardActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.signOut: {
                        if (user_main != null) {
                            authViewModel.SignOut();
                            user_main = null;
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
    private void setLayout() {
        setContentView(R.layout.activity_dash_board);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Indices"));
        tabLayout.addTab(tabLayout.newTab().setText("Shares"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dashBoardFragmentAdapter = new DashBoardFragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(dashBoardFragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
    private void setListeners(){
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
        MenuItem signOut = sideNavigationMenu.findItem(R.id.signOut);
        MenuItem commentary = sideNavigationMenu.findItem(R.id.commentary);

        if (isLoggedIn) {

            loginItem.setIcon(R.drawable.avatar);
            loginItem.setTitle("Profile");
            signOut.setVisible(true);
            commentary.setVisible(true);
            navigationView.getHeaderView(0).findViewById(R.id.header_signIn).setVisibility(View.VISIBLE);
            navigationView.getHeaderView(0).findViewById(R.id.header_singOut).setVisibility(View.INVISIBLE);
        }
        else {

            loginItem.setIcon(R.drawable.login);
            loginItem.setTitle("Sign in");
            signOut.setVisible(false);
            commentary.setVisible(false);
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

    private void setAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.getAuthData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                user_main = user;
                if (user_main == null) {
                    changeLoginStatus(false);
                } else {
                    Log.i("Dashboard", ">>>>>>>>>>>>>>>>>>>>" + user_main.getUser_id());
                    changeLoginStatus(true);
                }
            }
        });
        if (authViewModel.getAuthData().getValue() == null) {
            authViewModel.setFirebaseFirestore();
        }

    }

    private void moveToLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
//        finish();
    }


}


