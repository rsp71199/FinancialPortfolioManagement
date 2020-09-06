package com.example.financialportfoliomanagement.Activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Adapters.SearchAdapter;
import com.example.financialportfoliomanagement.Auth.Auth;
import com.example.financialportfoliomanagement.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.financialportfoliomanagement.Interfaces.OnSearchResultRetrieveInterface;
import com.example.financialportfoliomanagement.Models.SearchResult;
import com.example.financialportfoliomanagement.NetworkCalls.NetworkUtility;
import com.example.financialportfoliomanagement.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MaterialSearchView searchView;
    private SearchAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<SearchResult> searchResults = new ArrayList<>();
    private Toolbar toolbar;
    ImageButton refresh;
    private Auth auth;
    private String from;
    LinearLayout recycler_view_view, no_connection_view, progress_bar_view;
    private NetworkUtility networkUtility;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAuth();
    }

    private void setAuth() {
        auth = new Auth();
        auth.getUser(new AuthOnCompleteRetreiveInterface() {
            @Override
            public void onFireBaseUserRetrieveSuccess() {
                init();
            }

            @Override
            public void onFireBaseUserRetrieveFailure() {
                auth=null;
                init();
            }
        });
    }
    private void setLayout(){
        setContentView(R.layout.activity_search);
        refresh = findViewById(R.id.refresh_button);
        from = getIntent().getStringExtra("from");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        no_connection_view = findViewById(R.id.no_connection_view);
        progress_bar_view = findViewById(R.id.progress_bar_view);
        recycler_view_view = findViewById(R.id.search_recycler_view_view);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressbarView();
                networkUtility.get_search_result(text);
            }
        });
    }
    private void setNetworkUtility(){
        networkUtility = new NetworkUtility(this,auth);
        networkUtility.setOnSearchResultRetrieveInterface(new OnSearchResultRetrieveInterface() {
            @Override
            public void onSearchResultRetrieveSuccess(List<SearchResult> searchResultList) {
                if(mAdapter==null){
                    mAdapter = new SearchAdapter(searchResultList,getApplication(),auth);
                    recyclerView.setAdapter(mAdapter);
                }else{
                    mAdapter.refresh(searchResultList);
                }
                showSearchRecyclerView();
            }

            @Override
            public void onSearchResultRetrieveFailure() {
                showNoConnectionView();
            }
        });
        showProgressbarView();
        networkUtility.get_search_result("A");
    }
    private void recyclerViewSetter() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        Drawable verticalDivider = ContextCompat.getDrawable(this, R.drawable.shape);
        dividerItemDecoration.setDrawable(verticalDivider);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void showSearchRecyclerView(){
        recycler_view_view.setVisibility(View.VISIBLE);
        no_connection_view.setVisibility(View.INVISIBLE);
        progress_bar_view.setVisibility(View.INVISIBLE);
    }
    public void showNoConnectionView(){
        recycler_view_view.setVisibility(View.INVISIBLE);
        no_connection_view.setVisibility(View.VISIBLE);
        progress_bar_view.setVisibility(View.INVISIBLE);
    }
    public void showProgressbarView(){
        recycler_view_view.setVisibility(View.INVISIBLE);
        no_connection_view.setVisibility(View.INVISIBLE);
        progress_bar_view.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }




    private void init() {
        setLayout();
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                text = newText;
                showProgressbarView();
                networkUtility.get_search_result(newText);

                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        recyclerViewSetter();
        setNetworkUtility();
    }


}
