package com.example.financialportfoliomanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financialportfoliomanagement.Models.TrendingTicker;
import com.example.financialportfoliomanagement.R;

import java.util.List;

public class TrendingTickersAdapter extends RecyclerView.Adapter<TrendingTickersAdapter.MyViewHolder> {
    private List<TrendingTicker> mDataset;
    private Context context;

    public TrendingTickersAdapter(List<TrendingTicker> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView shortName;
        public TextView regularMarketPrice;
        public TextView regularMarketChange;
        public TextView regularMarketChangePercent;
        public TextView quoteType;


        public MyViewHolder(View v) {
            super(v);
            shortName = v.findViewById(R.id.shortName);
            regularMarketPrice = v.findViewById(R.id.regularMarketPrice);
            regularMarketChange = v.findViewById(R.id.regularMarketChange);
            quoteType = v.findViewById(R.id.quoteType);
        }
    }


    @NonNull
    @Override
    public TrendingTickersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trending_ticker_item, parent, false);


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //navigate to the charts activity
            }
        });
        return new TrendingTickersAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingTickersAdapter.MyViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(context, ChartsActivity.class);
//
//                context.startActivity(i);
            }
        });
        holder.shortName.setText(mDataset.get(position).shortName);
        holder.regularMarketChange.setText(mDataset.get(position).regularMarketChange);
        holder.regularMarketPrice.setText(mDataset.get(position).regularMarketPrice);
        holder.quoteType.setText(mDataset.get(position).quoteType);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
