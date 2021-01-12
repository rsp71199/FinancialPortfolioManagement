package com.example.financialportfoliomanagement.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.R;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.MyViewHolder> {
    public List<String> mDataset;
//    public Context context;


    public void refresh(List<String> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView shortName;


        public MyViewHolder(View v) {
            super(v);
            shortName = v.findViewById(R.id.shortName);


        }
    }

    public WatchListAdapter(List<String> myDataset) {

        this.mDataset = myDataset;
//        this.context = cont;
    }

    @Override
    public WatchListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watch_list_item, parent, false);
        return new WatchListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final WatchListAdapter.MyViewHolder holder, final int position) {

        holder.shortName.setText(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) return 0;
        return mDataset.size();
    }
}

