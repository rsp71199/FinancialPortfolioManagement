package com.example.financialportfoliomanagement.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Adapters.NewsAdapter;
import com.example.financialportfoliomanagement.Interfaces.OnNewsRetrieveInterface;
import com.example.financialportfoliomanagement.Models.News;
import com.example.financialportfoliomanagement.NetworkCalls.NetworkUtility;
import com.example.financialportfoliomanagement.R;

import java.util.List;

public class StockFragment extends Fragment {
    private RecyclerView newsRecyclerView;
    private NetworkUtility networkUtility;
    private NewsAdapter newsAdapter;
    private RecyclerView.LayoutManager newsLayoutManager;
    private LinearLayout no_connection_view, progress_bar_view, news_recycler_view_view;

    public StockFragment() {
        // Required empty public constructor
    }


    public static StockFragment newInstance(String param1, String param2) {
        StockFragment fragment = new StockFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        no_connection_view = view.findViewById(R.id.no_connection_view);
        news_recycler_view_view = view.findViewById(R.id.news_recycler_view_view);
        progress_bar_view = view.findViewById(R.id.progress_bar_view);
        newsRecyclerView = view.findViewById(R.id.news_recycler_view);
        setIndexRecyclerView();
        showProgressBar();
        setNetworkUtility();
        return view;
    }

    private void setIndexRecyclerView() {
        newsRecyclerView.setHasFixedSize(true);
        newsLayoutManager = new LinearLayoutManager(getContext());
        newsRecyclerView.setLayoutManager(newsLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable verticalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.shape);
        dividerItemDecoration.setDrawable(verticalDivider);
        newsRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void setNetworkUtility() {
        networkUtility = new NetworkUtility(getContext());
        networkUtility.setOnNewsRetrieveInterface(new OnNewsRetrieveInterface() {
            @Override
            public void success(List<News> newsList) {
                if (newsAdapter == null) {
                    newsAdapter = new NewsAdapter(newsList, getContext());
                    newsRecyclerView.setAdapter(newsAdapter);
                } else {
                    newsAdapter.refresh(newsList);
                }
                showRecyclerView();
            }

            @Override
            public void failure(Exception e) {
                showNoConnection();
            }
        });
        networkUtility.get_news(); // call this method for dummy data
//        networkUtility.get_index_summary(); //call this method for network data
    }

    private void showRecyclerView() {
        news_recycler_view_view.setVisibility(View.VISIBLE);
        no_connection_view.setVisibility(View.INVISIBLE);
        progress_bar_view.setVisibility(View.INVISIBLE);
    }

    private void showNoConnection() {
        news_recycler_view_view.setVisibility(View.INVISIBLE);
        no_connection_view.setVisibility(View.VISIBLE);
        progress_bar_view.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        news_recycler_view_view.setVisibility(View.INVISIBLE);
        no_connection_view.setVisibility(View.INVISIBLE);
        progress_bar_view.setVisibility(View.VISIBLE);
    }
}