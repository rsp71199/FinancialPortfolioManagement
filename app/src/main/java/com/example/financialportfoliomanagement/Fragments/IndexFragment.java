package com.example.financialportfoliomanagement.Fragments;

import android.app.ProgressDialog;
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

import com.example.financialportfoliomanagement.Adapters.IndexAdapter;
import com.example.financialportfoliomanagement.Interfaces.OnIndexDataRetrieveInterface;
import com.example.financialportfoliomanagement.Models.Index;
import com.example.financialportfoliomanagement.NetworkCalls.NetworkUtility;
import com.example.financialportfoliomanagement.R;

import java.util.List;


public class IndexFragment extends Fragment {
    private RecyclerView indexRecyclerView;
    private NetworkUtility networkUtility;
    private IndexAdapter indexAdapter;
    private RecyclerView.LayoutManager indexLayoutManager;
    private LinearLayout no_connection_view,progress_bar_view,index_recycler_view_view;

    public IndexFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkUtility = new NetworkUtility(getContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        no_connection_view = view.findViewById(R.id.no_connection_view);
        index_recycler_view_view = view.findViewById(R.id.index_recycler_view_view);
        progress_bar_view = view.findViewById(R.id.progress_bar_view);
        indexRecyclerView = view.findViewById(R.id.index_recycler_view);
        setIndexRecyclerView();
        showProgressBar();
        setNetworkUtility();
        return view;
    }

    private void setIndexRecyclerView() {
        indexRecyclerView.setHasFixedSize(true);
        indexLayoutManager = new LinearLayoutManager(getContext());
        indexRecyclerView.setLayoutManager(indexLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable verticalDivider = ContextCompat.getDrawable(getActivity(), R.drawable.shape);
        dividerItemDecoration.setDrawable(verticalDivider);
        indexRecyclerView.addItemDecoration(dividerItemDecoration);
    }
    private void setNetworkUtility(){
        networkUtility = new NetworkUtility(getContext());
        networkUtility.setOnIndexDataRetrieveInterface(new OnIndexDataRetrieveInterface() {
            @Override
            public void onIndexDataRetrieveSuccess(List<Index> indexList) {
                if(indexAdapter==null){
                    indexAdapter = new IndexAdapter(indexList,getContext());
                    indexRecyclerView.setAdapter(indexAdapter);
                }else{
                    indexAdapter.refresh(indexList);
                }
                showRecyclerView();

            }
            @Override
            public void onIndexDataRetrieveFailure() {
                showNoConnection();
            }
        });
        networkUtility.process_index_summary_data(""); // call this method for dummy data
//        networkUtility.get_index_summary(); //call this method for network data
    }
    private void showRecyclerView(){
        index_recycler_view_view.setVisibility(View.VISIBLE);
        no_connection_view.setVisibility(View.INVISIBLE);
        progress_bar_view.setVisibility(View.INVISIBLE);
    }
    private void showNoConnection(){
        index_recycler_view_view.setVisibility(View.INVISIBLE);
        no_connection_view.setVisibility(View.VISIBLE);
        progress_bar_view.setVisibility(View.INVISIBLE);
    }
    private void showProgressBar(){
        index_recycler_view_view.setVisibility(View.INVISIBLE);
        no_connection_view.setVisibility(View.INVISIBLE);
        progress_bar_view.setVisibility(View.VISIBLE);
    }

}