package com.example.financialportfoliomanagement.Activities;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.financialportfoliomanagement.Adapters.SearchAdapter;
import com.example.financialportfoliomanagement.Auth.Auth;
import com.example.financialportfoliomanagement.Interfaces.AuthOnCompleteRetreiveInterface;
import com.example.financialportfoliomanagement.Interfaces.OnSearchResultRetrieveInterface;
import com.example.financialportfoliomanagement.Models.SearchResult;
import com.example.financialportfoliomanagement.NetworkCalls.NetworkUtility;
import com.example.financialportfoliomanagement.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MaterialSearchView searchView;
    private SearchAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<SearchResult> searchResults = new ArrayList<>();
    private Toolbar toolbar;
    private Auth auth;
    private String from;
    private NetworkUtility networkUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAuth();
    }

    private void setAuth(){
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
        from = getIntent().getStringExtra("from");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
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
            }

            @Override
            public void onSearchResultRetrieveFailure() {

            }
        });
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
