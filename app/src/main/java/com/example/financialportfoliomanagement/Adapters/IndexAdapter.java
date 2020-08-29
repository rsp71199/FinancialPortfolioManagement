package com.example.financialportfoliomanagement.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Models.Index;
import com.example.financialportfoliomanagement.R;

import java.util.List;

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.MyViewHolder> {

    private List<Index> mDataset;
    private Context context;

    public IndexAdapter(List<Index> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView shortName, regularMarketChange, regularMarketPrice, marketName, regularMarketPreviousClose;

        public MyViewHolder(View v) {
            super(v);
            shortName = v.findViewById(R.id.shortName);
            regularMarketChange = v.findViewById(R.id.regularMarketChange);
            regularMarketPrice = v.findViewById(R.id.regularMarketPrice);
            regularMarketPreviousClose = v.findViewById(R.id.previousMarketPrice);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.index_item, parent, false);


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to the charts activity
            }
        });
        return new IndexAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.shortName.setText(mDataset.get(position).shortName);
        holder.regularMarketPrice.setText(mDataset.get(position).regularMarketPrice);
        holder.regularMarketChange.setText(mDataset.get(position).regularMarketChange);
        holder.regularMarketPreviousClose.setText(mDataset.get(position).market_name);
        float prev_m = Float.parseFloat(mDataset.get(position).market_name);
        if (prev_m > Float.parseFloat(mDataset.get(position).regularMarketPrice)) {
            holder.regularMarketPreviousClose.setTextColor(Color.RED);
        } else {
            holder.regularMarketPreviousClose.setTextColor(Color.GREEN);
        }
        if (mDataset.get(position).loss) {
            holder.regularMarketChange.setTextColor(Color.RED);
        } else {
            holder.regularMarketChange.setTextColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
