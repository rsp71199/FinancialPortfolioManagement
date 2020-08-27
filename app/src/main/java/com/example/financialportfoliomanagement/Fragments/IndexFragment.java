package com.example.financialportfoliomanagement.Fragments;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Models.Index;
import com.example.financialportfoliomanagement.NetworkCalls.DashBoardNetworkUtility;
import com.example.financialportfoliomanagement.R;

import java.util.List;


public class IndexFragment extends Fragment {
    RecyclerView indexRecyclerView;
    DashBoardNetworkUtility dashBoardNetworkUtility;
    private RecyclerView.LayoutManager indexLayoutManager;

    private List<Index> indexList;


    public IndexFragment(List<Index> indexList) {
        this.indexList = indexList;
    }

    public IndexFragment() {
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashBoardNetworkUtility = new DashBoardNetworkUtility(getContext(), new ProgressDialog(getContext()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        indexRecyclerView = view.findViewById(R.id.index_recycler_view);
        setIndexRecyclerView();
        dashBoardNetworkUtility.set_index_recycler_view_data(indexRecyclerView, getContext());
        return view;

    }
}